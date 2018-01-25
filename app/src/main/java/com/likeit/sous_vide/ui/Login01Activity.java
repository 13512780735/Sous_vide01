package com.likeit.sous_vide.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.util.AndroidWorkaround;
import com.likeit.sous_vide.util.HttpUtil;
import com.likeit.sous_vide.util.LoaddingDialog;
import com.likeit.sous_vide.util.StringUtil;
import com.likeit.sous_vide.util.ToastUtil;
import com.likeit.sous_vide.util.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class Login01Activity extends BaseActivity {

    @BindView(R.id.login_et_phone)
    EditText etPhone;
    @BindView(R.id.login_et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forgetpwd)
    TextView tvForgetPwd;
    @BindView(R.id.tv_Login)
    TextView tvLogin;
    private String phone;
    private String pwd;
    private LoaddingDialog loaddingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login01);
        Window window = this.getWindow();
        // 透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
        ButterKnife.bind(this);
        loaddingDialog = new LoaddingDialog(this);
        loaddingDialog.setCanceledOnTouchOutside(false);
        loaddingDialog.setCancelable(false);
        openPermission();
    }

    @OnClick({R.id.tv_register, R.id.tv_forgetpwd, R.id.tv_Login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                toActivity(RegisterActivity.class);
                break;
            case R.id.tv_forgetpwd:
                toActivity(ForgetPwdActivity.class);
                break;
            case R.id.tv_Login:
                login();
                break;
        }
    }

    private void login() {
        phone = etPhone.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        if (StringUtil.isBlank(phone)) {
            ToastUtil.showS(mContext, "The phone number can't be empty");
            return;
        }
//        if (!StringUtil.isCellPhone(phone)) {
//            ToastUtil.showS(mContext, "请输入正确的手机号");
//            return;
//        }
        if (StringUtil.isBlank(pwd)) {
            ToastUtil.showS(mContext, "The password cannot be empty");
            return;
        }
        Logining();
    }

    private void Logining() {
        String url = MyApiService.LoginUser;
        RequestParams params = new RequestParams();
        params.put("mobile", phone);
        params.put("password", pwd);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loaddingDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if ("true".equals(object.optString("status"))) {
                        UtilPreference.saveString(mContext, "ukey", object.optJSONObject("info").optString("ukey"));
                        UtilPreference.saveString(mContext, "nickname", object.optJSONObject("info").optJSONObject("info").optString("user_nickname"));
                        UtilPreference.saveString(mContext, "phone", object.optJSONObject("info").optJSONObject("info").optString("mobile"));
                        UtilPreference.saveString(mContext, "area", object.optJSONObject("info").optJSONObject("info").optString("area"));
                        UtilPreference.saveString(mContext, "sex", object.optJSONObject("info").optJSONObject("info").optString("sex"));
                        toActivityFinish(LoginActivity.class);
                    } else {
                        showToast(object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }
        });

//        HttpMethods.getInstance().LoginUser(new MySubscriber<Loginmodel>(this) {
//            @Override
//            public void onHttpCompleted(HttpResult<Loginmodel> httpResult) {
//                if (httpResult.isStatus()) {
//                    UtilPreference.saveString(mContext, "ukey", httpResult.getData().getUkey());
//                    UtilPreference.saveString(mContext, "user_nickname", httpResult.getData().getInfo().getUser_nickname());
//                    UtilPreference.saveString(mContext, "mobile", httpResult.getData().getInfo().getMobile());
//                    UtilPreference.saveString(mContext, "area", httpResult.getData().getInfo().getArea());
//                    toActivityFinish(LoginActivity.class);
//                } else {
//                    loaddingDialog.dismiss();
//                    showToast(httpResult.getMsg());
//                }
//            }
//
//            @Override
//            public void onHttpError(Throwable e) {
//
//            }
//        }, phone, pwd);
    }


    private void openPermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE + Manifest.permission.CAMERA + Manifest.permission.WRITE_EXTERNAL_STORAGE
                + Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(mContext,"请授予下面权限",Toast.LENGTH_SHORT).show();
            List<PermissionItem> permissions = new ArrayList<PermissionItem>();
            permissions.add(new PermissionItem(Manifest.permission.CALL_PHONE, "phone", R.drawable.permission_ic_phone));
            permissions.add(new PermissionItem(Manifest.permission.CAMERA, "photo", R.drawable.permission_ic_camera));
//            permissions.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位", R.drawable.permission_ic_location));
//            permissions.add(new PermissionItem(Manifest.permission.ACCESS_COARSE_LOCATION, "定位", R.drawable.permission_ic_location));
            //           permissions.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音", R.drawable.permission_ic_micro_phone));
            permissions.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "storage space", R.drawable.permission_ic_storage));
            HiPermission.create(mContext)
                    .permissions(permissions)
                    .msg("For your normal use of sous vide, the following permissions are required")
                    .animStyle(R.style.PermissionAnimModal)
                    .checkMutiPermission(new PermissionCallback() {
                        @Override
                        public void onClose() {
                            Log.i(TAG, "onClose");
                            ToastUtil.showS(mContext, "permission denied");
                        }

                        @Override
                        public void onFinish() {
                            //ToastUtil.showS(mContext,"权限已被开启");
                        }

                        @Override
                        public void onDeny(String permission, int position) {
                            Log.i(TAG, "onDeny");
                        }

                        @Override
                        public void onGuarantee(String permission, int position) {
                            Log.i(TAG, "onGuarantee");
                        }
                    });
            return;
        }
    }
}
