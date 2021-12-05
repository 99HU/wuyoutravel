package juhe;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xingli.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWv = findViewById(R.id.wv);

        mWv.getSettings().setJavaScriptEnabled(true);
        mWv.loadUrl("https://shfb.ibbtv.cn/public/tour/index");
    }
}