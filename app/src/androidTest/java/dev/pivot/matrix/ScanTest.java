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
 @Test public void testPrintedMatrixRecognition() throws Exception {
  Bitmap bitmap=Bitmap.createBitmap(1000,650,Bitmap.Config.ARGB_8888);Canvas canvas=new Canvas(bitmap);canvas.drawColor(Color.WHITE);Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);paint.setColor(Color.BLACK);paint.setTextSize(100);paint.setTypeface(android.graphics.Typeface.MONOSPACE);
  canvas.drawText("1",180,220,paint);canvas.drawText("2",480,220,paint);canvas.drawText("3",180,420,paint);canvas.drawText("4",480,420,paint);
  CountDownLatch latch=new CountDownLatch(1);String[] result={null},error={null};TextRecognizer recognizer=TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
  recognizer.process(InputImage.fromBitmap(bitmap,0)).addOnSuccessListener(text->{result[0]=CameraScanner.readRows(text);latch.countDown();}).addOnFailureListener(e->{error[0]=e.toString();latch.countDown();});
  assertTrue("OCR timed out",latch.await(60,TimeUnit.SECONDS));assertNull(error[0]);assertEquals("1 2\n3 4",result[0]);recognizer.close();bitmap.recycle();
 }
}