package sj.quran.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
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

import static sj.quran.app.util.Constants.JUZ2_COUNT;
import static sj.quran.app.util.Constants.PAGES_COUNT;
import static sj.quran.app.util.Constants.SURAS_COUNT;

public class SuraListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sura_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        QuranListAdapter adapter = new QuranListAdapter(getContext(), getSuraList());
        recyclerView.setAdapter(adapter);

        adapter.setQuranTouchListener((row, position) -> {
            Intent intent = new Intent(getContext(), QuranActivity.class);
            intent.putExtra(Constants.PREF_LAST_PAGE, row.page);
            startActivity(intent);
        });
    }

    private QuranRow[] getSuraList() {
        int next;
        int pos = 0;
        int sura = 1;
        QuranRow[] elements = new QuranRow[SURAS_COUNT + JUZ2_COUNT];

        Context context = getContext();
        boolean wantPrefix = context.getResources().getBoolean(R.bool.show_surat_prefix);
        boolean wantTranslation = context.getResources().getBoolean(R.bool.show_sura_names_translation);
        for (int juz = 1; juz <= JUZ2_COUNT; juz++) {
            final String headerTitle = context.getString(R.string.juz2_description,
                    QuranUtils.getLocalizedNumber(juz));
            final QuranRow.Builder headerBuilder = new QuranRow.Builder()
                    .withType(QuranRow.HEADER)
                    .withText(headerTitle)
                    .withPage(QuranInfo.JUZ_PAGE_START[juz - 1]);
            elements[pos++] = headerBuilder.build();
            next = (juz == JUZ2_COUNT) ? PAGES_COUNT + 1 :
                    QuranInfo.JUZ_PAGE_START[juz];

            while ((sura <= SURAS_COUNT) &&
                    (QuranInfo.SURA_PAGE_START[sura - 1] < next)) {
                final QuranRow.Builder builder = new QuranRow.Builder()
                        .withText(QuranInfo.getSuraName(context, sura, wantPrefix))
                        .withMetadata(QuranInfo.getSuraListMetaString(context, sura))
                        .withSura(sura)
                        .withPage(QuranInfo.SURA_PAGE_START[sura - 1]);
                elements[pos++] = builder.build();
                sura++;
            }
        }

        return elements;
    }
}