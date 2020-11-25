package sj.quran.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class QuranUtils {

    private static NumberFormat mNumberFormatter;

    public static boolean isOnWifiNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean haveInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm == null ? null : cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static String getLocalizedNumber(int number) {
        if (mNumberFormatter == null) {
            mNumberFormatter = DecimalFormat.getIntegerInstance();
        }

        return mNumberFormatter.format(number);
    }
}
