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
 private WebView web;
 @Override public void onCreate(Bundle state){super.onCreate(state);
 FrameLayout frame=new FrameLayout(this);frame.setBackgroundColor(Color.rgb(18,28,48));
 frame.setOnApplyWindowInsetsListener((v,insets)->{if(Build.VERSION.SDK_INT>=30){android.graphics.Insets safe=insets.getInsets(android.view.WindowInsets.Type.systemBars()|android.view.WindowInsets.Type.displayCutout()|android.view.WindowInsets.Type.ime());v.setPadding(safe.left,safe.top,safe.right,safe.bottom);}return insets;});
 web=new WebView(this);frame.addView(web,new FrameLayout.LayoutParams(-1,-1));setContentView(frame);
 web.setBackgroundColor(Color.rgb(242,244,237));WebSettings s=web.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setAllowFileAccess(false);s.setAllowContentAccess(false);s.setBlockNetworkLoads(true);
 if(Build.VERSION.SDK_INT>=33)s.setAlgorithmicDarkeningAllowed(false);else if(Build.VERSION.SDK_INT>=29)s.setForceDark(WebSettings.FORCE_DARK_OFF);
 WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
 WebViewAssetLoader assets=new WebViewAssetLoader.Builder().addPathHandler("/assets/",new WebViewAssetLoader.AssetsPathHandler(this)).build();
 web.setWebViewClient(new WebViewClient(){@Override public WebResourceResponse shouldInterceptRequest(WebView v,WebResourceRequest request){WebResourceResponse response=assets.shouldInterceptRequest(request.getUrl());return response!=null?response:new WebResourceResponse("text/plain","UTF-8",404,"Not found",null,new ByteArrayInputStream(new byte[0]));}@Override public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest request){return true;}});
 web.addJavascriptInterface(new IntroBridge(),"PivotBridge");
 web.loadUrl("https://appassets.androidplatform.net/assets/index.html");
 if(Build.VERSION.SDK_INT>=33)getOnBackInvokedDispatcher().registerOnBackInvokedCallback(android.window.OnBackInvokedDispatcher.PRIORITY_DEFAULT,this::back);
 }
 public class IntroBridge {
 @JavascriptInterface public boolean needsWelcome(){return !getSharedPreferences("intro",MODE_PRIVATE).getBoolean("completed",false);}
 @JavascriptInterface public void completeWelcome(){getSharedPreferences("intro",MODE_PRIVATE).edit().putBoolean("completed",true).apply();}
 }
 private void back(){web.evaluateJavascript("window.pivotBack&&window.pivotBack()",result->{if(!"true".equals(result))finish();});}
 @Override public void onBackPressed(){back();}
 @Override protected void onDestroy(){if(web!=null){((FrameLayout)web.getParent()).removeView(web);web.destroy();web=null;}super.onDestroy();}
}