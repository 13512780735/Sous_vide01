package com.likeit.sous_vide.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.listenter.OnLoginInforCompleted;
import com.likeit.sous_vide.listenter.OnLoginInforCompleted01;
import com.likeit.sous_vide.util.HttpUtil;
import com.likeit.sous_vide.util.LoaddingDialog;
import com.likeit.sous_vide.util.PhotoUtils;
import com.likeit.sous_vide.util.StringUtil;
import com.likeit.sous_vide.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomMenuActivity extends BaseActivity implements OnLoginInforCompleted, OnLoginInforCompleted01 {

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;


    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.ed_title)
    EditText edTitle;
    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_settemp)
    TextView tv_settemp;
    @BindView(R.id.iv_photo)
    ImageView iv_photo;

    private SetTimeFragment dialogFragment;
    private SetTempFragment dialogFragment01;
    private View mpopview;
    private PopupWindow mPopupWindow;
    private String base64Token;
    private String name;
    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_menu);
        ButterKnife.bind(this);
        tvHeader.setText("Custom menu");
    }


    @OnClick({R.id.backBtn, R.id.ll_setTime, R.id.ll_settemp, R.id.iv_takePhoto, R.id.tv_Save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.ll_setTime:
                showTime();
                break;
            case R.id.ll_settemp:
                showTemp();
                break;
            case R.id.iv_takePhoto:
                selectAvatar();
                break;
            case R.id.tv_Save:
                name = edTitle.getText().toString().trim();
                description = edContent.getText().toString().trim();
                if (StringUtil.isBlank(name) || StringUtil.isBlank(description) || StringUtil.isBlank(temp1) || StringUtil.isBlank(time1) || StringUtil.isBlank(base64Token)) {
                    ToastUtil.showS(mContext,"Please complete the information");
                    return;
                } else
                    upLoad();
                break;
        }
    }

    private void selectAvatar() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mpopview = inflater.inflate(R.layout.layout_choose_photo, null);
        mPopupWindow = new PopupWindow(mpopview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
//        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.mid_filter_bg));

        mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_custom_menu, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPopupWindow.setOutsideTouchable(false);
        //mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setTouchable(true); // 设置popupwindow可点击
        mPopupWindow.setOutsideTouchable(true); // 设置popupwindow外部可点击
        mPopupWindow.setFocusable(true); // 获取焦点
        mPopupWindow.update();

        Button mbuttonTakePhoto = (Button) mpopview
                .findViewById(R.id.button_take_photo);
        Button mbuttonChoicePhoto = (Button) mpopview
                .findViewById(R.id.button_choice_photo);
        Button mbuttonChoicecannce = (Button) mpopview
                .findViewById(R.id.button_choice_cancer);

        // 相册上传
        mbuttonChoicePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                autoObtainStoragePermission();
            }
        });

        // 拍照上传
        mbuttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                autoObtainCameraPermission();
            }
        });

        mbuttonChoicecannce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 如果点击了popupwindow的外部，popupwindow也会消失
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    private void showImages(Bitmap bitmap) {
        iv_photo.setImageBitmap(bitmap);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtil.showS(this, "You have rejected it once");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(mContext, "com.likeit.sous_vide.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtil.showS(this, "The device has no SD card！");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(mContext, "com.likeit.sous_vide.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtil.showS(this, "The device has no SD card！");
                    }
                } else {

                    ToastUtil.showS(this, "Please allow the camera to be opened！！");
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    ToastUtil.showS(this, "Please allow to operate SD Card！！");
                }
                break;
        }
    }

    private static final int output_X = 480;
    private static final int output_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Log.d("TAG321", imageUri.getPath());
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.likeit.sous_vide.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                        Log.d("TAG123", newUri.getPath());
                    } else {
                        ToastUtil.showS(this, "The device has no SD card！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    Log.d("TAG555", cropImageUri.toString());
                    if (bitmap != null) {
                        showImages(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytes = baos.toByteArray();
                        base64Token = Base64.encodeToString(bytes, Base64.DEFAULT);//  编码后
                        Log.d("TAG666", base64Token);
                    }
                    break;
            }
        }
    }

    private void upLoad() {
        final LoaddingDialog dialog=new LoaddingDialog(this);
        dialog.show();
        String url = MyApiService.Upfood;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("imgs", base64Token);
        params.put("name", name);
        params.put("wendu", temp1);
        params.put("time", time1);
        params.put("description", description);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if ("true".equals(object.optString("status"))) {
                        showToast(object.optString("msg"));
                        Bundle bundle=new Bundle();
                        bundle.putString("flag","1");
                        toActivity(MyFoodListActivity.class,bundle);
                        finish();
                    } else {
                        showToast(object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
dialog.dismiss();
            }
        });
//        HttpMethods.getInstance().Upfood(new MySubscriber<EmptyEntity>(this) {
//            @Override
//            public void onHttpCompleted(HttpResult<EmptyEntity> httpResult) {
//                if (httpResult.isStatus()) {
//                    showToast(httpResult.getMsg());
//                }else{
//                    showToast(httpResult.getMsg());
//                }
//            }
//
//            @Override
//            public void onHttpError(Throwable e) {
//
//            }
//        }, ukey, base64Token, name, temp1, time1, description);
    }

    private void showTime() {
        dialogFragment = new SetTimeFragment();
        dialogFragment.show(this.getSupportFragmentManager(), "android");
        dialogFragment.setOnLoginInforCompleted(this);
    }

    private void showTemp() {
        dialogFragment01 = new SetTempFragment();
        Bundle bundle = new Bundle();
        dialogFragment01.show(this.getSupportFragmentManager(), "android");
        dialogFragment01.setOnLoginInforCompleted01(this);
    }

    String time1, temp1;

    @Override
    public void inputLoginInforCompleted(String time) {
        time1 = time;
        tv_time.setText(time1 + " '");
    }

    @Override
    public void inputLoginInforCompleted01(String temp) {
        temp1 = temp;
        tv_settemp.setText(temp1 + "℃");
    }
}
