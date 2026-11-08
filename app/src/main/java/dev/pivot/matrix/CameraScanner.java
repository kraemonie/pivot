package dev.pivot.matrix;
import android.app.Activity;
import android.content.Intent;
import android.content.ClipData;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.graphics.Rect;
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
final class CameraScanner {
 interface Callback { void done(String text,String error); }
 private final Activity activity;private final Callback callback;
 private Uri pending;private boolean busy,closed,processing;private TextRecognizer recognizer;
 static final int CAMERA=61,PHOTO=62;
 CameraScanner(Activity activity,Callback callback,Bundle state){this.activity=activity;this.callback=callback;if(state!=null){String uri=state.getString("scan_uri");if(uri!=null){pending=Uri.parse(uri);busy=true;}if(state.getBoolean("scan_processing",false)){if(pending!=null)activity.revokeUriPermission(pending,Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);pending=null;busy=false;callback.done("","Scan interrupted. Please take or choose the photo again.");}}}
 void save(Bundle state){state.putBoolean("scan_processing",processing);if(pending!=null)state.putString("scan_uri",pending.toString());}
 void open(boolean photo){if(busy)return;busy=true;try{
  if(photo){activity.startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("image/*"),PHOTO);}
  else{File dir=new File(activity.getCacheDir(),"camera");if(!dir.exists()&&!dir.mkdirs())throw new java.io.IOException();File image=new File(dir,"matrix.jpg");pending=FileProvider.getUriForFile(activity,"dev.pivot.matrix.fileprovider",image);Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT,pending).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);intent.setClipData(ClipData.newRawUri("Matrix photo",pending));activity.startActivityForResult(intent,CAMERA);}
 }catch(Exception e){finish("","Could not open the camera or photo picker. Try another camera app or enter the matrix manually.");}}
 void result(int request,int result,Intent data){if(request!=CAMERA&&request!=PHOTO)return;if(result!=Activity.RESULT_OK){finish("","Scan cancelled. Your matrix is unchanged.");return;}Uri uri=request==CAMERA?pending:data==null?null:data.getData();if(uri==null){finish("","No photo was returned. Please try again.");return;}processing=true;try{recognizer=TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);recognizer.process(InputImage.fromFilePath(activity,uri)).addOnSuccessListener(text->{String rows=readRows(text);finish(rows,rows.trim().isEmpty()?"No readable numbers found. Take a sharper photo with just the matrix in view.":null);}).addOnFailureListener(e->finish("","Could not read this photo. Use a sharp, well-lit photo of a printed matrix."));}catch(Exception e){finish("","Could not open this image. Please choose another photo.");}}
 private void finish(String text,String error){busy=false;processing=false;if(pending!=null){activity.revokeUriPermission(pending,Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);pending=null;}if(recognizer!=null){recognizer.close();recognizer=null;}if(!closed)callback.done(text,error);}
 void close(){closed=true;if(recognizer!=null){recognizer.close();recognizer=null;}}
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