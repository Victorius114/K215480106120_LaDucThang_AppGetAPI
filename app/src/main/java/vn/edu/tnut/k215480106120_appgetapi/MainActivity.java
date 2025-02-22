package vn.edu.tnut.k215480106120_appgetapi;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Tìm WebView và thiết lập cài đặt
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Bật JavaScript nếu cần

        // Để WebView mở link trong app thay vì trình duyệt
        webView.setWebViewClient(new WebViewClient());

        // Load trang HTML bạn đã tạo
        webView.loadUrl("file:///android_asset/index.html");
    }
}
