package sj.quran.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import sj.quran.app.R;
import sj.quran.app.db.DBHelper;
import sj.quran.app.db.model.BookmarkModel;
import sj.quran.app.helper.BookmarkListener;
import sj.quran.app.ui.activity.QuranActivity;
import sj.quran.app.ui.adapter.BookmarkAdapter;
import sj.quran.app.util.Constants;
import sj.quran.app.util.QuranInfo;
import sj.quran.app.util.QuranSettings;
import sj.quran.app.util.Utils;

public class BookmarksFragment extends Fragment {

    private TextView textTitle;
    private TextView textMetadata;
    private TextView textPageNumber;
    private MaterialCardView cardLastPage;
    private RelativeLayout layoutLastPage;
    private RelativeLayout layoutPageBookmarks;

    private BookmarkAdapter bookmarkAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QuranActivity.setBookmarkListener(new BookmarkListener() {
            @Override
            public void onLastPageChange() {
                try {
                    setLastPageData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageBookmarkChange() {
                try {
                    setPageBookmarksData(DBHelper.get().getBookmarkPages());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingViews(view);
        setLastPageData();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(Utils.vertical(getContext()));
        bookmarkAdapter = new BookmarkAdapter();
        recyclerView.setAdapter(bookmarkAdapter);

        setPageBookmarksData(DBHelper.get().getBookmarkPages());

        bookmarkAdapter.setQuranTouchListener(page -> {
            Intent intent = new Intent(getContext(), QuranActivity.class);
            intent.putExtra(Constants.PREF_LAST_PAGE, page);
            startActivity(intent);
        });

        ItemTouchHelper.SimpleCallback touchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                ArrayList<BookmarkModel> pageBookmarks = DBHelper.get().getBookmarkPages();

                int page = pageBookmarks.get(viewHolder.getAdapterPosition()).getPage();
                String toastText = QuranInfo.getSuraNameString(requireContext(), page)
                        .concat(", ")
                        .concat(QuranInfo.getPageSubtitle(requireContext(), page))
                        .concat(" ")
                        .concat(getString(R.string.deleted));

                DBHelper.get().deleteBookmark(page);
                pageBookmarks.remove(viewHolder.getAdapterPosition());

                setPageBookmarksData(pageBookmarks);
                Utils.toast(requireContext(), toastText);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void bindingViews(View view) {
        textTitle = view.findViewById(R.id.textTitle);
        textMetadata = view.findViewById(R.id.textMetadata);
        textPageNumber = view.findViewById(R.id.textPageNumber);
        cardLastPage = view.findViewById(R.id.cardLastPage);
        layoutLastPage = view.findViewById(R.id.layoutLastPage);
        layoutPageBookmarks = view.findViewById(R.id.layoutPageBookmarks);

        cardLastPage.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), QuranActivity.class);
            intent.putExtra(Constants.PREF_LAST_PAGE, QuranSettings.get(getContext()).getLastPage());
            startActivity(intent);
        });
    }

    private void setLastPageData() {
        int lastPage = QuranSettings.get(getContext()).getLastPage();
        if (lastPage != Constants.NO_PAGE_SAVED) {
            textTitle.setText(QuranInfo.getSuraNameString(requireContext(), lastPage));
            textMetadata.setText(QuranInfo.getPageSubtitle(requireContext(), lastPage));
            textPageNumber.setText(String.valueOf(lastPage));

            layoutLastPage.setVisibility(View.VISIBLE);
            cardLastPage.setVisibility(View.VISIBLE);
        } else {
            layoutLastPage.setVisibility(View.GONE);
            cardLastPage.setVisibility(View.GONE);
        }
    }

    private void setPageBookmarksData(ArrayList<BookmarkModel> pageBookmarks) {
        layoutPageBookmarks.setVisibility(pageBookmarks.size() > 0 ? View.VISIBLE : View.GONE);
        bookmarkAdapter.setPageBookmarks(getContext(), pageBookmarks);
    }
}