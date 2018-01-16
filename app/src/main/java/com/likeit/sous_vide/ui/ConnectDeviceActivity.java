package com.likeit.sous_vide.ui;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.Result;
import com.machtalk.sdk.domain.SearchedLanDevice;
import com.machtalk.sdk.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectDeviceActivity extends BaseActivity {
    @BindView(R.id.wifi_et_phone)
    EditText wifi_et_phone;
    @BindView(R.id.wifi_et_password)
    EditText wifi_et_password;
    private String ssid;
    private String str1;
    private MyMachtalkSDKListener mSdkListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connet_device);
        ButterKnife.bind(this);

        initTitle("Device");
        getConnectWifiSsid();
        initView();
        //MachtalkSDK.getInstance().setDeviceWiFiCancle();
    }

    private void initView() {
        wifi_et_phone.setText(str1);
    }

    private void getConnectWifiSsid() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID", wifiInfo.getSSID());
        ssid = wifiInfo.getSSID();
        str1 = ssid.replace("\"", "");
        Log.d("TAG", "str1-->" + str1);
        if ("<unknown ssid>".equals(str1)) {
            showToast("请连接WIFI!");
        }
    }

    @OnClick(R.id.connect_tv_next)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_tv_next:
                String pwd = wifi_et_password.getText().toString().trim();
                MachtalkSDK.getInstance().setDeviceWiFi(str1, pwd, MachtalkSDKConstant.NO_SCAN_CODE);
                mSdkListener = new MyMachtalkSDKListener();
                MachtalkSDK.getInstance().setContext(this);
                //MachtalkSDK.getInstance().setDeviceWiFiCancle();
                //toActivity(ConnectDevice02Activity.class);
                break;
        }
    }

    class MyMachtalkSDKListener extends MachtalkSDKListener {
        @Override
        public void onSetDeviceWiFi(Result result, SearchedLanDevice searchedLanDevice) {
            super.onSetDeviceWiFi(result, searchedLanDevice);
            Log.d("TAG", result + "");
            Log.d("TAG", searchedLanDevice + "");
            int success = Result.FAILED;
            String errmesg = null;
            if (result != null) {
                success = result.getSuccess();
                errmesg = result.getErrorMessage();
            }
            if (success == Result.SUCCESS) {
                toActivity(ConnectDevice02Activity.class);
            }
        }
    }
}
