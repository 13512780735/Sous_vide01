package com.likeit.sous_vide.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.model.FoodDetailModel;
import com.likeit.sous_vide.util.HttpUtil;
import com.loopj.android.http.RequestParams;
import com.machtalk.sdk.connect.MachtalkSDK;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodDetailActivity extends BaseActivity {

    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_prep_time)
    TextView tvPrepTime;
    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;
    @BindView(R.id.tv_cook)
    TextView tvCook;
    @BindView(R.id.tv_serverse)
    TextView tvServerse;
    private String id;
    private FoodDetailModel foodDetailModel;
    String PrepTime = null;
    String Cooks = null;
    String deviceId = "111110001000078288";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);
        Bundle bundel = getIntent().getExtras();
        id = bundel.getString("id");
        initData();
    }

    private void initData() {
        String url = MyApiService.food_detail;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("id", id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if ("true".equals(object.optString("status"))) {
                        foodDetailModel = JSON.parseObject(object.optString("info"), FoodDetailModel.class);
                        PrepTime = foodDetailModel.getTime();
                        Cooks = foodDetailModel.getWendu();
                        initView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }
        });

    }

    private void initView() {
        ImageLoader.getInstance().displayImage(foodDetailModel.getMore().getPhotos().get(0).getUrl(), ivAvatar);
        tvName.setText(foodDetailModel.getName());
        tvPrepTime.setText("Prep Time:" + PrepTime + " Minutes");
        tvTotalTime.setText("Total Time:" + "");
        tvCook.setText("Cooks:" + Cooks + "℃ for 2 to 3Hours");
        tvServerse.setText("Serverse:" + "");
    }

    @OnClick({R.id.backBtn, R.id.tv_Heating})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_Heating:
                MachtalkSDK.getInstance().operateDevice(deviceId, new String[]{"110"}, new String[]{Cooks});
                MachtalkSDK.getInstance().operateDevice(deviceId, new String[]{"108"}, new String[]{PrepTime});
                toActivityFinish(MainActivity.class);
                break;
        }
    }
}
