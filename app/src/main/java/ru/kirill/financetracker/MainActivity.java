package ru.kirill.financetracker;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
public class MainActivity extends Activity {
    private WebView webView;
    public class StoreBridge {
        @JavascriptInterface
        public String getItem(String key) {
            return getSharedPreferences("finance_store", MODE_PRIVATE).getString(key, "");
        }
        @JavascriptInterface
        public void setItem(String key, String val) {
            getSharedPreferences("finance_store", MODE_PRIVATE).edit().putString(key, val).apply();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        if (w != null) {
            w.setStatusBarColor(Color.parseColor("#121212"));
            w.setNavigationBarColor(Color.parseColor("#121212"));
        }
        webView = new WebView(this);
        webView.setBackgroundColor(Color.parseColor("#121212"));
        setContentView(webView);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        webView.addJavascriptInterface(new StoreBridge(), "AndroidStore");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });
        webView.loadUrl("file:///android_asset/index.html");
    }
}
