package launcher.kcjy.xyz.App.epub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mertakdut.BookSection;
import com.github.mertakdut.CssStatus;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;

import launcher.kcjy.xyz.library.ActManager;
import launcher.kcjy.xyz.R;
import launcher.kcjy.xyz.library.Tools;

public class MainActivity extends AppCompatActivity implements PageFragment.OnFragmentReadyListener{
    private Reader reader;

    private ViewPager mViewPager;


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private int pageCount = Integer.MAX_VALUE;
    private int pxScreenWidth;

    private boolean isPickedWebView = false;

    private MenuItem searchMenuItem;
    private SearchView searchView;

    private boolean isSkippedToPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     ActManager.addActivity(this);


        pxScreenWidth = getResources().getDisplayMetrics().widthPixels;

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        if (getIntent() != null && getIntent().getExtras() != null) {
            String filePath = getIntent().getExtras().getString("filePath");
            isPickedWebView = getIntent().getExtras().getBoolean("isWebView");

            try {
                reader = new Reader();

                // Setting optionals once per file is enough.
                reader.setMaxContentPerSection(1250);
                reader.setCssStatus(isPickedWebView ? CssStatus.INCLUDE : CssStatus.OMIT);
                reader.setIsIncludingTextContent(true);
                reader.setIsOmittingTitleTag(true);

                // This method must be called before readSection.
                reader.setFullContent(filePath);

//                int lastSavedPage = reader.setFullContentWithProgress(filePath);
                if (reader.isSavedProgressFound()) {
                    int lastSavedPage = reader.loadProgress();
                    mViewPager.setCurrentItem(lastSavedPage);
                }

            } catch (ReadingException e) {
                Tools.showtoast( e.getMessage(),MainActivity.this);
            }
        }
        EditText bookedit = findViewById(R.id.bookedit);
        ImageView booksearch = findViewById(R.id.booksearch);
        booksearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str =bookedit.getText().toString();
                if (str!=null&&!str.equals("")&&!str.equals("0")){
                    mViewPager.setCurrentItem(Integer.valueOf(str));
                    bookedit.setText("");
                }
                else {
                    Tools.showtoast("error number",MainActivity.this);
                    bookedit.setText("");
                }
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                bookedit.setHint("current page: "+mViewPager.getCurrentItem());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public View onFragmentReady(int position) {

        BookSection bookSection = null;

        try {
            bookSection = reader.readSection(position);
        } catch (ReadingException e) {
            e.printStackTrace();
            Tools.showtoast(e.getMessage(),MainActivity.this);
        } catch (OutOfPagesException e) {
            e.printStackTrace();
            this.pageCount = e.getPageCount();

            if (isSkippedToPage) {
                Tools.showtoast("Max page number is: " + this.pageCount,MainActivity.this);
            }

            mSectionsPagerAdapter.notifyDataSetChanged();
        }

        isSkippedToPage = false;

        if (bookSection != null) {
            return setFragmentView(isPickedWebView, bookSection.getSectionContent(), "text/html", "UTF-8"); // reader.isContentStyled
        }

        return null;
    }


    @Override
    protected void onStop() {
        super.onStop();
        try {
            reader.saveProgress(mViewPager.getCurrentItem());
            Tools.showtoast("Saved page: " + mViewPager.getCurrentItem() + "...",MainActivity.this);
        } catch (ReadingException e) {
            e.printStackTrace();
            Tools.showtoast("Progress is not saved: " + e.getMessage(),MainActivity.this);
        } catch (OutOfPagesException e) {
            e.printStackTrace();
            Tools.showtoast("Progress is not saved. Out of Bounds. Page Count: " + e.getPageCount(),MainActivity.this);
        }
    }


    private View setFragmentView(boolean isContentStyled, String data, String mimeType, String encoding) {

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (isContentStyled) {
            WebView webView = new WebView(MainActivity.this);
            webView.loadDataWithBaseURL(null, data, mimeType, encoding, null);

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//            }

            webView.setLayoutParams(layoutParams);

            return webView;
        } else {
            ScrollView scrollView = new ScrollView(MainActivity.this);
            scrollView.setLayoutParams(layoutParams);

            TextView textView = new TextView(MainActivity.this);
            textView.setLayoutParams(layoutParams);

            textView.setText(Html.fromHtml(data, new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    String imageAsStr = source.substring(source.indexOf(";base64,") + 8);
                    byte[] imageAsBytes = Base64.decode(imageAsStr, Base64.DEFAULT);
                    Bitmap imageAsBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

                    int imageWidthStartPx = (pxScreenWidth - imageAsBitmap.getWidth()) / 2;
                    int imageWidthEndPx = pxScreenWidth - imageWidthStartPx;

                    Drawable imageAsDrawable = new BitmapDrawable(getResources(), imageAsBitmap);
                    imageAsDrawable.setBounds(imageWidthStartPx, 0, imageWidthEndPx, imageAsBitmap.getHeight());
                    return imageAsDrawable;
                }
            }, null));

            int pxPadding = dpToPx(12);

            textView.setPadding(pxPadding, pxPadding, pxPadding, pxPadding);

            scrollView.addView(textView);
            return scrollView;
        }
    }

    private void loseFocusOnSearchView() {
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setIconified(true);
        MenuItemCompat.collapseActionView(searchMenuItem);
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return pageCount;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            return PageFragment.newInstance(position);
        }
    }
}
