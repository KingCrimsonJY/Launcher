package launcher.kcjy.xyz.library;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import launcher.kcjy.xyz.launcher;
import launcher.kcjy.xyz.variable;

public class Tools {



    public static String UrlPost(String ur, String byteString) {
        String str="";
        try {
            URL url=new URL(ur);
            HttpURLConnection HttpURLConnection=(HttpURLConnection) url.openConnection();
            HttpURLConnection.setReadTimeout(9000);
            HttpURLConnection.setRequestMethod("POST");
            OutputStream outputStream = HttpURLConnection.getOutputStream();
            outputStream.write(byteString.getBytes());
            BufferedReader BufferedReader=new BufferedReader(new InputStreamReader(HttpURLConnection.getInputStream()));
            String String="";
            StringBuffer StringBuffer=new StringBuffer();
            while ((String = BufferedReader.readLine()) != null) {
                StringBuffer.append(String);
            }
            str = StringBuffer.toString();
        } catch (IOException e) {}
        return str;
    }

    public static void Applist(Context context, boolean isSystem){
      AlertDialog.Builder appdialog = new AlertDialog.Builder(context);
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

    public static boolean Network(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }
    
    public static void 存储权限(Context context) {
        Activity activity1 = (Activity) context;
        boolean isGranted = true;
        activity1.getWindow().setType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                                      ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                                      : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            if (!isGranted) {
                activity1.requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                        .ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    102);
            }
        }
    }
    public static boolean checkapp(Context context,String pkgname){
if (TextUtils.isEmpty(pkgname)){
    return false;
}
try {
context.getPackageManager().getApplicationInfo(pkgname,0);
}catch (Exception e){
    return false;
}
return true;
    }
    public static boolean startapp(Context context, String packagename) {
		PackageManager packageManager=context.getPackageManager();
		Intent intent=new Intent();
		intent = packageManager.getLaunchIntentForPackage(packagename);
		context.startActivity(intent);
		return true;
	}
    public static boolean uninstallapp(Context context, String packagename) {
        if (TextUtils.isEmpty(packagename)){
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:"+packagename));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }
    
}
