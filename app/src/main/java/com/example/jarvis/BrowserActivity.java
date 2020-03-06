package com.example.jarvis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class BrowserActivity extends AppCompatActivity {
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Intent intent = getIntent();
        String token = intent.getStringExtra(MainActivity.USER_TOKEN);
        webview = findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());

        if (token.indexOf(".com") != -1)
        {
            String url = "www."+token;
            webview.loadUrl(url);
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }
        else
        {
            String url = "https://www.google.com/search?q="+token;
            webview.loadUrl(url);
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }

    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack())
        {
            webview.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
