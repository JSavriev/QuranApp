package sj.quran.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sj.quran.app.R;
import sj.quran.app.db.model.BookmarkModel;
import sj.quran.app.util.QuranInfo;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BookmarkModel> pageBookmarks = new ArrayList<>();
    private QuranTouchListener touchListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_sura, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textSuraNumber.setVisibility(View.GONE);
        holder.imageRowIcon.setVisibility(View.VISIBLE);
        holder.imageRowIcon.setImageResource(R.drawable.ic_bookmark_fill);
        holder.imageRowIcon.setColorFilter(context.getResources().getColor(R.color.colorApp));
        holder.textTitle.setText(QuranInfo.getSuraNameString(context, pageBookmarks.get(position).getPage()));
        holder.textMetadata.setText(QuranInfo.getPageSubtitle(context, pageBookmarks.get(position).getPage()));
        holder.textPageNumber.setText(String.valueOf(pageBookmarks.get(position).getPage()));
    }

    @Override
    public int getItemCount() {
        return pageBookmarks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textSuraNumber;
        private TextView textTitle;
        private TextView textMetadata;
        private TextView textPageNumber;
        private ImageView imageRowIcon;

        public ViewHolder(@NonNull View view) {
            super(view);

            textSuraNumber = view.findViewById(R.id.textSuraNumber);
            textTitle = view.findViewById(R.id.textTitle);
            textMetadata = view.findViewById(R.id.textMetadata);
            textPageNumber = view.findViewById(R.id.textPageNumber);
            imageRowIcon = view.findViewById(R.id.imageRowIcon);

            view.setOnClickListener(v -> touchListener.onClick(pageBookmarks.get(getAdapterPosition()).getPage()));
        }
    }

    public void setPageBookmarks(Context context, ArrayList<BookmarkModel> pageBookmarks) {
        this.pageBookmarks = pageBookmarks;
        this.context = context;
        notifyDataSetChanged();
    }

    public interface QuranTouchListener {
        void onClick(int page);
    }

    public void setQuranTouchListener(QuranTouchListener listener) {
        touchListener = listener;
    }

}
