package launcher.kcjy.xyz.App.epub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.ReadingException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import launcher.kcjy.xyz.library.ActManager;
import launcher.kcjy.xyz.R;

public class MenuActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ActManager.addActivity(this);
        ((GridView) findViewById(R.id.grid_book_info)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clickedItemFilePath = ((BookInfo) adapterView.getAdapter().getItem(i)).getFilePath();
                askForWidgetToUse(clickedItemFilePath);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        new ListBookInfoTask().execute();
    }



    private class ListBookInfoTask extends AsyncTask<Object, Object, List<BookInfo>> {

        private Exception occuredException;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<BookInfo> doInBackground(Object... params) {
            List<BookInfo> bookInfoList = searchForPdfFiles();

            Reader reader = new Reader();
            for (BookInfo bookInfo : bookInfoList) {
                try {
                    reader.setInfoContent(bookInfo.getFilePath());

                    String title = reader.getInfoPackage().getMetadata().getTitle();
                    if (title != null && !title.equals("")) {
                        bookInfo.setTitle(reader.getInfoPackage().getMetadata().getTitle());
                    } else { // If title doesn't exist, use fileName instead.
                        int dotIndex = bookInfo.getTitle().lastIndexOf('.');
                        bookInfo.setTitle(bookInfo.getTitle().substring(0, dotIndex));
                    }

                    bookInfo.setCoverImage(reader.getCoverImage());
                } catch (ReadingException e) {
                    occuredException = e;
                    e.printStackTrace();
                }
            }

            return bookInfoList;
        }

        @Override
        protected void onPostExecute(List<BookInfo> bookInfoList) {
            super.onPostExecute(bookInfoList);
            progressBar.setVisibility(View.GONE);

            if (bookInfoList != null) {
                BookInfoGridAdapter adapter = new BookInfoGridAdapter(MenuActivity.this, bookInfoList);
                ((GridView) findViewById(R.id.grid_book_info)).setAdapter(adapter);
            }

            if (occuredException != null) {
                Toast.makeText(MenuActivity.this, occuredException.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private List<BookInfo> searchForPdfFiles() {
        boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        List<BookInfo> bookInfoList = null;

        if (isSDPresent) {
            bookInfoList = new ArrayList<>();

            List<File> files = getListFiles(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
            File file1 = getFileFromAssets("1.epub");
            File file2 = getFileFromAssets("2.epub");
            File file3 = getFileFromAssets("3.epub");
            File file4 = getFileFromAssets("4.epub");
            File file5 = getFileFromAssets("5.epub");
            File file6 = getFileFromAssets("6.epub");
            File file7 = getFileFromAssets("7.epub");
            File file8 = getFileFromAssets("8.epub");
            File file9 = getFileFromAssets("9.epub");
            File file10 = getFileFromAssets("10.epub");
            File file11 = getFileFromAssets("11.epub");
            File file12 = getFileFromAssets("12.epub");
            File file13 = getFileFromAssets("13.epub");
            File file14 = getFileFromAssets("14.epub");
            File file15 = getFileFromAssets("15.epub");
            File file16 = getFileFromAssets("16.epub");
            files.add(0,file1);
            files.add(0,file2);
            files.add(0,file3);
            files.add(0,file4);
            files.add(0,file5);
            files.add(0,file6);
            files.add(0,file7);
            files.add(0,file8);
            files.add(0,file9);
            files.add(0,file10);
            files.add(0,file11);
            files.add(0,file12);
            files.add(0,file13);
            files.add(0,file14);
            files.add(0,file15);
            files.add(0,file16);


            for (File file : files) {
                BookInfo bookInfo = new BookInfo();

                bookInfo.setTitle(file.getName());
                bookInfo.setFilePath(file.getPath());

                bookInfoList.add(bookInfo);
            }
        }

        return bookInfoList;
    }

    public File getFileFromAssets(String fileName) {

        File file = new File(getCacheDir() + "/" + fileName);

        if (!file.exists()) try {

            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    inFiles.addAll(getListFiles(file));
                } else {
                    if (file.getName().endsWith(".epub")) {
                        inFiles.add(file);
                    }
                }
            }
        }
        return inFiles;
    }

    private void askForWidgetToUse(final String filePath) {

        final Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.putExtra("filePath", filePath);

        new AlertDialog.Builder(MenuActivity.this)
                .setTitle("Pick your widget")
                .setMessage("Textview or WebView?")
                .setPositiveButton("TextView", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("isWebView", false);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("WebView", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("isWebView", true);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
