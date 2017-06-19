package com.malav.medicinesontheway.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.malav.medicinesontheway.R;

/**
 * Created by shahmalav on 21/08/16.
 */

public class ContactUsWebView extends AppCompatActivity {

    WebView webview;
    String websiteUrl, title;
    /*ProgressDialog mProgressDialog;*/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_webview);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,Window.PROGRESS_VISIBILITY_ON);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(android.R.drawable.btn_dialog));

        }

        webview = (WebView) findViewById(R.id.webview);
        websiteUrl = getIntent().getExtras().getString("websiteUrl");
        title = getIntent().getExtras().getString("title");

        ContactUsWebView.this.runOnUiThread(new Runnable()
        {
            public void run()
            {

                webview.getSettings().setPluginState(WebSettings.PluginState.ON);

                webview.setWebViewClient(new MyBrowser());
                webview.setWebChromeClient(new WebChromeClient()
                {
                    public void onProgressChanged(WebView view, int progress)
                    {
                        ContactUsWebView.this.setTitle("Loading...");
                        ContactUsWebView.this.setProgress(progress * 100);
                        if(progress == 100) {
                            ContactUsWebView.this.setTitle(title);
                        }
                    }
                });
                webview.loadUrl(websiteUrl);
                webview.getSettings().setLoadsImagesAutomatically(true);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.getSettings().setLoadWithOverviewMode(true);
                webview.getSettings().setUseWideViewPort(true);
                webview.getSettings().setBuiltInZoomControls(true);
                webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            }
        });
    }

    private class MyBrowser extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            Toast.makeText(ContactUsWebView.this, "Oh no! " + description, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            /*mProgressDialog.dismiss();*/
            super.onPageFinished(view, url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            webview.stopLoading();
            ViewGroup parent = (ViewGroup)webview.getParent();
            parent.removeView(webview);
            webview.destroy();
            finish();
        }
    }
}
