package com.screenmagic.player;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    WebView webView;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("config", MODE_PRIVATE);

        String shopId = prefs.getString("shopId", "");
        String screenId = prefs.getString("screenId", "");

        if (shopId.isEmpty() || screenId.isEmpty()) {
            showSetupScreen();
        } else {
            showPlayer(shopId, screenId);
        }
    }

    void showSetupScreen() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 100, 60, 60);
        layout.setBackgroundColor(0xFF0a0a0a);

        android.widget.TextView title = new android.widget.TextView(this);
        title.setText("ScreenMagic Player");
        title.setTextSize(28);
        title.setTextColor(0xFF00e676);
        title.setPadding(0, 0, 0, 40);
        layout.addView(title);

        android.widget.TextView label1 = new android.widget.TextView(this);
        label1.setText("Shop ID:");
        label1.setTextColor(0xFFffffff);
        label1.setTextSize(16);
        layout.addView(label1);

        EditText shopInput = new EditText(this);
        shopInput.setHint("shop_1234567890");
        shopInput.setTextColor(0xFFffffff);
        shopInput.setHintTextColor(0xFF666666);
        shopInput.setBackgroundColor(0xFF1a1a1a);
        shopInput.setPadding(20, 16, 20, 16);
        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(-1, -2);
        p1.setMargins(0, 8, 0, 24);
        layout.addView(shopInput, p1);

        android.widget.TextView label2 = new android.widget.TextView(this);
        label2.setText("Screen ID:");
        label2.setTextColor(0xFFffffff);
        label2.setTextSize(16);
        layout.addView(label2);

        EditText screenInput = new EditText(this);
        screenInput.setHint("screen_1");
        screenInput.setTextColor(0xFFffffff);
        screenInput.setHintTextColor(0xFF666666);
        screenInput.setBackgroundColor(0xFF1a1a1a);
        screenInput.setPadding(20, 16, 20, 16);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(-1, -2);
        p2.setMargins(0, 8, 0, 40);
        layout.addView(screenInput, p2);

        Button btn = new Button(this);
        btn.setText("BAŞLAT");
        btn.setBackgroundColor(0xFF00e676);
        btn.setTextColor(0xFF000000);
        btn.setTextSize(18);
        layout.addView(btn);

        btn.setOnClickListener(v -> {
            String shop = shopInput.getText().toString().trim();
            String screen = screenInput.getText().toString().trim();
            if (!shop.isEmpty() && !screen.isEmpty()) {
                prefs.edit().putString("shopId", shop).putString("screenId", screen).apply();
                showPlayer(shop, screen);
            }
        });

        setContentView(layout);
    }

    void showPlayer(String shopId, String screenId) {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebViewClient(new WebViewClient());

        String url = "https://takipciberlin-sketch.github.io/signage/signage_screen_v4.html"
            + "?shop=" + shopId + "&screen=" + screenId;
        webView.loadUrl(url);
        setContentView(webView);
    }
}
