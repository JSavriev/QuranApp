package sj.quran.app.ui.adapter;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Objects;

import sj.quran.app.R;
import sj.quran.app.util.Constants;
import sj.quran.app.util.QuranInfo;
import sj.quran.app.util.QuranSettings;

public class SlideQuranPageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Integer> pages = new ArrayList<>();
    private QuranTouchListener touchListener;

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(layoutInflater).inflate(R.layout.model_slide_quran_page, container, false);

        setViewShadow(view, position);

        ImageView imageQuranPage = view.findViewById(R.id.imageQuranPage);
        imageQuranPage.setImageResource(pages.get(position));

        if (QuranSettings.get(context).getTheme()) {
            float[] matrix = {
                    -1, 0, 0, 0, 255,
                    0, -1, 0, 0, 255,
                    0, 0, -1, 0, 255,
                    0, 0, 0, 1, 0
            };
            imageQuranPage.setColorFilter(new ColorMatrixColorFilter(matrix));
        } else {
            imageQuranPage.setColorFilter(context.getResources().getColor(R.color.colorTransparent));
        }

        imageQuranPage.setOnClickListener(v -> touchListener.onClick(position));

        if (QuranSettings.get(context).getShowPageInfo()) {
            setQuranPageDetails(view, position);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

    private void setViewShadow(View view, int position) {
        View viewLeft1 = view.findViewById(R.id.viewLeft1);
        View viewLeft = view.findViewById(R.id.viewLeft);
        View viewRight = view.findViewById(R.id.viewRight);
        View viewLeftShadow = view.findViewById(R.id.viewLeftShadow);
        View viewRightShadow = view.findViewById(R.id.viewRightShadow);

        if (QuranSettings.get(context).getTheme()) {
            viewLeft.setBackgroundResource(R.drawable.bg_night_left_border);
            viewRight.setBackgroundResource(R.drawable.bg_night_right_border);
        } else {
            viewLeft.setBackgroundResource(R.drawable.bg_border_left);
            viewRight.setBackgroundResource(R.drawable.bg_border_right);
        }

        if (position % 2 != 0) {
            viewLeftShadow.setVisibility(View.VISIBLE);
            viewRightShadow.setVisibility(View.GONE);
            viewLeft1.setVisibility(View.VISIBLE);
            viewLeft.setVisibility(View.GONE);
            viewRight.setVisibility(View.VISIBLE);
        } else {
            viewRightShadow.setVisibility(View.VISIBLE);
            viewLeftShadow.setVisibility(View.GONE);
            viewLeft1.setVisibility(View.GONE);
            viewLeft.setVisibility(View.VISIBLE);
            viewRight.setVisibility(View.GONE);
        }
    }

    private void setQuranPageDetails(View view, int position) {
        TextView textSuraTitle = view.findViewById(R.id.textSuraTitle);
        TextView textJuzTitle = view.findViewById(R.id.textJuzTitle);
        TextView textPageNumber = view.findViewById(R.id.textPageNumber);

        int page = Constants.PAGES_COUNT - position;
        textSuraTitle.setText(QuranInfo.getSuraNameString(context, page));
        textJuzTitle.setText(QuranInfo.getJuzString(context, page));
        textPageNumber.setText(String.valueOf(page));
    }

    public void setPages(Context context, ArrayList<Integer> pages) {
        this.context = context;
        this.pages = pages;
    }

    public void setQuranTouchListener(QuranTouchListener listener) {
        touchListener = listener;
    }

    public interface QuranTouchListener {
        void onClick(int position);
    }

}
