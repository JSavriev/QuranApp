package sj.quran.app.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import sj.quran.app.R;
import sj.quran.app.ui.adapter.PagerAdapter;
import sj.quran.app.ui.fragment.BookmarksFragment;
import sj.quran.app.ui.fragment.JuzListFragment;
import sj.quran.app.ui.fragment.SuraListFragment;
import sj.quran.app.util.Constants;
import sj.quran.app.util.QuranSettings;
import sj.quran.app.util.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingView();
        setPopUpWindow();
        setViewPager();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonQuran:
                Intent intent = new Intent(this, QuranActivity.class);
                intent.putExtra(Constants.PREF_LAST_PAGE, QuranSettings.get(this).getLastPage());
                startActivity(intent);
                break;
            case R.id.buttonMore:
                popupWindow.showAsDropDown(v, 0, -36);
                break;
        }
    }

    private void bindingView() {
        findViewById(R.id.buttonQuran).setOnClickListener(this);
        findViewById(R.id.buttonMore).setOnClickListener(this);
    }

    private void setViewPager() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new SuraListFragment(), getResources().getString(R.string.list_sura));
        pagerAdapter.addFragment(new JuzListFragment(), getResources().getString(R.string.quran_juz2));
        pagerAdapter.addFragment(new BookmarksFragment(), getResources().getString(R.string.bookmarks));

        int limit = (pagerAdapter.getCount() > 1 ? pagerAdapter.getCount() - 1 : 1);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
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
}