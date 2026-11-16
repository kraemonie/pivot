package dev.pivot.matrix;
import android.app.Activity;
import android.content.Intent;
import android.content.ClipData;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.util.Base64;
import com.google.android.gms.tasks.Tasks;
import androidx.core.content.FileProvider;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.io.ByteArrayOutputStream;
final class CameraScanner {
 interface Callback { void done(String text,String error,String note,String preview); }
 private final Activity activity;private final Callback callback;
 private Uri pending;private boolean busy,processing;private volatile boolean closed;private TextRecognizer recognizer;
 private final ExecutorService worker=Executors.newSingleThreadExecutor();
 static final int CAMERA=61,PHOTO=62,CROP=63;
 CameraScanner(Activity activity,Callback callback,Bundle state){this.activity=activity;this.callback=callback;if(state!=null){String uri=state.getString("scan_uri");if(uri!=null)pending=Uri.parse(uri);busy=state.getBoolean("scan_busy",false);if(state.getBoolean("scan_processing",false))finish("","Scan interrupted. Please take or choose the photo again.");}}
 void save(Bundle state){state.putBoolean("scan_busy",busy);state.putBoolean("scan_processing",processing);if(pending!=null)state.putString("scan_uri",pending.toString());}
 void open(boolean photo){if(busy)return;busy=true;try{
  if(photo){activity.startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("image/*"),PHOTO);}
  else{File dir=new File(activity.getCacheDir(),"camera");if(!dir.exists()&&!dir.mkdirs())throw new java.io.IOException();File image=new File(dir,"matrix.jpg");pending=FileProvider.getUriForFile(activity,"dev.pivot.matrix.fileprovider",image);Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT,pending).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);intent.setClipData(ClipData.newRawUri("Matrix photo",pending));activity.startActivityForResult(intent,CAMERA);}
 }catch(Exception e){finish("","Could not open the camera or photo picker. Try another camera app or enter the matrix manually.");}}
 void result(int request,int result,Intent data){if(request!=CAMERA&&request!=PHOTO&&request!=CROP)return;if(result!=Activity.RESULT_OK){finish("","Scan cancelled. Your matrix is unchanged.");return;}Uri uri=request==CAMERA?pending:data==null?null:data.getData();if(uri==null){finish("","No photo was returned. Please try again.");return;}if(request!=CROP){try{activity.startActivityForResult(new Intent(activity,ScanCropActivity.class).setData(uri).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION),CROP);}catch(Exception e){finish("","Could not open the crop screen. Choose another photo.");}return;}processing=true;boolean handwritten=data.getBooleanExtra("handwritten",false);worker.execute(()->recognize(uri,handwritten));}
 private void recognize(Uri uri,boolean handwritten){Bitmap original=null,clean=null,contrast=null;TextRecognizer reader=null;try{original=ScanImage.decode(activity,uri,1800);ScanImage image=new ScanImage(original);clean=image.cleaned(original,false);contrast=image.cleaned(original,true);String rows,note;if(handwritten){DigitReader.Result result=new DigitReader(activity).read(image);rows=result.text;note=result.uncertain>0?"Unclear digits are marked ?. Compare the photo and correct them before computing.":"Handwriting read on your device. Compare every digit with the photo.";}else{reader=TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);String first=readRows(Tasks.await(reader.process(InputImage.fromBitmap(clean,0)),25,TimeUnit.SECONDS));String second=readRows(Tasks.await(reader.process(InputImage.fromBitmap(contrast,0)),25,TimeUnit.SECONDS));rows=quality(second)>quality(first)?second:first;note=first.equals(second)?"Two scan passes agree. Check every entry before computing.":"The scan passes differ. Compare every entry with the photo.";}if(image.removedBrackets>0)note="Matrix brackets removed. "+note;if(rows.trim().isEmpty()){finish("","No clear matrix found. Crop closer, use brighter light, or enable Handwritten numbers.");return;}ByteArrayOutputStream bytes=new ByteArrayOutputStream();float scale=Math.min(1,850f/Math.max(clean.getWidth(),clean.getHeight()));Bitmap thumbnail=Bitmap.createScaledBitmap(clean,Math.max(1,Math.round(clean.getWidth()*scale)),Math.max(1,Math.round(clean.getHeight()*scale)),true);thumbnail.compress(Bitmap.CompressFormat.JPEG,85,bytes);if(thumbnail!=clean)thumbnail.recycle();finish(rows,null,note,"data:image/jpeg;base64,"+Base64.encodeToString(bytes.toByteArray(),Base64.NO_WRAP));}catch(Exception|OutOfMemoryError e){finish("","Could not read this crop. Try a sharper photo or change the handwriting option.");}finally{if(reader!=null)reader.close();if(original!=null)original.recycle();if(clean!=null)clean.recycle();if(contrast!=null)contrast.recycle();}}
 private static int quality(String text){if(text.trim().isEmpty())return -1;String[] rows=text.trim().split("\\n");if(rows.length>5)return -1;int columns=-1;for(String row:rows){int size=row.trim().split("\\s+").length;if(size<1||size>5)return -1;if(columns<0)columns=size;else if(columns!=size)return 0;}return rows.length*columns;}
 private void finish(String text,String error){finish(text,error,"","");}
 private void finish(String text,String error,String note,String preview){activity.runOnUiThread(()->{busy=false;processing=false;if(pending!=null){activity.revokeUriPermission(pending,Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);pending=null;}if(!closed)callback.done(text,error,note,preview);});}
 void close(){closed=true;worker.shutdown();}
 static String readRows(Text text){
  List<Text.Element> elements=new ArrayList<>();for(Text.TextBlock block:text.getTextBlocks())for(Text.Line line:block.getLines())for(Text.Element element:line.getElements())if(element.getBoundingBox()!=null)elements.add(element);
  elements.sort(Comparator.comparingInt(e->e.getBoundingBox().centerY()));
  List<List<Text.Element>> rows=new ArrayList<>();List<Text.Element> row=null;float center=0,height=0;int count=0;
  for(Text.Element e:elements){Rect box=e.getBoundingBox();if(row==null||Math.abs(box.centerY()-center)>Math.max(8,Math.min(height,box.height())*.6)){row=new ArrayList<>();rows.add(row);center=box.centerY();height=box.height();count=0;}row.add(e);center=(center*count+box.centerY())/(count+1);height=(height*count+box.height())/(count+1);count++;}
  StringBuilder out=new StringBuilder();for(List<Text.Element> line:rows){line.sort(Comparator.comparingInt(e->e.getBoundingBox().left));StringBuilder values=new StringBuilder();for(Text.Element e:line){String token=e.getText().replaceAll("[\\[\\]()|]", " ").trim();if(!token.isEmpty()){if(values.length()>0)values.append(' ');values.append(token);}}String value=numericRow(values.toString());if(value.isEmpty())continue;if(out.length()>0)out.append('\n');out.append(value);}return out.toString();
 }
 static String numericRow(String text){
  String clean=text.replaceAll("[−–]","-").replaceAll("[\\[\\]()|]"," ").replaceAll("^\\s*[A-Za-z]\\s*=\\s*", "").replaceAll("-\\s+(?=\\d)", "-");
  if(java.util.regex.Pattern.compile("[\\p{L}]").matcher(clean).find())return "";
  java.util.regex.Matcher matcher=java.util.regex.Pattern.compile("[+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+)(?:/[+-]?\\d+)?").matcher(clean);
  StringBuilder out=new StringBuilder();while(matcher.find()){if(out.length()>0)out.append(' ');out.append(matcher.group());}return out.toString();
 }

}
