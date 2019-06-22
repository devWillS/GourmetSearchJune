package test.engineering.com.gourmetsearchjune.View.StoreDetail;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {
    public interface CustomWebViewClientListener {
        void onPageFinished(WebView view, String url);
    }

    private CustomWebViewClientListener listener;

    CustomWebViewClient(CustomWebViewClientListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        listener.onPageFinished(view, url);
    }
}
