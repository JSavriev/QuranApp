package sj.quran.app.util;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import io.reactivex.plugins.RxJavaPlugins;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(configuration);

        try {
            RxJavaPlugins.setErrorHandler(throwable -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        int nightMode;
        if (QuranSettings.get(this).getTheme()) {
            nightMode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            nightMode = AppCompatDelegate.MODE_NIGHT_NO;
        }

        AppCompatDelegate.setDefaultNightMode(nightMode);
    }
}
