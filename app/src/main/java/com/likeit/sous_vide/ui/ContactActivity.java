package com.likeit.sous_vide.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.model.AboutModel;
import com.likeit.sous_vide.util.HttpUtil;
import com.likeit.sous_vide.util.ToastUtil;
import com.likeit.sous_vide.util.richtext.RichText;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends BaseActivity {


    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.richText)
    RichText richText;
    private AboutModel aboutModel;
    private String Link_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        initData();

        initView();
    }

    private void initData() {
        LoaddingShow();
        String url = MyApiService.aboutUs;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                LoaddingDismiss();
                Log.d("TAG", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("true".equals(object.optString("status"))) {
                        JSONArray array = object.optJSONArray("info");
                        for (int i = 0; i < array.length(); i++) {
                            aboutModel = JSON.parseObject(array.optString(i), AboutModel.class);
                            richText.setRichText(aboutModel.getLink_content());
                        }
                    } else {
                        ToastUtil.showS(mContext, object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                LoaddingDismiss();
            }
        });
    }

    private void initView() {
        tvHeader.setText("Contact us");
        //richText.setRichText(aboutModel.getLink_content());
    }

    @OnClick({R.id.backBtn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;

        }
    }
}
