package com.likeit.sous_vide.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.util.AndroidWorkaround;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        // 透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_header_right, R.id.login_tv_connet, R.id.login_tv_skip})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_right:
                toActivity(ConnectDeviceActivity.class);
                break;
            case R.id.login_tv_connet:
                toActivity(ConnectDeviceActivity.class);
                break;
            case R.id.login_tv_skip:
                toActivity(MainActivity.class);
                break;
        }
    }
}
