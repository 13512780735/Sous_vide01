package com.likeit.sous_vide.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.util.HttpUtil;
import com.likeit.sous_vide.util.LoaddingDialog;
import com.likeit.sous_vide.util.StringUtil;
import com.likeit.sous_vide.util.ToastUtil;
import com.likeit.sous_vide.util.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.register_et_phone)
    EditText edPhone;
    @BindView(R.id.register_et_pwd)
    EditText edPwd;
    @BindView(R.id.register_et_code)
    EditText edCode;
    @BindView(R.id.send_code_btn)
    TextView tvSendCode;
    @BindView(R.id.tv_Register)
    TextView tvRegister;
    private String mobile;
    private String password;
    private String code;
    TimeCount time = new TimeCount(60000, 1000);
    private LoaddingDialog loaddingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
        loaddingDialog = new LoaddingDialog(this);
        loaddingDialog.setCanceledOnTouchOutside(false);
        loaddingDialog.setCancelable(false);
        tvHeader.setText("Register");
    }

    @OnClick({R.id.send_code_btn, R.id.tv_Register, R.id.backBtn})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.send_code_btn:
                sendCode();
                break;
            case R.id.tv_Register:
                register();
                break;
        }
    }

    private void sendCode() {
        mobile = edPhone.getText().toString().trim();
        if (StringUtil.isBlank(mobile)) {
            ToastUtil.showS(mContext, "手机号不能为空");
            return;
//        }
//        if (!(StringUtil.isCellPhone(mobile))) {
//            ToastUtil.showS(mContext, "请输入正确的手机号码");
//            return;
        } else {
            SMSSDK.getVerificationCode("86", mobile);
            time.start();
        }
    }

    private void register() {
        mobile = edPhone.getText().toString().trim();
        code = edCode.getText().toString().trim();
        password = edPwd.getText().toString().trim();


        if (StringUtil.isBlank(code)) {
            ToastUtil.showS(mContext, "Verify that the code cannot be empty");
            return;
        }
        if (StringUtil.isBlank(password)) {
            ToastUtil.showS(mContext, "The password cannot be empty");
            return;
        }
         SMSSDK.submitVerificationCode("86", mobile, code);
       // Register();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            tvSendCode.setText("get code");
            tvSendCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            tvSendCode.setClickable(false);//防止重复点击
            tvSendCode.setText(millisUntilFinished / 1000 + "s");
        }
    }

    Handler mHandler = new

            Handler() {
                public void handleMessage(Message msg) {

                    // TODO Auto-generated method stub
                    super.handleMessage(msg);
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    Log.e("event", "event=" + event);
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        System.out.println("--------result" + event);
                        //短信注册成功后，返回MainActivity,然后提示新好友
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                            //Toast.makeText(getApplicationContext(), "提交验证码成功"+data.toString(), Toast.LENGTH_SHORT).show();
//                            showProgress("Loading...");
                            Register();
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //已经验证
                            Toast.makeText(getApplicationContext(), "The verification code has been sent", Toast.LENGTH_SHORT).show();


                        }

                    } else {
                        int status = 0;
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            status = object.optInt("status");
                            if (!TextUtils.isEmpty(des)) {
                                Toast.makeText(mContext, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }


                }
            };

    private void Register() {
        String url = MyApiService.RegisterUser;
        RequestParams params = new RequestParams();
        params.put("area", 86);
        params.put("mobile", mobile);
        params.put("password", password);
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
//        HttpMethods.getInstance().RegisterUser(new MySubscriber<Registermodel>(this) {
//            @Override
//            public void onHttpCompleted(HttpResult<Registermodel> httpResult) {
//                Log.d("TAG",httpResult.getData().getUkey());
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
//        }, mobile, "86", password);
    }

}
