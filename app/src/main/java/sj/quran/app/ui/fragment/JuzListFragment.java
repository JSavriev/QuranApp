package sj.quran.app.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sj.quran.app.R;
import sj.quran.app.model.QuranRow;
import sj.quran.app.ui.activity.QuranActivity;
import sj.quran.app.ui.adapter.QuranListAdapter;
import sj.quran.app.util.Constants;
import sj.quran.app.util.QuranInfo;
import sj.quran.app.util.QuranUtils;
import sj.quran.app.widget.JuzView;

import static sj.quran.app.util.Constants.JUZ2_COUNT;

public class JuzListFragment extends Fragment {

    private static final int[] entryTypes = {
            JuzView.TYPE_JUZ, JuzView.TYPE_QUARTER,
            JuzView.TYPE_HALF, JuzView.TYPE_THREE_QUARTERS};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_juz_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        QuranListAdapter adapter = new QuranListAdapter(getContext(), getJuz2List());
        recyclerView.setAdapter(adapter);

        adapter.setQuranTouchListener((row, position) -> {
            Intent intent = new Intent(getContext(), QuranActivity.class);
            intent.putExtra(Constants.PREF_LAST_PAGE, row.page);
            startActivity(intent);
        });
    }

    private QuranRow[] getJuz2List() {
        Activity activity = getActivity();
        Resources res = getResources();
        String[] quarters = res.getStringArray(R.array.quarter_prefix_array);
        QuranRow[] elements = new QuranRow[JUZ2_COUNT * (8 + 1)];

        int ctr = 0;
        for (int i = 0; i < (8 * JUZ2_COUNT); i++) {
            int[] pos = QuranInfo.QUARTERS[i];
            int page = QuranInfo.getPageFromSuraAyah(pos[0], pos[1]);

            if (i % 8 == 0) {
                int juz = 1 + (i / 8);
                final String juzTitle = getResources().getString(R.string.juz2_description,
                        QuranUtils.getLocalizedNumber(juz));
                final QuranRow.Builder builder = new QuranRow.Builder()
                        .withType(QuranRow.HEADER)
                        .withText(juzTitle)
                        .withPage(QuranInfo.JUZ_PAGE_START[juz - 1]);
                elements[ctr++] = builder.build();
            }

            final String verseString = getResources().getString(R.string.quran_ayah, pos[1]);
            final String metadata =
                    QuranInfo.getSuraName(activity, pos[0], true) + ", " + verseString;
            final QuranRow.Builder builder = new QuranRow.Builder()
                    .withText(quarters[i])
                    .withMetadata(metadata)
                    .withPage(page)
                    .withJuzType(entryTypes[i % 4]);
            if (i % 4 == 0) {
                final String overlayText =
                        QuranUtils.getLocalizedNumber(1 + (i / 4));
                builder.withJuzOverlayText(overlayText);
            }
            elements[ctr++] = builder.build();
        }

        return elements;
    }
}