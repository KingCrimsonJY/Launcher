package launcher.kcjy.xyz.library;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

import org.json.JSONObject;

import java.io.File;
import java.util.logging.Logger;

import launcher.kcjy.xyz.BuildConfig;
import launcher.kcjy.xyz.library.Tools;
import launcher.kcjy.xyz.launcher;
import launcher.kcjy.xyz.variable;

public class update {
    public String path = "/storage/emulated/0/Android/data/launcher.kcjy.xyz/files/1.apk";
    public String downloadlink;   //下载链接


    public void update(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String state;
                try{
                    String content = Tools.UrlPost(variable.url,"");
                        JSONObject jsonObject = new JSONObject(content);
                        String version = jsonObject.getString("version");
                        boolean usestate = jsonObject.getBoolean("usestate");
                        downloadlink = jsonObject.getString("downloadlink");
                        if (!usestate) {
                            state = "unusable";
                        } else if (!variable.nowversion.equals(version)) {
                            state = "update";
                    }
                        else {
                            state = "normal";
                        }

                }catch (Exception e) {}
            }
        }).start();
    }


    private void installApp(String apkPath,Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //版本在7.0以上是不能直接通过uri访问的
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                File file = (new File(apkPath));
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                        "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            //必须加入，不然不会显示安装成功界面
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
