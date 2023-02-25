package launcher.kcjy.xyz.library;

import static android.app.Activity.RESULT_CANCELED;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import launcher.kcjy.xyz.BuildConfig;
import launcher.kcjy.xyz.variable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
public String getState(){
        return state;
}
    public void checkupdate(){
        try {
                    String content = Tools.UrlPost(variable.url, "");
                    JSONObject jsonObject = new JSONObject(content);
                    int version = jsonObject.getInt("version");
                    boolean usestate = jsonObject.getBoolean("usestate");
                    downloadlink = jsonObject.getString("downloadlink");
                  if (usestate){
                      state = "unusable";
                  }
                    else if (variable.nowversion != version) {
                       state = "update";
            }
                    else {
                        state = "normal";
                  }

                }catch (Exception e) {}
    }
private void updatedialog(){
    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
    dialog.setCancelable(false);
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
