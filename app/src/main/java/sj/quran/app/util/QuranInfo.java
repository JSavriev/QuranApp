package sj.quran.app.util;

import android.content.Context;

import sj.quran.app.R;
import sj.quran.app.data.QuranData;

import static sj.quran.app.util.Constants.PAGES_COUNT;

public class QuranInfo {

    public static int[] SURA_PAGE_START = QuranData.SURA_PAGE_START;
    public static int[] PAGE_SURA_START = QuranData.PAGE_SURA_START;
    public static int[] PAGE_AYAH_START = QuranData.PAGE_AYAH_START;
    public static int[] JUZ_PAGE_START = QuranData.JUZ_PAGE_START;
    public static int[] PAGE_RUB3_START = QuranData.PAGE_RUB3_START;
    public static int[] SURA_NUM_AYAHS = QuranData.SURA_NUM_AYAHS;
    public static boolean[] SURA_IS_MAKKI = QuranData.SURA_IS_MAKKI;
    public static int[][] QUARTERS = QuranData.QUARTERS;

    public static String getSuraName(Context context, int sura, boolean wantPrefix) {
        if (sura < Constants.SURA_FIRST ||
                sura > Constants.SURA_LAST) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        if (wantPrefix) {
            builder.append(context.getString(R.string.quran_sura_title,
                    context.getResources().getStringArray(R.array.sura_names)[sura - 1]));
        } else {
            builder.append(context.getResources().getStringArray(R.array.sura_names)[sura - 1]);
        }

        return builder.toString();
    }

    public static int getSuraNumberFromPage(int page) {
        int sura = -1;
        for (int i = 0; i < Constants.SURAS_COUNT; i++) {
            if (SURA_PAGE_START[i] == page) {
                sura = i + 1;
                break;
            } else if (SURA_PAGE_START[i] > page) {
                sura = i;
                break;
            }
        }

        return sura;
    }

    public static String getSuraNameFromPage(Context context, int page, boolean wantTitle) {
        int sura = getSuraNumberFromPage(page);
        return (sura > 0) ? getSuraName(context, sura, wantTitle) : "";
    }

    public static String getPageSubtitle(Context context, int page) {
        String description = context.getString(R.string .page_description);
        return String.format(description,
                QuranUtils.getLocalizedNumber(page),
                QuranUtils.getLocalizedNumber(
                        QuranInfo.getJuzFromPage(page)));
    }

    public static String getJuzString(Context context, int page) {
        String description = context.getString(R.string.juz2_description);
        return String.format(description, QuranUtils.getLocalizedNumber(
                QuranInfo.getJuzFromPage(page)));
    }

    public static String getSuraAyahString(Context context, int sura, int ayah) {
        String suraName = getSuraName(context, sura, false);
        return context.getString(R.string.sura_ayah_notification_str, suraName, ayah);
    }

    public static String getSuraListMetaString(Context context, int sura) {
        String info = context.getString(QuranInfo.SURA_IS_MAKKI[sura - 1]
                ? R.string.makki : R.string.madani) + " - ";

        int ayahs = QuranInfo.SURA_NUM_AYAHS[sura - 1];
        info += context.getResources().getQuantityString(R.plurals.verses, ayahs,
                QuranUtils.getLocalizedNumber(ayahs));
        return info;
    }

    public static Integer[] getPageBounds(int page) {
        if (page > PAGES_COUNT)
            page = PAGES_COUNT;
        if (page < 1) page = 1;

        Integer[] bounds = new Integer[4];
        bounds[0] = PAGE_SURA_START[page - 1];
        bounds[1] = PAGE_AYAH_START[page - 1];
        if (page == PAGES_COUNT) {
            bounds[2] = Constants.SURA_LAST;
            bounds[3] = 6;
        } else {
            int nextPageSura = PAGE_SURA_START[page];
            int nextPageAyah = PAGE_AYAH_START[page];

            if (nextPageSura == bounds[0]) {
                bounds[2] = bounds[0];
                bounds[3] = nextPageAyah - 1;
            } else {
                if (nextPageAyah > 1) {
                    bounds[2] = nextPageSura;
                    bounds[3] = nextPageAyah - 1;
                } else {
                    bounds[2] = nextPageSura - 1;
                    bounds[3] = SURA_NUM_AYAHS[bounds[2] - 1];
                }
            }
        }
        return bounds;
    }

    public static String getSuraNameFromPage(Context context, int page) {
        for (int i = 0; i < Constants.SURAS_COUNT; i++) {
            if (SURA_PAGE_START[i] == page) {
                return getSuraName(context, i + 1, false);
            } else if (SURA_PAGE_START[i] > page) {
                return getSuraName(context, i, false);
            }
        }
        return "";
    }

    public static int getJuzFromPage(int page) {
        int juz = ((page - 2) / 20) + 1;
        return juz > 30 ? 30 : Math.max(juz, 1);
    }

    public static int getRub3FromPage(int page) {
        if ((page > PAGES_COUNT) || (page < 1)) return -1;
        return PAGE_RUB3_START[page - 1];
    }

    public static int getPageFromSuraAyah(int sura, int ayah) {
        if (ayah == 0) ayah = 1;
        if ((sura < 1) || (sura > Constants.SURAS_COUNT)
                || (ayah < Constants.AYA_MIN) ||
                (ayah > Constants.AYA_MAX))
            return -1;

        int index = QuranInfo.SURA_PAGE_START[sura - 1] - 1;
        while (index < PAGES_COUNT) {
            int ss = QuranInfo.PAGE_SURA_START[index];

            if (ss > sura || ((ss == sura) &&
                    (QuranInfo.PAGE_AYAH_START[index] > ayah))) {
                break;
            }

            index++;
        }

        return index;
    }

    public static int getNumAyahs(int sura) {
        if ((sura < 1) || (sura > Constants.SURAS_COUNT)) return -1;
        return SURA_NUM_AYAHS[sura - 1];
    }

    public static int getPageFromPos(int position) {
        return PAGES_COUNT - position;
    }

    public static int getPosFromPage(int page) {
        return PAGES_COUNT - page;
    }

    public static String getSuraNameString(Context context, int page) {
        return context.getString(R.string.quran_sura_title, getSuraNameFromPage(context, page));
    }

    public static int getPageAyahStart(int page) {
        return PAGE_AYAH_START[page];
    }
}
