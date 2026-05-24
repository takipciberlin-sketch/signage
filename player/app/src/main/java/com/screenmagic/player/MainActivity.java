package com.screenmagic.player;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.text.InputType;

public class MainActivity extends Activity {

    private WebView webView;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ekranı her zaman açık tut
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        prefs = getSharedPreferences("screenmagic", MODE_PRIVATE);
        String shopId = prefs.getString("shopId", "");
        String screenId = prefs.getString("screenId", "");

        if (!shopId.isEmpty() && !screenId.isEmpty()) {
            showPlayer(shopId, screenId);
        } else {
            showSetup();
        }
    }

    void showSetup() {
        // Tam ekran
        hideSystemUI();

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(0xFF0a0a0a);
        scroll.setFillViewport(true);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 80, 60, 60);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        // Logo / Başlık
        TextView logo = new TextView(this);
        logo.setText("📺 ScreenMagic Player");
        logo.setTextSize(28);
        logo.setTextColor(0xFF00e676);
        logo.setGravity(Gravity.CENTER);
        logo.setPadding(0, 0, 0, 10);
        layout.addView(logo);

        TextView version = new TextView(this);
        version.setText("v2.0 — Launcher Mode");
        version.setTextSize(13);
        version.setTextColor(0xFF666666);
        version.setGravity(Gravity.CENTER);
        version.setPadding(0, 0, 0, 50);
        layout.addView(version);

        // Shop ID
        TextView label1 = new TextView(this);
        label1.setText("Shop ID");
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
        shopInput.setHint("shop_abc123");
        shopInput.setTextColor(0xFFffffff);
        shopInput.setHintTextColor(0xFF555555);
        shopInput.setBackgroundColor(0xFF1a1a1a);
        shopInput.setPadding(20, 16, 20, 16);
        shopInput.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p1.setMargins(0, 0, 0, 30);
        layout.addView(shopInput, p1);

        // Screen ID
        TextView label2 = new TextView(this);
        label2.setText("Screen ID");
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
        screenInput.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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

        // Ayar sıfırla bilgisi
        TextView resetInfo = new TextView(this);
        resetInfo.setText("\n\nAyarları sıfırlamak için:\nAyarlar → Uygulamalar → ScreenMagic → Veriyi Sil");
        resetInfo.setTextSize(11);
        resetInfo.setTextColor(0xFF444444);
        resetInfo.setGravity(Gravity.CENTER);
        layout.addView(resetInfo);

        scroll.addView(layout);
        setContentView(scroll);
    }

    void showPlayer(String shopId, String screenId) {
        hideSystemUI();

        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Bağlantı hatası — 10 saniye sonra tekrar dene
                view.postDelayed(() -> view.reload(), 10000);
            }
        });

        String url = "https://takipciberlin-sketch.github.io/signage/signage_screen_v5.html"
            + "?shop=" + shopId + "&screen=" + screenId;
        webView.loadUrl(url);
        setContentView(webView);
    }

    void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= 30) {
            getWindow().setDecorFitsSystemWindows(false);
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    public void onBackPressed() {
        // Back tuşunu devre dışı bırak — kiosk modu
        // Ayarlara dönmek için: Uygulamalar → Veriyi Sil
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }
}
