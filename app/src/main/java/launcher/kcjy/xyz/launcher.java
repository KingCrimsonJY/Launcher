package launcher.kcjy.xyz;
import static launcher.kcjy.xyz.library.Tools.startapp;
import static launcher.kcjy.xyz.library.Tools.uninstallapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import launcher.kcjy.xyz.App.browser.Activity.BrowserActivity;
import launcher.kcjy.xyz.App.epub.MenuActivity;
import launcher.kcjy.xyz.library.ActManager;
import launcher.kcjy.xyz.library.NewAppButton;
import launcher.kcjy.xyz.library.Tools;
import launcher.kcjy.xyz.library.update;

public class launcher extends AppCompatActivity {
   public static Context mContext;
   private EditText edit;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
         ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            //调用hide()方法将标题栏隐藏起来
            actionbar.hide();
        }
        setContentView(R.layout.launcher);
      init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActManager.finishAll();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
          if (keyCode==KeyEvent.KEYCODE_BACK)return true;
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("ResourceType")
    private void init(){
        this.mContext = this;
        
      NewAppButton youdao = new NewAppButton(mContext,R.drawable.youdao,"有道词典",14);
      NewAppButton calculator = new NewAppButton(mContext,R.drawable.calculator,"计算器",14);
      NewAppButton calendar = new NewAppButton(mContext,R.drawable.calendar,"日历",14);
      NewAppButton clock = new NewAppButton(mContext, R.drawable.clock,"时钟",14);
      NewAppButton himalaya = new NewAppButton(mContext,R.drawable.himalaya,"喜马拉雅",14);
      NewAppButton speaking = new NewAppButton(mContext,R.drawable.speaking,"有道口语",14);
      NewAppButton applist = new NewAppButton(mContext,R.drawable.applist,"应用列表",17);
      NewAppButton mryytl = new NewAppButton(mContext,R.drawable.epub,"Epub阅读器",17);
      NewAppButton browser = new NewAppButton(mContext,R.drawable.browser,"浏览器",17);

      LinearLayout layout1 = findViewById(R.id.layout1);
      LinearLayout bottomapp = findViewById(R.id.bottomapp);
        layout1.addView(applist);
        layout1.addView(mryytl);
        layout1.addView(browser);

        bottomapp.addView(youdao);
        bottomapp.addView(calculator);
        bottomapp.addView(calendar);
        bottomapp.addView(clock);
        bottomapp.addView(himalaya);
        bottomapp.addView(speaking);

        applist.setId(1);
        mryytl.setId(2);
        browser.setId(3);

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

        applist.setOnClickListener(new apponclick());
        mryytl.setOnClickListener(new apponclick());
        browser.setOnClickListener(new apponclick());

getReadPermissions();
Intent intent = new Intent(mContext,checktask.class);
startService(intent);
    }
    
        class bottomapponclick implements View.OnClickListener {
		@Override
		public void onClick(View v) {
            switch(v.getId()){
           case 1://有道词典
             Tools.showtoast("请点击实体键盘中部红色圆键以打开",mContext);
			 break;
	     case 2://计算器
             startapp(mContext,"com.android.calculator2");
			  break;
		 case 3://日历
             startapp(mContext,"com.android.calendar");
			 break;
		 case 4://时钟
             startapp(mContext,"com.android.deskclock");
			 break;
	     case 5://喜马拉雅
             startapp(mContext,"com.ximalaya.ting.kid");
			 break;
		 case 6://口语
             startapp(mContext,"com.youdao.crackingenglish");
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
                    appdialogcheck();
                    break;
                case 2:

/*String str = check();
if (Tools.checkapp(mContext,"com.eusoft.ting.en")) {
    if (str.equals("normal")) {
        Tools.startapp(mContext, "com.eusoft.ting.en");
    } else {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses("com.eusoft.ting.en");
    }
}
else {
  Tools.showtoast("App not installed",mContext);
}*/
                    if (check().equals("normal")||check().equals("unavailable")) startActivity(new Intent(mContext, MenuActivity.class));
                break;
                case 3:
                    if (check().equals("normal")) startActivity(new Intent(mContext, BrowserActivity.class));
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
Applist(mContext,false);
                        break;
                    case 2:
Applist(mContext,true);
                        break;
                    default:
                        break;
                }
            }
            else {
                Tools.showtoast("error",mContext);
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

        private String check(){
            update up = new update(mContext);
          Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    up.checkstate();
                }
            });
          thread.start();
            while (thread.isAlive()){}
            String str = up.getState();
            if (str.equals("unusable")) Tools.showtoast("不可用",mContext);
            if (str.equals("unavailable"))Tools.showtoast("未连接网络",mContext);
            if (str.equals("networkerror"))Tools.showtoast("网络连接错误，请尝试重新链接网络",mContext);
            if (str.equals("update"))up.updatedialog();
            return str;
        }
    public static void Applist(Context context, boolean isSystem){
        android.app.AlertDialog.Builder appdialog = new android.app.AlertDialog.Builder(context);
        appdialog.setTitle("应用列表");
        appdialog.setCancelable(false);
        appdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ScrollView listlayout = new ScrollView(context);
        listlayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout mainlayout = new LinearLayout(context);
        mainlayout.setOrientation(LinearLayout.VERTICAL);
        mainlayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        listlayout.addView(mainlayout);
        appdialog.setView(listlayout);
        PackageManager pkgmanager = context.getPackageManager();
        List<PackageInfo> list = pkgmanager.getInstalledPackages(0);
        for (PackageInfo p : list){
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));

            ImageView appimage = new ImageView(context);
            appimage.setLayoutParams(new LinearLayout.LayoutParams(100,ViewGroup.LayoutParams.MATCH_PARENT));

            LinearLayout sublayout = new LinearLayout(context);
            sublayout.setOrientation(LinearLayout.VERTICAL);
            sublayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            sublayout.setGravity(Gravity.CENTER);

            MaterialTextView appname = new MaterialTextView(context);
            appname.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            appname.setTextSize(18);
            appname.setTypeface(null, Typeface.BOLD);

            MaterialTextView pkgname = new MaterialTextView(context);
            pkgname.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            pkgname.setTextSize(13);

            layout.addView(appimage);
            layout.addView(sublayout);
            sublayout.addView(appname);
            sublayout.addView(pkgname);

            appimage.setImageDrawable(p.applicationInfo.loadIcon(pkgmanager));
            appname.setText(pkgmanager.getApplicationLabel(p.applicationInfo).toString());
            pkgname.setText(p.applicationInfo.packageName);
            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    uninstallapp(context,pkgname.getText().toString());
                    return false;
                }
            });
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startapp(context,pkgname.getText().toString());
                }
            });
            int flags = p.applicationInfo.flags;
            if (isSystem){
                mainlayout.addView(layout);
            }
            else {
                if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                    mainlayout.addView(layout);
                }
            }
        }
        appdialog.create().show();
    }
    private void getReadPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, 10001);
                } else {//没有则请求获取权限，示例权限是：存储权限，需要其他权限请更改或者替换
                    ActivityCompat.requestPermissions(this,
                            new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10001);
                }
            } else {//如果已经获取到了权限则直接进行下一步操作
                Log.e(null, "全部权限已经授权成功");
            }
        }

    }

    /**
     * 一个或多个权限请求结果回调
     * 循环回调获取权限，除非勾选禁止后不再询问，之后提示用户引导用户去设置
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 10001:
                for (int i = 0; i < grantResults.length; i++) {
//                   如果拒绝获取权限
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean flag = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                        if (flag) {
                            getReadPermissions();
                            return;//用户权限是一个一个的请求的，只要有拒绝，剩下的请求就可以停止，再次请求打开权限了
                        } else { // 勾选不再询问，并拒绝
                             Tools.showtoast("请到设置中打开权限",mContext);
                            return;
                        }
                    }
                }
                //Toast.makeText(this, "权限开启完成", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

    }

}
