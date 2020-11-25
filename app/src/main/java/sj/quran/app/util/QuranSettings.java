package sj.quran.app.util;

import android.content.Context;


public class QuranSettings {

    private static QuranSettings quranSettings;
    private static SharedPrefs sharedPrefs;

    public static synchronized QuranSettings get(Context context) {
        if (quranSettings == null) {
            quranSettings = new QuranSettings(context.getApplicationContext());
        }
        return quranSettings;
    }

    private QuranSettings(Context context) {
        sharedPrefs = SharedPrefs.get(context);
    }

    public int getLastPage() {
        return sharedPrefs.getInt(Constants.PREF_LAST_PAGE, Constants.NO_PAGE_SAVED);
    }

    public void setLastPage(int page) {
      sharedPrefs.saveInt(Constants.PREF_LAST_PAGE, page);
    }

    public boolean getShowPageInfo() {
        return sharedPrefs.getBoolean(Constants.SHOW_PAGE_INFO, true);
    }

    public void setShowPageInfo(boolean isShow) {
        sharedPrefs.saveBoolean(Constants.SHOW_PAGE_INFO, isShow);
    }

    public boolean getVolumeKey() {
        return sharedPrefs.getBoolean(Constants.VOLUME_KEY, false);
    }

    public void setVolumeKey(boolean key) {
        sharedPrefs.saveBoolean(Constants.VOLUME_KEY, key);
    }

    public boolean getTheme() {
        return sharedPrefs.getBoolean(Constants.THEME, false);
    }

    public void setTheme(boolean isDark) {
        sharedPrefs.saveBoolean(Constants.THEME, isDark);
    }
}
