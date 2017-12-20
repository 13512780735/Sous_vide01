package com.likeit.sous_vide.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.qfxl.view.RoundProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectDevice02Activity extends BaseActivity {
    @BindView(R.id.RoundProgressBar)
    RoundProgressBar mRoundProgressBar;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.connect_tv_heading)
    TextView connect_tv_heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_device02);

        ButterKnife.bind(this);
        initTitle("Device");
        initView();
    }

    private void initView() {
        mRoundProgressBar.setStrokeColor(this.getResources().getColor(R.color.header_bg));
        mRoundProgressBar.setAutoStart(true);
        mRoundProgressBar.setDirection(RoundProgressBar.Direction.FORWARD);

     //   LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    //    mLinearLayout.addView(mRoundProgressBar, lp);

        mRoundProgressBar.setProgressChangeListener(new RoundProgressBar.ProgressChangeListener() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onProgressChanged(int progress) {
            tv_progress.setText("The connected  "+  progress+" %");
            }
        });
    }

    @OnClick({R.id.connect_tv_heading})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_tv_heading:
                toActivity(MainActivity.class);
                break;
        }
    }
}
