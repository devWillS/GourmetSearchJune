package test.engineering.com.gourmetsearchjune.View.StoreDetail;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import test.engineering.com.gourmetsearchjune.Model.Response.StoreResponse;
import test.engineering.com.gourmetsearchjune.R;

public class WebActivity extends AppCompatActivity implements CustomWebViewClient.CustomWebViewClientListener {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private WebView webView;
    private ImageView backButton;
    private ImageView forwardButton;
    private ImageView reloadButton;

    private StoreResponse selectedStore;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        selectedStore = (StoreResponse) getIntent().getSerializableExtra(getString(R.string.STORE));

        setupView();
        setupToolbar();

        webView = findViewById(R.id.web_view);
        // 標準ブラウザをキャンセル
        webView.setWebViewClient(new CustomWebViewClient(this));
        // アプリ起動時に読み込むURL
        webView.loadUrl(selectedStore.getUrls().getPc());
        // javascriptを許可する
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    private void setupView() {
        backButton = findViewById(R.id.backButton);
        forwardButton = findViewById(R.id.forwardButton);
        reloadButton = findViewById(R.id.reloadButton);

        backButton.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        forwardButton.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goBack();
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goForward();
            }
        });

        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(selectedStore.getName());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setHasOptionsMenu(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        boolean result = true;

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        return result;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 端末のBACKキーで一つ前のページヘ戻る
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onPageFinished(WebView view, String url) {

        backButton.setEnabled(view.canGoBack());
        forwardButton.setEnabled(view.canGoForward());

        if (view.canGoBack()) {
            backButton.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        } else {
            backButton.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        }

        if (view.canGoForward()) {
            forwardButton.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        } else {
            forwardButton.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        }
    }
}
