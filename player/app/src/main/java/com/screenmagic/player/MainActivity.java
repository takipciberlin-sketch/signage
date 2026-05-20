package com.screenmagic.player;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.Intent;
import android.provider.Settings;

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
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(0xFF0a0a0a);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 80, 60, 60);

        // Başlık
        TextView title = new TextView(this);
        title.setText("🖥️ ScreenMagic Player");
        title.setTextSize(26);
        title.setTextColor(0xFF00e676);
        title.setPadding(0, 0, 0, 8);
        layout.addView(title);

        TextView sub = new TextView(this);
        sub.setText("Kurulum Sihirbazı");
        sub.setTextSize(14);
        sub.setTextColor(0xFF888888);
        sub.setPadding(0, 0, 0, 40);
        layout.addView(sub);

        // WiFi butonu
        TextView wifiTitle = new TextView(this);
        wifiTitle.setText("1. WiFi Bağlantısı");
        wifiTitle.setTextSize(16);
        wifiTitle.setTextColor(0xFF00e676);
        wifiTitle.setPadding(0, 0, 0, 8);
        layout.addView(wifiTitle);

        TextView wifiSub = new TextView(this);
        wifiSub.setText("WiFi ayarlarını açıp bağlantınızı seçin");
        wifiSub.setTextSize(13);
        wifiSub.setTextColor(0xFF888888);
        wifiSub.setPadding(0, 0, 0, 12);
        layout.addView(wifiSub);

        Button wifiBtn = new Button(this);
        wifiBtn.setText("📶  WiFi Ayarlarını Aç");
        wifiBtn.setBackgroundColor(0xFF1a3a2a);
        wifiBtn.setTextColor(0xFF00e676);
        wifiBtn.setTextSize(15);
        LinearLayout.LayoutParams wifiP = new LinearLayout.LayoutParams(-1, -2);
        wifiP.setMargins(0, 0, 0, 40);
        layout.addView(wifiBtn, wifiP);

        wifiBtn.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        });

        // Ayraç
        View divider = new View(this);
        divider.setBackgroundColor(0xFF222222);
        LinearLayout.LayoutParams divP = new LinearLayout.LayoutParams(-1, 1);
        divP.setMargins(0, 0, 0, 40);
        layout.addView(divider, divP);

        // Shop ID
        TextView label1 = new TextView(this);
        label1.setText("2. Shop ID");
        label1.setTextSize(16);
        label1.setTextColor(0xFF00e676);
        label1.setPadding(0, 0, 0, 8);
        layout.addView(label1);

        TextView shopSub = new TextView(this);
        shopSub.setText("Admin panelden kopyalayın");
        shopSub.setTextSize(13);
        shopSub.setTextColor(0xFF888888);
        shopSub.setPadding(0, 0, 0, 10);
        layout.addView(shopSub);

        EditText shopInput = new EditText(this);
        shopInput.setHint("shop_1234567890");
        shopInput.setTextColor(0xFFffffff);
        shopInput.setHintTextColor(0xFF555555);
        shopInput.setBackgroundColor(0xFF1a1a1a);
        shopInput.setPadding(20, 16, 20, 16);
        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(-1, -2);
        p1.setMargins(0, 0, 0, 30);
        layout.addView(shopInput, p1);

        // Screen ID
        TextView label2 = new TextView(this);
        label2.setText("3. Screen ID");
        label2.setTextSize(16);
        label2.setTextColor(0xFF00e676);
        label2.setPadding(0, 0, 0, 8);
        layout.addView(label2);

        TextView screenSub = new TextView(this);
        screenSub.setText("Admin panelden kopyalayın");
        screenSub.setTextSize(13);
        screenSub.setTextColor(0xFF888888);
        screenSub.setPadding(0, 0, 0, 10);
        layout.addView(screenSub);

        EditText screenInput = new EditText(this);
        screenInput.setHint("screen_1");
        screenInput.setTextColor(0xFFffffff);
        screenInput.setHintTextColor(0xFF555555);
        screenInput.setBackgroundColor(0xFF1a1a1a);
        screenInput.setPadding(20, 16, 20, 16);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(-1, -2);
        p2.setMargins(0, 0, 0, 40);
        layout.addView(screenInput, p2);

        // Başlat butonu
        Button btn = new Button(this);
        btn.setText("🚀  BAŞLAT");
        btn.setBackgroundColor(0xFF00e676);
        btn.setTextColor(0xFF000000);
        btn.setTextSize(18);
        layout.addView(btn);

        btn.setOnClickListener(v -> {
            String shop = shopInput.getText().toString().trim();
            String screen = screenInput.getText().toString().trim();
            if (!shop.isEmpty() && !screen.isEmpty()) {
                prefs.edit()
                    .putString("shopId", shop)
                    .putString("screenId", screen)
                    .apply();
                showPlayer(shop, screen);
            }
        });

        scroll.addView(layout);
        setContentView(scroll);
    }

    void showPlayer(String shopId, String screenId) {
        getWindow().setDecorFitsSystemWindows(false);
        WindowInsetsController controller = getWindow().getInsetsController();
        if (controller != null) {
            controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            controller.setSystemBarsBehavior(
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        }

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
