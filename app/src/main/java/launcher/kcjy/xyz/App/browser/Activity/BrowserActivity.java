package launcher.kcjy.xyz.App.browser.Activity;

import static android.view.KeyEvent.KEYCODE_BACK;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;

import launcher.kcjy.xyz.R;
import launcher.kcjy.xyz.library.ActManager;
import launcher.kcjy.xyz.variable;

public class BrowserActivity extends AppCompatActivity {
private AgentWeb mAgentWeb;
private EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);
        ActManager.addActivity(this);
        init();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (mAgentWeb.getWebCreator().getWebView().getUrl().equals(variable.browserurl))ActManager.finishAll();
            else mAgentWeb.back();
        }
        return true;
    }
private void init(){
    LinearLayout web = findViewById(R.id.web);
    edit = findViewById(R.id.edit);
    ImageView search = findViewById(R.id.search);
    ImageView home = findViewById(R.id.home);
    ImageView refresh = findViewById(R.id.refresh);
    ImageView before = findViewById(R.id.before);
    ImageView next = findViewById(R.id.next);
    search.setOnClickListener(new onclick());
    home.setOnClickListener(new onclick());
    refresh.setOnClickListener(new onclick());
    before.setOnClickListener(new onclick());
    next.setOnClickListener(new onclick());
    mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(web, new LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    edit.setText(url);
                }
            })
            .createAgentWeb()
            .ready()
            .go(variable.browserurl);
    WebSettings webSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
    webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
    webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
    webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
    webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
    webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
}
private class onclick implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search:
                mAgentWeb.getUrlLoader().loadUrl(edit.getText().toString());
                break;
            case R.id.home:
                mAgentWeb.getUrlLoader().loadUrl(variable.browserurl);
                break;
            case R.id.refresh:
                mAgentWeb.getUrlLoader().loadUrl(mAgentWeb.getWebCreator().getWebView().getUrl());
                break;
            case R.id.before:
                mAgentWeb.getWebCreator().getWebView().goBack();
                break;
            case R.id.next:
                mAgentWeb.getWebCreator().getWebView().goForward();
                break;
        }
    }
}

}