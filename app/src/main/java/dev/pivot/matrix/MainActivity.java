package dev.pivot.matrix;
import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.graphics.Color;
import android.webkit.*;
import android.widget.FrameLayout;
import androidx.webkit.WebViewAssetLoader;
import java.io.ByteArrayInputStream;
public class MainActivity extends Activity {
 private WebView web;private FrameLayout frame;private CameraScanner scanner;private String pendingScanResult;
 @Override public void onCreate(Bundle state){super.onCreate(state);
 frame=new FrameLayout(this);frame.setBackgroundColor(Color.rgb(212,255,56));
 frame.setOnApplyWindowInsetsListener((v,insets)->{if(Build.VERSION.SDK_INT>=30){android.graphics.Insets safe=insets.getInsets(android.view.WindowInsets.Type.systemBars()|android.view.WindowInsets.Type.displayCutout()|android.view.WindowInsets.Type.ime());v.setPadding(safe.left,safe.top,safe.right,safe.bottom);}return insets;});
 web=new WebView(this);frame.addView(web,new FrameLayout.LayoutParams(-1,-1));setContentView(frame);
 web.setBackgroundColor(Color.rgb(212,255,56));WebSettings s=web.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setAllowFileAccess(false);s.setAllowContentAccess(false);s.setBlockNetworkLoads(true);
 if(Build.VERSION.SDK_INT>=33)s.setAlgorithmicDarkeningAllowed(false);else if(Build.VERSION.SDK_INT>=29)s.setForceDark(WebSettings.FORCE_DARK_OFF);
 WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
 WebViewAssetLoader assets=new WebViewAssetLoader.Builder().addPathHandler("/assets/",new WebViewAssetLoader.AssetsPathHandler(this)).build();
 web.setWebViewClient(new WebViewClient(){@Override public void onPageFinished(WebView v,String url){flushScan();}@Override public WebResourceResponse shouldInterceptRequest(WebView v,WebResourceRequest request){WebResourceResponse response=assets.shouldInterceptRequest(request.getUrl());return response!=null?response:new WebResourceResponse("text/plain","UTF-8",404,"Not found",null,new ByteArrayInputStream(new byte[0]));}@Override public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest request){return true;}});
 scanner=new CameraScanner(this,(text,error)->{pendingScanResult="window.onPivotScan&&window.onPivotScan("+org.json.JSONObject.quote(text)+","+(error==null?"null":org.json.JSONObject.quote(error))+")";flushScan();},state);
 web.addJavascriptInterface(new IntroBridge(),"PivotBridge");
 applySection(getSharedPreferences("intro",MODE_PRIVATE).getBoolean("completed",false)?"solve":"welcome");
 web.loadUrl("https://appassets.androidplatform.net/assets/index.html");
 if(Build.VERSION.SDK_INT>=33)getOnBackInvokedDispatcher().registerOnBackInvokedCallback(android.window.OnBackInvokedDispatcher.PRIORITY_DEFAULT,this::back);
 }
 public class IntroBridge {
 @JavascriptInterface public void setSection(String section){runOnUiThread(()->applySection(section));}
 @JavascriptInterface public void scanMatrix(boolean photo){runOnUiThread(()->{if(web!=null&&"https://appassets.androidplatform.net/assets/index.html".equals(web.getUrl()))scanner.open(photo);});}
 @JavascriptInterface public boolean needsWelcome(){return !getSharedPreferences("intro",MODE_PRIVATE).getBoolean("completed",false);}
 @JavascriptInterface public void completeWelcome(){getSharedPreferences("intro",MODE_PRIVATE).edit().putBoolean("completed",true).apply();}
 }
 private void applySection(String section){
 int color;boolean light=true;
 if("welcome".equals(section)){color=Color.rgb(116,39,245);light=false;}
 else if("practice".equals(section))color=Color.rgb(241,104,189);
 else if("learn".equals(section))color=Color.rgb(60,219,231);
 else color=Color.rgb(212,255,56);
 frame.setBackgroundColor(color);getWindow().setStatusBarColor(color);getWindow().setNavigationBarColor(color);
 if(Build.VERSION.SDK_INT>=30){android.view.WindowInsetsController controller=getWindow().getInsetsController();if(controller!=null){int bars=android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS|android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS;controller.setSystemBarsAppearance(light?bars:0,bars);}}
 else{int bars=android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;int flags=getWindow().getDecorView().getSystemUiVisibility();getWindow().getDecorView().setSystemUiVisibility(light?flags|bars:flags&~bars);}
 }
 private void flushScan(){if(web!=null&&pendingScanResult!=null){String payload=pendingScanResult;web.evaluateJavascript("(function(){if(!window.onPivotScan)return false;"+payload+";return true;})()",result->{if("true".equals(result)&&payload.equals(pendingScanResult))pendingScanResult=null;});}}
 @Override protected void onSaveInstanceState(Bundle state){super.onSaveInstanceState(state);scanner.save(state);}
 @Override protected void onActivityResult(int request,int result,android.content.Intent data){super.onActivityResult(request,result,data);if(result==RESULT_OK&&web!=null)web.evaluateJavascript("document.getElementById('scanStatus').textContent='Reading numbers on your device...'",null);scanner.result(request,result,data);}
 private void back(){web.evaluateJavascript("window.pivotBack&&window.pivotBack()",result->{if(!"true".equals(result))finish();});}
 @Override public void onBackPressed(){back();}
 @Override protected void onDestroy(){if(scanner!=null)scanner.close();if(web!=null){((FrameLayout)web.getParent()).removeView(web);web.destroy();web=null;}super.onDestroy();}
}
