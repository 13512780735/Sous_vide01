package com.likeit.sous_vide.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.listenter.OnLoginInforCompleted;
import com.likeit.sous_vide.listenter.OnLoginInforCompleted01;
import com.likeit.sous_vide.model.DeviceAidDisplay;
import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.AidStatus;
import com.machtalk.sdk.domain.Device;
import com.machtalk.sdk.domain.DeviceAttributeInfo;
import com.machtalk.sdk.domain.DeviceStatus;
import com.machtalk.sdk.domain.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnLoginInforCompleted, OnLoginInforCompleted01 {
    @BindView(R.id.slider)
    SliderLayout mSliderLayout;
    //    @BindView(R.id.logo_title)
//    TextView tvLogoTitle;
    @BindView(R.id.ll_setTime)
    LinearLayout ll_setTime;
    @BindView(R.id.ll_settemp)
    LinearLayout ll_settemp;
    @BindView(R.id.iv_open_close)
    ImageView iv_open_close;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_settemp)
    TextView tv_settemp;
    @BindView(R.id.tv_currenttemp)
    TextView tv_currenttemp;
    @BindView(R.id.tv_c)
    TextView tv_c;
    @BindView(R.id.tv_f)
    TextView tv_f;
    @BindView(R.id.rl_temp)
    RelativeLayout rl_temp;
    String type = "0";//0; 代表关 1：代表开
    String flag = "0";//0; 摄氏度 1：华氏摄氏度
    private SetTimeFragment dialogFragment;
    private SetTempFragment dialogFragment01;
    String deviceId = "111110001000078288";
    private MyMachtalkSDKListener mSdkListener;
    private Device device;
    private DeviceAidDisplay aidDisplay;
    String temp = "55.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MachtalkSDK.getInstance().setContext(mContext);
        initTitle("Recipes");
        imageSlider();
        mSdkListener = new MyMachtalkSDKListener();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
        MachtalkSDK.getInstance().getDeviceAttribute(deviceId);
        MachtalkSDK.getInstance().setSdkListener(mSdkListener);
        initView();

    }

    private void initView() {
        if ("0".equals(flag)) {
            tv_c.setTextColor(this.getResources().getColor(R.color.white));
            tv_f.setTextColor(this.getResources().getColor(R.color.defualt_textcolor_a));
            MachtalkSDK.getInstance().operateDevice(deviceId, new String[]{"104"}, new String[]{flag});
            tv_settemp.setText(temp + "℃");
        } else {
            tv_c.setTextColor(this.getResources().getColor(R.color.defualt_textcolor_a));
            tv_f.setTextColor(this.getResources().getColor(R.color.white));
            MachtalkSDK.getInstance().operateDevice(deviceId, new String[]{"104"}, new String[]{flag});
            tv_settemp.setText(1.8 * Double.valueOf(temp) + 32 + "℉");
        }
    }

    private List<AidStatus> aidList;
    private List<DeviceAidDisplay> attrList = new ArrayList<DeviceAidDisplay>();

    class MyMachtalkSDKListener extends MachtalkSDKListener {
        @Override
        public void onGetDeviceAttribute(Result result, DeviceAttributeInfo deviceAttribute) {
            super.onGetDeviceAttribute(result, deviceAttribute);
//            Log.d("TAG",deviceAttribute.getDeviceModel());
//            Log.d("TAG",deviceAttribute.getAidList().toString());
//
        }

        @Override
        public void onQueryDeviceStatus(Result result, DeviceStatus deviceStatus) {
            super.onQueryDeviceStatus(result, deviceStatus);
            Log.d("TAG", result.getErrorMessage());
            Log.d("TAG", result.getErrorCode());
            int success = Result.FAILED;
            String errmesg = null;
            if (result != null) {
                success = result.getSuccess();
                errmesg = result.getErrorMessage();
            }
            if (success == Result.SUCCESS && deviceStatus != null && device.equals(deviceStatus.getDeviceId())) {
                aidList = deviceStatus.getDeviceAidStatuslist();
                if (aidList != null) {
                    for (AidStatus aidStatus : aidList) {
                        Log.d("TAG", "aid-->" + aidStatus.getAid());
                        Log.d("TAG", "values-->" + aidStatus.getValue());
//                        if (isIRDevice){
//                            try {
//                                if (Constant.AID_MODE.equals(aidStatus.getAid())){
//                                    irDeviceStatus.put(Constant.AID_MODE,aidStatus.getValue());
//                                } else if (Constant.AID_TEMP.equals(aidStatus.getAid())){
//                                    irDeviceStatus.put(Constant.AID_TEMP,aidStatus.getValue());
//                                } else if (Constant.AID_SELF_ID.equals(aidStatus.getAid())){
//                                    irDeviceStatus.put(Constant.AID_SELF_ID,aidStatus.getValue());
//                                } else if (Constant.AID_THIRD_ID.equals(aidStatus.getAid())){
//                                    irDeviceStatus.put(Constant.AID_THIRD_ID,aidStatus.getValue());
//                                }
//                            } catch (JSONException e){
//                                e.printStackTrace();
//                            }
                    }

//                        for (DeviceAidDisplay aidDisplay : attrList) {
//                            if (aidDisplay.getAidInfo().getAid() == Integer.parseInt(aidStatus.getAid())) {
//                                if (aidDisplay.getAidInfo().getAidType() == DeviceAid.TYPE_ENUM) {
//                                    if (aidDisplay.getAidInfo().getAidValueMap() != null) {
//                                        aidDisplay.setCurrentValue(aidDisplay.getAidInfo().getAidValueMap().get(aidStatus.getValue()));
//                                    }
//                                }
//                                else {
//                                    aidDisplay.setCurrentValue(aidStatus.getValue());
//                                }
//                                continue;
//                            }
//                        }
                }
            }

//            Log.d("TAG",deviceStatus.getDeviceId());
//            Log.d("TAG",deviceStatus.getDeviceAidStatuslist().get(0).getAid());
        }
    }

    private void imageSlider() {
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Beef", R.drawable.beef_bg);
        file_maps.put("Beefs", R.drawable.beef_bg);
        file_maps.put("Beef", R.drawable.beef_bg);
//        for (int i = 0; i < file_maps.size(); i++) {
//            DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
//            // textSliderView.description("");//设置标题
//            defaultSliderView.image(file_maps.get(i));//设置图片的网络地址
//            //添加到布局中显示
//            sliderShow.addSlider(defaultSliderView);
//        }
        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            toActivity(FoodListActivity.class);
                        }
                    });
            mSliderLayout.addSlider(textSliderView);
        }
        //其他设置
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//使用默认指示器，在底部显示
        mSliderLayout.setDuration(5000);//停留时间
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);

    }


    @OnClick({R.id.ll_setTime, R.id.ll_settemp, R.id.iv_open_close, R.id.rl_temp})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_setTime:
                showTime();
                break;
            case R.id.ll_settemp:
                showTemp();
                break;
            case R.id.iv_open_close:
                if ("0".equals(type)) {
                    iv_open_close.setImageResource(R.mipmap.icon_tab_open);
                    MachtalkSDK.getInstance().operateDevice(deviceId, new String[]{"101"}, new String[]{type});
                    type = "1";
                } else if ("1".equals(type)) {
                    iv_open_close.setImageResource(R.mipmap.icon_tab_close);
                    MachtalkSDK.getInstance().operateDevice(deviceId, new String[]{"101"}, new String[]{type});
                    type = "0";

                }
                break;
            case R.id.rl_temp:
                if ("0".equals(flag)) {
                    tv_c.setTextColor(this.getResources().getColor(R.color.defualt_textcolor_a));
                    tv_f.setTextColor(this.getResources().getColor(R.color.white));
                    flag = "1";
                    MachtalkSDK.getInstance().operateDevice(deviceId, new String[]{"104"}, new String[]{flag});
                    tv_settemp.setText(1.8 * Double.valueOf(temp) + 32 + "℉");

                } else {
                    tv_c.setTextColor(this.getResources().getColor(R.color.white));
                    tv_f.setTextColor(this.getResources().getColor(R.color.defualt_textcolor_a));
                    flag = "0";
                    MachtalkSDK.getInstance().operateDevice(deviceId, new String[]{"104"}, new String[]{flag});
                    tv_settemp.setText(temp + "℃");

                }
                break;
        }
    }

    private void showTemp() {
        dialogFragment01 = new SetTempFragment();
        Bundle bundle = new Bundle();
        bundle.putString("flag", flag);
        dialogFragment.setArguments(bundle);
        dialogFragment01.show(this.getSupportFragmentManager(), "android");
        dialogFragment01.setOnLoginInforCompleted01(this);
    }

    private void showTime() {
        dialogFragment = new SetTimeFragment();
        dialogFragment.show(this.getSupportFragmentManager(), "android");
        dialogFragment.setOnLoginInforCompleted(this);
    }


    @Override
    public void inputLoginInforCompleted(String time) {
        tv_time.setText(time + " '");

    }

    @Override
    public void inputLoginInforCompleted01(String temp1) {
        //tv_settemp.setText(temp);
        temp = temp1;
        if ("1".equals(flag)) {
            tv_settemp.setText(1.8 * Double.valueOf(temp) + 32 + "℉");
        } else {
            tv_settemp.setText(temp + "℃");
        }
    }
}
