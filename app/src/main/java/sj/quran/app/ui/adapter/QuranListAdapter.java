package sj.quran.app.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import sj.quran.app.R;
import sj.quran.app.model.QuranRow;
import sj.quran.app.util.QuranUtils;
import sj.quran.app.widget.JuzView;

public class QuranListAdapter extends RecyclerView.Adapter<QuranListAdapter.HeaderHolder> {

    private Context context;
    private LayoutInflater inflater;
    private QuranRow[] elements;
    private QuranTouchListener touchListener;

    public QuranListAdapter(Context context, QuranRow[] elements) {
        inflater = LayoutInflater.from(context);
        this.elements = elements;
        this.context = context;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return elements.length;
    }

    private void bindRow(HeaderHolder vh, int position) {
        ViewHolder holder = (ViewHolder) vh;

        final QuranRow item = elements[position];
        bindHeader(vh, position);
        holder.textSuraNumber.setText(QuranUtils.getLocalizedNumber(item.sura));

        holder.textMetadata.setVisibility(View.VISIBLE);
        holder.textMetadata.setText(item.metadata);

        if (item.juzType != null) {
            holder.imageRowIcon.setImageDrawable(
                    new JuzView(context, item.juzType, item.juzOverlayText));
            holder.imageRowIcon.setVisibility(View.VISIBLE);
            holder.textSuraNumber.setVisibility(View.GONE);
        } else if (item.imageResource == null) {
            holder.textSuraNumber.setVisibility(View.VISIBLE);
            holder.imageRowIcon.setVisibility(View.GONE);
        } else {
            holder.imageRowIcon.setImageResource(item.imageResource);
            if (item.imageFilterColor == null) {
                holder.imageRowIcon.setColorFilter(null);
            } else {
                holder.imageRowIcon.setColorFilter(
                        item.imageFilterColor, PorterDuff.Mode.SRC_ATOP);
            }
            holder.imageRowIcon.setVisibility(View.VISIBLE);
            holder.textSuraNumber.setVisibility(View.GONE);
        }
    }

    private void bindHeader(HeaderHolder holder, int pos) {
        final QuranRow item = elements[pos];
        holder.textTitle.setText(item.text);
        if (item.page == 0) {
            holder.textPageNumber.setVisibility(View.GONE);
        } else {
            holder.textPageNumber.setVisibility(View.VISIBLE);
            holder.textPageNumber.setText(
                    QuranUtils.getLocalizedNumber(item.page));
        }
    }

    @NotNull
    @Override
    public HeaderHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            final View view = inflater.inflate(R.layout.model_header, parent, false);
            return new HeaderHolder(view);
        } else {
            final View view = inflater.inflate(R.layout.model_sura, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NotNull HeaderHolder viewHolder, int position) {
        final int type = getItemViewType(position);
        if (type == 0) {
            bindHeader(viewHolder, position);
        } else {
            bindRow(viewHolder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return elements[position].isHeader() ? 0 : 1;
    }

    public void setQuranTouchListener(QuranTouchListener listener) {
        touchListener = listener;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private TextView textPageNumber;

        public HeaderHolder(View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.textTitle);
            textPageNumber = (TextView) itemView.findViewById(R.id.textPageNumber);

            itemView.setOnClickListener(v -> {
                QuranRow element = elements[getAdapterPosition()];
                touchListener.onClick(element, getAdapterPosition());
            });
        }
    }

    class ViewHolder extends HeaderHolder {

        private TextView textSuraNumber;
        private TextView textMetadata;
        private ImageView imageRowIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            textMetadata = (TextView) itemView.findViewById(R.id.textMetadata);
            textSuraNumber = (TextView) itemView.findViewById(R.id.textSuraNumber);
            imageRowIcon = (ImageView) itemView.findViewById(R.id.imageRowIcon);
        }
    }

    public interface QuranTouchListener {
        void onClick(QuranRow row, int position);
    }
}
