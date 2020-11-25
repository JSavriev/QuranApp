package sj.quran.app.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import sj.quran.app.R;
import sj.quran.app.data.Pages;
import sj.quran.app.db.DBHelper;
import sj.quran.app.helper.BookmarkListener;
import sj.quran.app.ui.adapter.SlideQuranPageAdapter;
import sj.quran.app.util.Constants;
import sj.quran.app.util.QuranInfo;
import sj.quran.app.util.QuranSettings;
import sj.quran.app.util.Utils;

public class QuranActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textPageNumber;
    private TextView textSuraTitle;
    private TextView textTitle;
    private TextView textSubtitle;
    private ViewPager viewPager;
    private SeekBar seekBar;
    private View viewShadowBottom;
    private View viewPage;
    private AppBarLayout appBarLayout;
    private ImageButton buttonBookmark;
    private RelativeLayout layoutDetails;
    private PopupWindow popupWindow;

    private static BookmarkListener bookmarkListener;

    public static final int MSG_HIDE_ACTIONBAR = 1;
    private static final long DEFAULT_HIDE_AFTER_TIME = 1000;

    private int page = 0;
    private boolean isActionBarHidden = false;

    private final PagerHandler handler = new PagerHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quran);
        bindingViews();
        setViewPager();
        setPopUpWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager.setOnSystemUiVisibilityChangeListener(null);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                super.onBackPressed();
                break;
            case R.id.buttonBookmark:
                saveOrDeleteBookmarkPage(page);
                break;
            case R.id.buttonMore:
                popupWindow.showAsDropDown(v, 0, -36);
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            handler.sendEmptyMessageDelayed(MSG_HIDE_ACTIONBAR, DEFAULT_HIDE_AFTER_TIME);
        } else {
            handler.removeMessages(MSG_HIDE_ACTIONBAR);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean navigate = QuranSettings.get(this).getVolumeKey();

        if (navigate && keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            return true;
        } else if (navigate && keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void bindingViews() {
        page = Objects.requireNonNull(getIntent().getExtras()).getInt(Constants.PREF_LAST_PAGE, 0);

        viewPager = findViewById(R.id.viewPager);
        buttonBookmark = findViewById(R.id.buttonBookmark);
        viewPage = findViewById(R.id.viewPage);
        viewShadowBottom = findViewById(R.id.viewShadowBottom);
        appBarLayout = findViewById(R.id.appBarLayout);
        textPageNumber = findViewById(R.id.textPageNumber);
        textSuraTitle = findViewById(R.id.textSuraTitle);
        textTitle = findViewById(R.id.textTitle);
        textSubtitle = findViewById(R.id.textSubtitle);
        layoutDetails = findViewById(R.id.layoutDetails);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(Constants.PAGES_COUNT);

        setPageDetails(page);
        checkBookmarkPage(page);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setPageDetails(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                animateActionBar(false);
                checkBookmarkPage(page);
                viewPager.setCurrentItem(Constants.PAGES_COUNT - getPage(seekBar.getProgress()), true);
            }
        });

        toggleActionBarVisibility(true);

        buttonBookmark.setOnClickListener(this);
        findViewById(R.id.buttonBack).setOnClickListener(this);
        findViewById(R.id.buttonMore).setOnClickListener(this);
    }

    private void setViewPager() {
        ArrayList<Integer> pages = Pages.getPages();
        Collections.reverse(pages);

        SlideQuranPageAdapter slideQuranPageAdapter = new SlideQuranPageAdapter();
        slideQuranPageAdapter.setPages(this, pages);
        viewPager.setAdapter(slideQuranPageAdapter);
        viewPager.setCurrentItem(Constants.PAGES_COUNT - page);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPageDetails(Constants.PAGES_COUNT - position);
                checkBookmarkPage(page);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        slideQuranPageAdapter.setQuranTouchListener(position -> toggleActionBar());

        viewPager.setOnSystemUiVisibilityChangeListener(
                flags -> {
                    boolean visible = (flags & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0;
                    animateActionBar(visible);
                });
    }

    @SuppressLint("InflateParams")
    private void setPopUpWindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_more, null);

        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setElevation((float) 2);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        view.findViewById(R.id.layoutSettings).setOnClickListener(v -> {
            popupWindow.dismiss();
            startActivity(new Intent(this, SettingsActivity.class));
        });

        view.findViewById(R.id.layoutInfo).setOnClickListener(v -> {
            popupWindow.dismiss();
            startActivity(new Intent(this, InfoActivity.class));
        });

        view.findViewById(R.id.layoutShare).setOnClickListener(v -> {
            popupWindow.dismiss();
            Utils.share(this, getResources().getString(R.string.app_name));
        });
    }

    private void setPageDetails(int progress) {
        page = getPage(progress);

        textTitle.setText(QuranInfo.getSuraNameString(QuranActivity.this, page));
        textSuraTitle.setText(QuranInfo.getSuraNameString(QuranActivity.this, page));
        textSubtitle.setText(QuranInfo.getPageSubtitle(QuranActivity.this, page));
        textPageNumber.setText(String.valueOf(page).concat(" - бет"));
        seekBar.setProgress(progress);

        QuranSettings.get(QuranActivity.this).setLastPage(page);
        bookmarkListener.onLastPageChange();
    }

    private int getPage(int progress) {
        if (progress == 0) {
            return 1;
        } else if (progress == 605) {
            return 604;
        } else {
            return progress;
        }
    }

    private void checkBookmarkPage(int page) {
        if (DBHelper.get().getBookmarkByPage(page) != null) {
            buttonBookmark.setImageResource(R.drawable.ic_bookmark_fill);
        } else {
            buttonBookmark.setImageResource(R.drawable.ic_bookmark);
        }
    }

    private void saveOrDeleteBookmarkPage(int page) {
        if (DBHelper.get().getBookmarkByPage(page) != null) {
            DBHelper.get().deleteBookmark(page);
            buttonBookmark.setImageResource(R.drawable.ic_bookmark);
        } else {
            DBHelper.get().saveBookmarkPage(page);
            buttonBookmark.setImageResource(R.drawable.ic_bookmark_fill);
        }

        bookmarkListener.onPageBookmarkChange();
    }

    private static class PagerHandler extends Handler {
        private final WeakReference<QuranActivity> activity;

        PagerHandler(QuranActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NotNull Message msg) {
            QuranActivity activity = this.activity.get();
            if (activity != null) {
                if (msg.what == MSG_HIDE_ACTIONBAR) {
                    activity.toggleActionBarVisibility(false);
                } else {
                    super.handleMessage(msg);
                }
            }
        }
    }

    private void toggleActionBarVisibility(boolean visible) {
        if (visible == isActionBarHidden) {
            toggleActionBar();
        }
    }

    private void toggleActionBar() {
        if (isActionBarHidden) {
            setUIVisibility(true);
            isActionBarHidden = false;
        } else {
            handler.removeMessages(MSG_HIDE_ACTIONBAR);
            setUIVisibility(false);
            isActionBarHidden = true;
        }
    }

    private void setUIVisibility(boolean isVisible) {
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (!isVisible) {
            flags |= View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        viewPager.setSystemUiVisibility(flags);
        animateActionBar(isVisible);
    }

    private void animateActionBar(boolean visible) {
        isActionBarHidden = !visible;

        viewPage.setVisibility(visible ? View.VISIBLE : View.GONE);

        appBarLayout.animate()
                .translationY(visible ? 0 : -appBarLayout.getHeight())
                .setDuration(250)
                .start();

        viewShadowBottom.animate()
                .translationY(visible ? 0 : -appBarLayout.getHeight())
                .setDuration(250)
                .start();

        layoutDetails.animate()
                .translationY(visible ? 0 : layoutDetails.getHeight() + 112)
                .setDuration(250)
                .start();
    }

    public static void setBookmarkListener(BookmarkListener listener) {
        bookmarkListener = listener;
    }
}