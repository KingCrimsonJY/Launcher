package launcher.kcjy.xyz.library;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

import java.util.List;

public class Tools {
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

          /*  image.setIcon(p.applicationInfo.loadIcon(pkgmanager));
            name.setLabel(pkgmanager.getApplicationLabel(p.applicationInfo).toString());
            pkgname.setPackage_name(p.applicationInfo.packageName);*/
            int flags = p.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0&&isSystem){}
            else{
               // Appinfo.add(bean);
            }
        }

    }

    public  static void Network(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(50);//检测到无网络状态延迟退出
                            System.exit(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            return;
        }
        if (info.isRoaming()) {
            return;
        }
        return;
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
    
    public static boolean startapp(Context context, String packagename) {
		PackageManager packageManager=context.getPackageManager();
		Intent intent=new Intent();
		intent = packageManager.getLaunchIntentForPackage(packagename);
		context.startActivity(intent);
		return true;
	}
    
}
