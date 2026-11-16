package dev.pivot.matrix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.core.content.FileProvider;
import java.io.*;
import java.util.concurrent.*;

/** A local crop/rotation step shared by camera capture and imported photographs. */
public class ScanCropActivity extends Activity {
 private CropView crop;
 private Bitmap bitmap;
 private CheckBox handwriting;
 private Button read;
 private final ExecutorService worker=Executors.newSingleThreadExecutor();
 private boolean closed;
 @Override public void onCreate(Bundle state){
  super.onCreate(state);
  LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(0xFF17142B);root.setPadding(dp(18),dp(14),dp(18),dp(18));
  root.setOnApplyWindowInsetsListener((v,insets)->{if(android.os.Build.VERSION.SDK_INT>=30){Insets safe=insets.getInsets(WindowInsets.Type.systemBars()|WindowInsets.Type.displayCutout());v.setPadding(dp(18)+safe.left,dp(14)+safe.top,dp(18)+safe.right,dp(18)+safe.bottom);}return insets;});
  TextView title=new TextView(this);title.setText("Crop your matrix");title.setTextColor(Color.WHITE);title.setTextSize(25);title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);root.addView(title);
  TextView hint=new TextView(this);hint.setText("Drag the corners around the numbers. Leave a little space; crop out headings and other matrices.");hint.setTextColor(0xFFDDD5EF);hint.setTextSize(14);hint.setPadding(0,dp(8),0,dp(14));root.addView(hint);
  crop=new CropView(this);root.addView(crop,new LinearLayout.LayoutParams(-1,0,1));
  handwriting=new CheckBox(this);handwriting.setText("Handwritten numbers");handwriting.setTextColor(Color.WHITE);handwriting.setTextSize(15);handwriting.setButtonTintList(android.content.res.ColorStateList.valueOf(0xFFD4FF38));root.addView(handwriting);
  LinearLayout tools=new LinearLayout(this);tools.setPadding(0,dp(7),0,dp(12));root.addView(tools);
  Button cancel=button("Cancel",false),reset=button("Reset crop",false),rotate=button("Rotate 90°",false);
  for(Button button:new Button[]{cancel,reset,rotate}){LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,dp(48),1);params.setMargins(dp(3),0,dp(3),0);tools.addView(button,params);}
  cancel.setOnClickListener(v->finish());reset.setOnClickListener(v->crop.reset());rotate.setOnClickListener(v->{if(bitmap!=null){Matrix matrix=new Matrix();matrix.postRotate(90);Bitmap previous=bitmap;bitmap=Bitmap.createBitmap(previous,0,0,previous.getWidth(),previous.getHeight(),matrix,true);crop.setBitmap(bitmap);if(previous!=bitmap)previous.recycle();}});
  read=button("Read cropped matrix",true);read.setEnabled(false);root.addView(read,new LinearLayout.LayoutParams(-1,dp(54)));read.setOnClickListener(v->complete());
  setContentView(root);getWindow().setStatusBarColor(0xFF17142B);getWindow().setNavigationBarColor(0xFF17142B);getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility()&~(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR));
  Uri uri=getIntent().getData();worker.execute(()->{try{Bitmap decoded=ScanImage.decode(this,uri,2000);runOnUiThread(()->{if(closed){decoded.recycle();return;}bitmap=decoded;crop.setBitmap(bitmap);read.setEnabled(true);});}catch(Exception|OutOfMemoryError e){runOnUiThread(()->{if(!closed){Toast.makeText(this,"Could not open this photo. Try a smaller image.",Toast.LENGTH_LONG).show();finish();}});}});
 }
 private int dp(int value){return Math.round(value*getResources().getDisplayMetrics().density);}
 private Button button(String text,boolean primary){Button button=new Button(this);button.setText(text);button.setAllCaps(false);button.setTextSize(primary?16:12);button.setTextColor(Color.WHITE);GradientDrawable background=new GradientDrawable();background.setColor(primary?0xFF7427F5:0xFF302840);background.setCornerRadius(dp(11));background.setStroke(dp(2),primary?0xFFD4FF38:0xFF736782);button.setBackground(background);return button;}
 private void complete(){
  if(bitmap==null)return;read.setEnabled(false);Rect area=crop.getCrop();boolean handwritten=handwriting.isChecked();
  worker.execute(()->{try{Bitmap result=Bitmap.createBitmap(area.width(),area.height(),Bitmap.Config.ARGB_8888);Canvas flat=new Canvas(result);flat.drawColor(Color.WHITE);flat.drawBitmap(bitmap,area,new Rect(0,0,area.width(),area.height()),new Paint(Paint.FILTER_BITMAP_FLAG));File directory=new File(getCacheDir(),"camera");if(!directory.exists()&&!directory.mkdirs())throw new IOException();File file=new File(directory,"matrix-crop.jpg");try(OutputStream stream=new FileOutputStream(file)){if(!result.compress(Bitmap.CompressFormat.JPEG,97,stream))throw new IOException();}result.recycle();Uri uri=FileProvider.getUriForFile(this,"dev.pivot.matrix.fileprovider",file);runOnUiThread(()->{if(!closed){setResult(RESULT_OK,new Intent().setData(uri).putExtra("handwritten",handwritten));finish();}});}catch(Exception e){runOnUiThread(()->{if(!closed){read.setEnabled(true);Toast.makeText(this,"Could not crop the photo. Please try again.",Toast.LENGTH_LONG).show();}});}});
 }
 @Override protected void onDestroy(){closed=true;worker.shutdown();super.onDestroy();}

 static final class CropView extends View {
  private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
  private final RectF image=new RectF(),selection=new RectF();
  private Bitmap bitmap;
  private float lastX,lastY;private int handle=-1;
  CropView(android.content.Context context){super(context);setContentDescription("Matrix crop area. Drag a corner to resize, or drag inside to move the crop.");setFocusable(true);}
  void setBitmap(Bitmap value){bitmap=value;reset();}
  void reset(){selection.set(0,0,1,1);invalidate();}
  Rect getCrop(){int left=Math.max(0,Math.round(selection.left*bitmap.getWidth())),top=Math.max(0,Math.round(selection.top*bitmap.getHeight()));int right=Math.min(bitmap.getWidth(),Math.round(selection.right*bitmap.getWidth())),bottom=Math.min(bitmap.getHeight(),Math.round(selection.bottom*bitmap.getHeight()));return new Rect(left,top,Math.max(left+1,right),Math.max(top+1,bottom));}
  @Override protected void onDraw(Canvas canvas){super.onDraw(canvas);if(bitmap==null)return;float scale=Math.min((getWidth()-8f)/bitmap.getWidth(),(getHeight()-8f)/bitmap.getHeight());float w=bitmap.getWidth()*scale,h=bitmap.getHeight()*scale;image.set((getWidth()-w)/2,(getHeight()-h)/2,(getWidth()+w)/2,(getHeight()+h)/2);canvas.drawBitmap(bitmap,null,image,paint);RectF box=display();paint.setColor(0xAA17142B);canvas.drawRect(image.left,image.top,image.right,box.top,paint);canvas.drawRect(image.left,box.bottom,image.right,image.bottom,paint);canvas.drawRect(image.left,box.top,box.left,box.bottom,paint);canvas.drawRect(box.right,box.top,image.right,box.bottom,paint);paint.setColor(0xFFD4FF38);paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(4);canvas.drawRect(box,paint);paint.setStyle(Paint.Style.FILL);for(float x:new float[]{box.left,box.right})for(float y:new float[]{box.top,box.bottom})canvas.drawCircle(x,y,9,paint);}
  private RectF display(){return new RectF(image.left+selection.left*image.width(),image.top+selection.top*image.height(),image.left+selection.right*image.width(),image.top+selection.bottom*image.height());}
  @Override public boolean onTouchEvent(MotionEvent event){if(bitmap==null||image.width()<=0)return false;float x=(event.getX()-image.left)/image.width(),y=(event.getY()-image.top)/image.height();if(event.getAction()==MotionEvent.ACTION_DOWN){RectF box=display();float radius=40*getResources().getDisplayMetrics().density;handle=-1;float distance=Float.MAX_VALUE;float[] xs={box.left,box.right,box.left,box.right},ys={box.top,box.top,box.bottom,box.bottom};for(int i=0;i<4;i++){float d=(float)Math.hypot(event.getX()-xs[i],event.getY()-ys[i]);if(d<radius&&d<distance){handle=i;distance=d;}}if(handle<0&&box.contains(event.getX(),event.getY()))handle=4;if(handle<0)return false;lastX=x;lastY=y;getParent().requestDisallowInterceptTouchEvent(true);return true;}if(event.getAction()==MotionEvent.ACTION_MOVE&&handle>=0){float min=.045f;if(handle==4){float dx=Math.max(-selection.left,Math.min(1-selection.right,x-lastX)),dy=Math.max(-selection.top,Math.min(1-selection.bottom,y-lastY));selection.offset(dx,dy);}else{if(handle==0||handle==2)selection.left=Math.max(0,Math.min(selection.right-min,x));else selection.right=Math.min(1,Math.max(selection.left+min,x));if(handle==0||handle==1)selection.top=Math.max(0,Math.min(selection.bottom-min,y));else selection.bottom=Math.min(1,Math.max(selection.top+min,y));}lastX=x;lastY=y;invalidate();return true;}if(event.getAction()==MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_CANCEL){handle=-1;performClick();return true;}return true;}
  @Override public boolean performClick(){super.performClick();return true;}
 }
}
