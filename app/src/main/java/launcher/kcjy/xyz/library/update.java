package launcher.kcjy.xyz.library;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import launcher.kcjy.xyz.BuildConfig;
import launcher.kcjy.xyz.variable;


public class update {
private Context context;
private ProgressDialog progressdialog;
private String downloadlink;
private int progress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    public String state;
    public update(Context mcontext){
    this.context = mcontext;
}
    public String checkupdate(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
        if (Tools.Network(context)) {
            try {
                String content = Tools.UrlPost(variable.url, "");
                JSONObject jsonObject = new JSONObject(content);
                int version = jsonObject.getInt("version");
                boolean usestate = jsonObject.getBoolean("usestate");
                downloadlink = jsonObject.getString("downloadlink");
                if (!usestate) {
                    state = "unusable";
                } else if (variable.nowversion != version) {
                    state = "update";
                } else {
                    state = "normal";
                }

            } catch (Exception e) {
            }
        }
        else {
            state = "unavailable";
        }

            }
        });
        thread.start();
        while (thread.isAlive()){}
        return state;
    }
public void updatedialog(){
    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
    dialog.setCancelable(true);
    dialog.setTitle("Update");
    dialog.setMessage("pleas download");
    dialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            downloaddialog();
        }
    });
    dialog.create().show();
}
private void downloaddialog(){
        download();
    progressdialog = new ProgressDialog(context);
    progressdialog.setCancelable(false);
    progressdialog.setCanceledOnTouchOutside(false);
    progressdialog.setTitle("Downloading");
    progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressdialog.show();
    progressdialog.setMax(100);

}
private void download(){
    Thread downloadthread = new Thread(downapkrunnable);
    downloadthread.start();
}
private Runnable downapkrunnable = new Runnable() {
    @Override
    public void run() {
        URL url;
        try {
          url = new URL(downloadlink);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            con.connect();
            int length = con.getContentLength();
            InputStream ins = con.getInputStream();
            File file = new File(variable.path);
            if (!file.exists()){
                file.mkdir();
            }
            File apkFile = new File(variable.filename);
            FileOutputStream fos = new FileOutputStream(apkFile);
            int count = 0;
            byte[] buf = new byte[1024];
            while (true) {
                int numread = ins.read(buf);
                count += numread;
                progress = (int) (((float) count / length) * 100);
// 下载进度
                mHandler.sendEmptyMessage(DOWN_UPDATE);
                if (numread <= 0) {
// 下载完成通知安装
                    mHandler.sendEmptyMessage(DOWN_OVER);
                    break;
                }
                fos.write(buf, 0, numread);
            }
            fos.close();
            ins.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
};
    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    progressdialog.setProgress(progress);
                    break;
                case DOWN_OVER:
                installApp(variable.filename);
                    break;
                default:
                    break;
            }
        }
    };

    private void installApp(String apkPath) {
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(context, "launcher.kcjy.xyz.fileprovider", file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
    }
}
