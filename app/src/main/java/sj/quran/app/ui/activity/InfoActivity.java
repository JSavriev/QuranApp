package sj.quran.app.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import sj.quran.app.R;
import sj.quran.app.util.Constants;
import sj.quran.app.util.Utils;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        bindingViews();
        setNestedScrollView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                super.onBackPressed();
                break;
            case R.id.cardFacebook:
                Utils.shareLink(this, Constants.FACEBOOK);
                break;
            case R.id.cardTelegram:
                Utils.shareLink(this, Constants.TELEGRAM);
                break;
            case R.id.cardGmail:
                Utils.shareGmail(this);
                break;
            case R.id.cardRateUs:
                Utils.launchMarket(this);
                break;
        }
    }

    private void bindingViews() {
        findViewById(R.id.buttonBack).setOnClickListener(this);
        findViewById(R.id.cardRateUs).setOnClickListener(this);
        findViewById(R.id.cardFacebook).setOnClickListener(this);
        findViewById(R.id.cardGmail).setOnClickListener(this);
        findViewById(R.id.cardTelegram).setOnClickListener(this);
    }

    private void setNestedScrollView() {
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        View viewShadow = findViewById(R.id.viewShadow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener)
                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                        if (scrollY > 45) {
                            viewShadow.setVisibility(View.VISIBLE);
                        } else {
                            viewShadow.setVisibility(View.GONE);
                        }
                    });
        }
    }
}