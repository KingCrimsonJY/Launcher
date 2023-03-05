package launcher.kcjy.xyz.App.browser.Activity;

import static android.view.KeyEvent.KEYCODE_BACK;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import launcher.kcjy.xyz.R;
import launcher.kcjy.xyz.library.ActManager;
import launcher.kcjy.xyz.variable;

public class BrowserActivity extends AppCompatActivity {
private AgentWeb mAgentWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);
        LinearLayout web = findViewById(R.id.web);
        ActManager.addActivity(this);
       mAgentWeb.with(this)
                .setAgentWebParent(web, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(variable.browserurl);

    }


}