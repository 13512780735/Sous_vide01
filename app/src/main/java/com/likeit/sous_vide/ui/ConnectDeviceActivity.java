package com.likeit.sous_vide.ui;

import android.os.Bundle;
import android.view.View;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectDeviceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connet_device);
        ButterKnife.bind(this);
        initTitle("Device");
    }

    @OnClick(R.id.connect_tv_next)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_tv_next:
                toActivity(ConnectDevice02Activity.class);
                break;
        }
    }
}
