package lightningchart.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ChartDisplayActivity extends AppCompatActivity {
    private WebView webView;
    private Boolean safeBrowsingIsInitialized = false;

    /**
     * creates a webView and displays the chart on this webView
     *
     * @param savedInstanceState Bundle that contains the params passed from previous activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chart_display);
        final Bundle bundle = getIntent().getExtras();

        webView = findViewById(R.id.chartDisplayWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (bundle.getBoolean("useRandom")) {
                    // Timer pushing new data on interval
                    Timer t = new Timer();
                    t.scheduleAtFixedRate(new DataTask(view), 0, 1000);
                } else {
                    final String xyArray = bundle.getString("xyValues");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        view.evaluateJavascript("setData('" + xyArray + "')", null);
                    } else {
                        view.loadUrl("javascript:setData('" + xyArray + "')");
                    }
                }
            }
        });
        webView.loadUrl("file:///android_asset/index.html");
    }
}

class DataTask extends TimerTask {
    private WebView view;
    private Random random;

    DataTask(WebView view) {
        this.view = view;
        this.random = new Random();
    }

    @Override
    public void run() {
        // Generate random float to show
        float f = random.nextFloat();
        final String data = Float.toString(f);
        // send the generated data to the WebView
        // method for sending depends on Android version
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.evaluateJavascript("addData('" + data + "')", null);
                }
            });
        } else {
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.loadUrl("javascript:addData('" + data + "')");
                }
            });
        }
    }
}