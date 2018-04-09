package com.likeit.sous_vide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.model.AdModel;
import com.likeit.sous_vide.model.FoodDetailModel;
import com.likeit.sous_vide.model.MyFoodDetailsModel;
import com.likeit.sous_vide.util.HttpUtil;
import com.likeit.sous_vide.util.ToastUtil;
import com.likeit.sous_vide.util.UtilPreference;
import com.likeit.sous_vide.util.richtext.RichText;
import com.loopj.android.http.RequestParams;
import com.machtalk.sdk.connect.MachtalkSDK;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyFoodDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_right)
    TextView tvRight;
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
    @BindView(R.id.tv_richText)
    RichText tv_richText;
    private String id;
    private MyFoodDetailsModel foodDetailModel;
    String PrepTime = null;
    String Cooks = null;
    String deviceId = "111110001000078288";
    private String ukey;
    private List<AdModel> adlists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food_detail);
        ButterKnife.bind(this);
        Bundle bundel = getIntent().getExtras();
        ukey = UtilPreference.getStringValue(this, "ukey");
        id = bundel.getString("id");
        Log.d("TAG555", id);
        initData();

    }

    private void initData() {
        String url = MyApiService.myFoodDetail;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("id", id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG444", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("true".equals(object.optString("status"))) {
                        foodDetailModel = JSON.parseObject(object.optString("info"), MyFoodDetailsModel.class);
                        adlists=new ArrayList<>();
                        for(int i=0;i<foodDetailModel.getImgs().size();i++){
                            AdModel admodel=new AdModel();
                            admodel.setUrl(foodDetailModel.getImgs().get(i));
                            admodel.setName(foodDetailModel.getName());
                            adlists.add(admodel);
                        }
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
        tvHeader.setText("My Recipes");
        tvRight.setText("Delete");
        ImageLoader.getInstance().displayImage(foodDetailModel.getImgs().get(0), ivAvatar);
        tvName.setText(foodDetailModel.getName());
        tvPrepTime.setText("Prep Time:" + PrepTime + " Minutes");
        tvTotalTime.setText("Total Time:" + "");
        tvCook.setText("Cooks:" + Cooks + "℃");// + "℃ for 2 to 3Hours")
        tv_richText.setRichText(foodDetailModel.getDescription());
    }

    @OnClick({R.id.backBtn, R.id.tv_Heating, R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                Intent intent = new Intent(MyFoodDetailActivity.this, MyFoodListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_right://删除
                String url = MyApiService.delFood;
                RequestParams params = new RequestParams();
                params.put("ukey", ukey);
                params.put("id", id);
                HttpUtil.post(url, params, new HttpUtil.RequestListener() {
                    @Override
                    public void success(String response) {
                        Log.d("TAG444", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if ("true".equals(object.optString("status"))) {
                                ToastUtil.showL(MyFoodDetailActivity.this, object.optString("msg"));
                                Intent intent = new Intent(MyFoodDetailActivity.this, MyFoodListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Throwable e) {

                    }
                });
                break;
            case R.id.tv_Heating:
                MachtalkSDK.getInstance().operateDevice(deviceId, new String[]{"110"}, new String[]{Cooks});
                MachtalkSDK.getInstance().operateDevice(deviceId, new String[]{"108"}, new String[]{PrepTime});
                Bundle bundle = new Bundle();
                bundle.putString("time", PrepTime);
                bundle.putString("temp", Cooks);
                bundle.putString("adKey", "2");
                bundle.putSerializable("adlists", (Serializable) adlists);
                //  toActivity(MainActivity.class, bundle);
                Intent intent1 = new Intent(MyFoodDetailActivity.this, MainActivity.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyFoodDetailActivity.this, MyFoodListActivity.class);
        startActivity(intent);
        finish();
    }
}
