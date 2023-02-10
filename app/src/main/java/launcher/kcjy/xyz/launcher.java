package launcher.kcjy.xyz;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import launcher.kcjy.xyz.library.NewAppButton;
import launcher.kcjy.xyz.library.Tools;

public class launcher extends AppCompatActivity {
   private Context mContext;
   private EditText edit;


      @Override
    protected void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
         ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            //调用hide()方法将标题栏隐藏起来
            actionbar.hide();
        }
        setContentView(R.layout.activity);
      init();
    }
    @SuppressLint("ResourceType")
    private void init(){
        this.mContext = this;
        
      NewAppButton youdao = new NewAppButton(mContext,R.drawable.youdao,"有道词典",14);
      NewAppButton calculator = new NewAppButton(mContext,R.drawable.calculator,"计算器",14);
      NewAppButton calendar = new NewAppButton(mContext,R.drawable.calendar,"日历",14);
      NewAppButton clock = new NewAppButton(mContext,R.drawable.clock,"时钟",14);
      NewAppButton himalaya = new NewAppButton(mContext,R.drawable.himalaya,"喜马拉雅",14);
      NewAppButton speaking = new NewAppButton(mContext,R.drawable.speaking,"有道口语",14);
      NewAppButton browser = new NewAppButton(mContext,R.drawable.browser,"browser",17);
      NewAppButton player = new NewAppButton(mContext,R.drawable.player,"音乐播放器",17);
      NewAppButton applist = new NewAppButton(mContext,R.drawable.applist,"应用列表",17);
      LinearLayout layout1 = findViewById(R.id.layout1);
      LinearLayout bottomapp = findViewById(R.id.bottomapp);
        layout1.addView(browser);
        layout1.addView(player);
        layout1.addView(applist);
        bottomapp.addView(youdao);
        bottomapp.addView(calculator);
        bottomapp.addView(calendar);
        bottomapp.addView(clock);
        bottomapp.addView(himalaya);
        bottomapp.addView(speaking);
        
        browser.setId(1);
        player.setId(2);
        applist.setId(3);
        
        youdao.setId(1);
        calculator.setId(2);
        calendar.setId(3);
        clock.setId(4);
        himalaya.setId(5);
        speaking.setId(6);
        
        youdao.setOnClickListener(new bottomapponclick());
        calculator.setOnClickListener(new bottomapponclick());
        calendar.setOnClickListener(new bottomapponclick());
        clock.setOnClickListener(new bottomapponclick());
        himalaya.setOnClickListener(new bottomapponclick());
        speaking.setOnClickListener(new bottomapponclick());

        browser.setOnClickListener(new apponclick());
        player.setOnClickListener(new apponclick());
        applist.setOnClickListener(new apponclick());

    }
    
        class bottomapponclick implements View.OnClickListener {
		@Override
		public void onClick(View v) {
            switch(v.getId()){
           case 1://有道词典
             Tools.startapp(mContext,"com.youdao.hardware.dict");
			 break;
	     case 2://计算器
             Tools.startapp(mContext,"com.android.calculator2");
			  break;
		 case 3://日历
             Tools.startapp(mContext,"com.android.calendar");
			 break;
		 case 4://时钟
             Tools.startapp(mContext,"com.android.deskclock");
			 break;
	     case 5://喜马拉雅
             Tools.startapp(mContext,"com.ximalaya.ting.kid");
			 break;
		 case 6://口语
             Tools.startapp(mContext,"com.youdao.crackingenglish");
			 break;
                default:
                break;
                }
            }
        }
    class apponclick implements View.OnClickListener {
		@Override
		public void onClick(View v) {
            switch(v.getId()){
                case 1:
                break;
                case 2:

                break;
                case 3:
                    appdialogcheck();
               break;
                default:
                break;
            }
            }
        }

    class dialogbtnonclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (edit.getText().toString().contains("kingcrimsonjy")) {
                switch (v.getId()) {
                    case 1:
Tools.Applist(mContext,false);
                        break;
                    case 2:
Tools.Applist(mContext,true);
                        break;
                    default:
                        break;
                }
            }
            else {
                Toast.makeText(mContext,"错误",Toast.LENGTH_SHORT).show();
            }
            edit.setText("");
            }
        }

        @SuppressLint("ResourceType")
        private void appdialogcheck(){
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            edit = new EditText(mContext);
            edit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            edit.setTransformationMethod(PasswordTransformationMethod.getInstance());

            MaterialButton user = new MaterialButton(mContext);
            user.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            user.setText("用户应用");

            MaterialButton system = new MaterialButton(mContext);
            system.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            system.setText("系统应用");

            layout.addView(edit);
            layout.addView(user);
            layout.addView(system);
            user.setId(1);
            system.setId(2);
            user.setOnClickListener(new dialogbtnonclick());
            system.setOnClickListener(new dialogbtnonclick());

            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setCancelable(true);
            dialog.setView(layout);
            dialog.create().show();
        }

}
