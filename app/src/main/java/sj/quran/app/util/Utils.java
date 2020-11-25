package sj.quran.app.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import sj.quran.app.R;

public class Utils {

    public static LinearLayoutManager vertical(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void share(Context context, String shareMessage) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage.concat("\n\n").concat(Constants.PLAY_MARKET_URL));
        context.startActivity(shareIntent);
    }

    public static void shareLink(Context context, String link) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareGmail(Context context) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", Constants.GMAIL, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
            emailIntent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.to_contact_developer));
            context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void launchMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            toast(context, context.getResources().getString(R.string.error));
        }
    }
}

