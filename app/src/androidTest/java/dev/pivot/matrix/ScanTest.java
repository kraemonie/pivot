package dev.pivot.matrix;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static org.junit.Assert.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
@RunWith(AndroidJUnit4.class)
public class ScanTest {
 @Test public void testBracketsAreRemovedWithoutDroppingOnes(){
  Bitmap bitmap=Bitmap.createBitmap(700,500,Bitmap.Config.ARGB_8888);Canvas canvas=new Canvas(bitmap);canvas.drawColor(Color.WHITE);Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);paint.setColor(Color.BLACK);paint.setTextSize(80);paint.setTypeface(android.graphics.Typeface.MONOSPACE);canvas.drawText("1",200,180,paint);canvas.drawText("2",400,180,paint);canvas.drawText("1",200,340,paint);canvas.drawText("4",400,340,paint);paint.setStrokeWidth(7);canvas.drawLine(90,60,90,390,paint);canvas.drawLine(90,60,130,60,paint);canvas.drawLine(90,390,130,390,paint);canvas.drawLine(580,60,580,390,paint);canvas.drawLine(540,60,580,60,paint);canvas.drawLine(540,390,580,390,paint);ScanImage image=new ScanImage(bitmap);assertEquals(2,image.removedBrackets);assertEquals(4,image.components.size());bitmap.recycle();
 }
 @Test public void testBareOneIsNeverTreatedAsBracket(){
  Bitmap bitmap=Bitmap.createBitmap(500,400,Bitmap.Config.ARGB_8888);Canvas canvas=new Canvas(bitmap);canvas.drawColor(Color.WHITE);Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);paint.setColor(Color.BLACK);paint.setTextSize(80);paint.setTypeface(android.graphics.Typeface.MONOSPACE);canvas.drawText("1",80,140,paint);canvas.drawText("1",280,140,paint);canvas.drawText("1",80,300,paint);canvas.drawText("1",280,300,paint);ScanImage image=new ScanImage(bitmap);assertEquals(0,image.removedBrackets);assertEquals(4,image.components.size());bitmap.recycle();
 }
 @Test public void testDigitModelSerializationAndPredictions()throws Exception{
  android.content.Context target=androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();DigitReader reader=new DigitReader(target);int correct=0;
  try(java.io.DataInputStream input=new java.io.DataInputStream(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext().getAssets().open("digit-fixtures.bin"))){int count=input.readInt();assertEquals(100,count);for(int sample=0;sample<count;sample++){int label=input.readUnsignedByte();float[] values=new float[784];for(int i=0;i<784;i++)values[i]=input.readUnsignedByte()/255f;if(reader.predict(values).digit==label)correct++;}}
  assertTrue("Digit reader regression: "+correct,correct>=94);
 }
 @Test public void testHandwrittenMatrixWithBrackets()throws Exception{
  android.content.Context target=androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();Bitmap bitmap=android.graphics.BitmapFactory.decodeStream(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext().getAssets().open("handwritten-matrix.png"));ScanImage image=new ScanImage(bitmap);assertEquals(2,image.removedBrackets);DigitReader.Result result=new DigitReader(target).read(image);assertEquals("1 2\n3 4",result.text);bitmap.recycle();
 }
 @Test public void testCropDragAndReset(){
  androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().runOnMainSync(()->{android.content.Context context=androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();android.widget.FrameLayout parent=new android.widget.FrameLayout(context);ScanCropActivity.CropView view=new ScanCropActivity.CropView(context);parent.addView(view);view.layout(0,0,600,600);Bitmap source=Bitmap.createBitmap(400,200,Bitmap.Config.ARGB_8888);view.setBitmap(source);Bitmap surface=Bitmap.createBitmap(600,600,Bitmap.Config.ARGB_8888);view.draw(new Canvas(surface));android.view.MotionEvent down=android.view.MotionEvent.obtain(0,0,android.view.MotionEvent.ACTION_DOWN,20,160,0);android.view.MotionEvent move=android.view.MotionEvent.obtain(0,20,android.view.MotionEvent.ACTION_MOVE,120,220,0);view.onTouchEvent(down);view.onTouchEvent(move);android.graphics.Rect area=view.getCrop();assertTrue(area.left>30);assertTrue(area.top>20);assertTrue(area.right<=400&&area.bottom<=200);view.reset();assertTrue(view.getCrop().width()>350);down.recycle();move.recycle();source.recycle();surface.recycle();});
 }
 @Test public void testPrintedMatrixRecognition() throws Exception {
  Bitmap bitmap=Bitmap.createBitmap(1000,650,Bitmap.Config.ARGB_8888);Canvas canvas=new Canvas(bitmap);canvas.drawColor(Color.WHITE);Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);paint.setColor(Color.BLACK);paint.setTextSize(100);paint.setTypeface(android.graphics.Typeface.MONOSPACE);
  canvas.drawText("1",180,220,paint);canvas.drawText("2",480,220,paint);canvas.drawText("3",180,420,paint);canvas.drawText("4",480,420,paint);
  CountDownLatch latch=new CountDownLatch(1);String[] result={null},error={null};TextRecognizer recognizer=TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
  recognizer.process(InputImage.fromBitmap(bitmap,0)).addOnSuccessListener(text->{result[0]=CameraScanner.readRows(text);latch.countDown();}).addOnFailureListener(e->{error[0]=e.toString();latch.countDown();});
  assertTrue("OCR timed out",latch.await(60,TimeUnit.SECONDS));assertNull(error[0]);assertEquals("1 2\n3 4",result[0]);recognizer.close();bitmap.recycle();
 }
 @Test public void testOnlyNumericMatrixRows(){
  assertEquals("",CameraScanner.numericRow("Example 2"));
  assertEquals("",CameraScanner.numericRow("The rank is 3"));
  assertEquals("1 -2 3/4",CameraScanner.numericRow("A = [ 1 − 2 3/4 ]"));
  assertEquals("0.25 -0.5",CameraScanner.numericRow("[ 0.25 -0.5 ]"));
 }

}
