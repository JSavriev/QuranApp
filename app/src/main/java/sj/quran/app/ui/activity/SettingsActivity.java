package sj.quran.app.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.NestedScrollView;

import sj.quran.app.R;
import sj.quran.app.util.QuranSettings;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bindingViews();
        setNestedScrollView();
        setDayNight();
        setShowPageInfo();
        setVolumeKey();
    }

    private void bindingViews() {
        findViewById(R.id.buttonBack).setOnClickListener(v -> super.onBackPressed());
    }

    private void setDayNight() {
        CheckBox checkboxDayNight = findViewById(R.id.checkboxDayNight);
        checkboxDayNight.setChecked(QuranSettings.get(this).getTheme());

        findViewById(R.id.cardDayNight).setOnClickListener(v -> {
            if (checkboxDayNight.isChecked()) {
                checkboxDayNight.setChecked(false);
                switchToMode(AppCompatDelegate.MODE_NIGHT_NO, false);
            } else {
                checkboxDayNight.setChecked(true);
                switchToMode(AppCompatDelegate.MODE_NIGHT_YES, true);
            }
        });
    }

    private void setShowPageInfo() {
        CheckBox checkboxShowPageInfo = findViewById(R.id.checkboxShowPageInfo);
        checkboxShowPageInfo.setChecked(QuranSettings.get(this).getShowPageInfo());

        findViewById(R.id.cardShowPageInfo).setOnClickListener(v -> {
            if (checkboxShowPageInfo.isChecked()) {
                checkboxShowPageInfo.setChecked(false);
                QuranSettings.get(this).setShowPageInfo(false);
            } else {
                checkboxShowPageInfo.setChecked(true);
                QuranSettings.get(this).setShowPageInfo(true);
            }
        });
    }

    private void setVolumeKey() {
        CheckBox checkboxVolumeKey = findViewById(R.id.checkboxVolumeKey);
        checkboxVolumeKey.setChecked(QuranSettings.get(this).getVolumeKey());

        findViewById(R.id.cardVolumeKey).setOnClickListener(v -> {
            if (checkboxVolumeKey.isChecked()) {
                checkboxVolumeKey.setChecked(false);
                QuranSettings.get(this).setVolumeKey(false);
            } else {
                checkboxVolumeKey.setChecked(true);
                QuranSettings.get(this).setVolumeKey(true);
            }
        });
    }

    private void switchToMode(int nightMode, boolean isChecked) {
        AppCompatDelegate.setDefaultNightMode(nightMode);
        QuranSettings.get(this).setTheme(isChecked);
    }

    private void setNestedScrollView() {
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        View viewShadow = findViewById(R.id.viewShadow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener)
                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                        if (scrollY > 45) {
                            viewShadow.setVisibility(View.VISIBLE);
                        } else {
                            viewShadow.setVisibility(View.GONE);
                        }
                    });
        }
    }
}