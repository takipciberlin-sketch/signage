package com.screenmagic.player;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
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
        shopInput.setHintTextColor(0xFF666
