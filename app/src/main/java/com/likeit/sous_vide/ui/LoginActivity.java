package com.likeit.sous_vide.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.util.AndroidWorkaround;
import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.Result;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {
    private MyMachtalkSDKListener mSdkListener;

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
        mSdkListener = new MyMachtalkSDKListener();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(mSdkListener);
        MachtalkSDK.getInstance().userLogin("zc160428", "zc171115", 58);
    }

    class MyMachtalkSDKListener extends MachtalkSDKListener {
        @Override
        public void onUserLogin(Result result, String user) {
            Log.d("TAG", "result-->" + result + "user-->" + user);
            int success = Result.FAILED;
            String errmesg = null;
            if (result != null) {
                success = result.getSuccess();
                errmesg = result.getErrorMessage();
            }
            if (success == Result.SUCCESS) {
//                editor.putString(Constant.SP_KEY_USERNAME, telephone);
//                editor.putString(Constant.SP_KEY_PASSWORD, password);
//                editor.commit();
//                Log.i(TAG, "store username: " + telephone + " password: " + password);
//                DemoGlobal.instance().setUserName(telephone);
//                DemoGlobal.instance().setPassword(password);
//
//                Intent it = new Intent(UserLogin.this, Main.class);
//                startActivity(it);
//                UserLogin.this.finish();
            } else {
//                if (errmesg == null) {
//                    errmesg = getResources().getString(R.string.network_exception);
//                }
//                Util.showToast(context, errmesg);
            }
        }
    }

    @OnClick({R.id.iv_header_right,R.id.iv_header_left, R.id.login_tv_connet, R.id.login_tv_skip})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_right:
                toActivity(ConnectDeviceActivity.class);
                break;
            case R.id.iv_header_left:
                toActivity(PersonalCenterActivity.class);
                break;
            case R.id.login_tv_connet:
                toActivity(ConnectDeviceActivity.class);
                break;
            case R.id.login_tv_skip:
                toActivity(MainActivity.class);
                break;
        }
    }
    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(LoginActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                    return true;
                } else {
                    //MyActivityManager.getInstance().moveTaskToBack(mContext);// 不退出，后台运行
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
