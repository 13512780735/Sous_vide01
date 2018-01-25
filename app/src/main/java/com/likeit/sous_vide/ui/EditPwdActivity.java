package com.likeit.sous_vide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.util.HttpUtil;
import com.likeit.sous_vide.util.MyActivityManager;
import com.likeit.sous_vide.util.StringUtil;
import com.likeit.sous_vide.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPwdActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.register_et_pwd)
    EditText edordPwd;
    @BindView(R.id.register_new_pwd)
    EditText edNewPwd;
    @BindView(R.id.register_new_pwd_confirm)
    EditText edConfirmPwd;
    private String ordpwd, newpwd, confirmpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd);
        ButterKnife.bind(this);
        tvHeader.setText("Edit password");
    }

    @OnClick({R.id.backBtn, R.id.tv_Confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_Confirm:
                ordpwd = edordPwd.getText().toString().trim();
                newpwd = edNewPwd.getText().toString().trim();
                confirmpwd = edConfirmPwd.getText().toString().trim();
                if (StringUtil.isBlank(ordpwd) || StringUtil.isBlank(newpwd) || StringUtil.isBlank(confirmpwd)) {
                    ToastUtil.showS(mContext, "Please complete the information");
                    return;
                } else {
                    editInfo();
                }
                break;
        }
    }

    private void editInfo() {
        String url = MyApiService.EditPreson;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("old_password", ordpwd);
        params.put("password", newpwd);
        params.put("repassword", confirmpwd);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if ("true".equals(object.optString("status"))) {
                        ToastUtil.showS(mContext, object.optString("msg"));
                        Intent intent = new Intent(mContext, Login01Activity.class);
                        startActivity(intent);
                        MyActivityManager.getInstance().finishAllActivity();
                    } else {
                        ToastUtil.showS(mContext, object.optString("msg"));
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
}
