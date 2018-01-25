package com.likeit.sous_vide.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.util.CircleImageView;
import com.likeit.sous_vide.util.HttpUtil;
import com.likeit.sous_vide.util.MyActivityManager;
import com.likeit.sous_vide.util.PhotoUtils;
import com.likeit.sous_vide.util.ToastUtil;
import com.likeit.sous_vide.util.UtilPreference;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalCenterActivity extends AppCompatActivity {

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    // 性别选择
    private View layoutGender;
    private PopupWindow popGender;
    private ListView popGenderList;
    private List<String> listGender;
    private String strItem;

    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_right)
    TextView tvRight;

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.ed_ednickName)
    EditText edName;
    @BindView(R.id.edphone)
    EditText edphone;
    @BindView(R.id.edEamil)
    EditText edEamil;
    @BindView(R.id.tv_tvGender)
    TextView tvGender;
    @BindView(R.id.rl_rlGender)
    LinearLayout llGender;
    private Context mContext;
    private Intent intent;
    private View mpopview;
    private PopupWindow mPopupWindow;
    private String base64Token;
    private String ukey;
    private String sex;
    private String phone = null;
    private String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);
        ukey = UtilPreference.getStringValue(this, "ukey");
        mContext = this;
        tvHeader.setText("Personal data");
        tvRight.setText("Save");
        initView();
    }

    private void initView() {
        ImageLoader.getInstance().displayImage(UtilPreference.getStringValue(mContext, "headimg"), ivAvatar);
        edName.setText(UtilPreference.getStringValue(mContext, "nickname"));
        edEamil.setText("");
        sex = UtilPreference.getStringValue(mContext, "sex");
        if ("0".equals(sex)) {
            tvGender.setText("Male");
        } else {
            tvGender.setText("Female");
        }
        // tvBirthday.setText(UtilPreference.getStringValue(mContext, "birthday"));
        edphone.setText(UtilPreference.getStringValue(mContext, "phone"));
    }

    @OnClick({R.id.backBtn, R.id.tv_right, R.id.rl_release, R.id.rl_change_pwd, R.id.tv_Exit, R.id.iv_send, R.id.iv_avatar, R.id.tv_tvGender})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_avatar:
                selectAvatar();
                break;
            case R.id.tv_right:
                phone = edphone.getText().toString();
                name = edName.getText().toString();
                saveInfo();
                break;
            case R.id.tv_tvGender:
                selectGneder();
                break;
            case R.id.rl_release:
                intent = new Intent(mContext, MyFoodListActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_change_pwd:
                intent = new Intent(mContext, EditPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_Exit:
                intent = new Intent(mContext, Login01Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.iv_send:
                intent = new Intent(mContext, CustomMenuActivity.class);
                startActivity(intent);
                break;
        }
    }

    @SuppressLint("InflateParams")
    private void selectGneder() {
        if (popGender != null && popGender.isShowing()) {
            popGender.dismiss();
        } else {
            layoutGender = getLayoutInflater().inflate(
                    R.layout.operationinto_popmenulist, null);
            popGenderList = (ListView) layoutGender.findViewById(R.id.menulist);
            listGender = new ArrayList<String>();
            listGender.add("choose sex");
            listGender.add("Male");
            listGender.add("Female");
            // 创建ArrayAdapter
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    mContext,
                    android.R.layout.simple_list_item_1, listGender);
            // 绑定适配器
            popGenderList.setAdapter(arrayAdapter);
            Log.d("TAG", "listLeft:" + popGenderList);

            // 点击listview中item的处理
            popGenderList
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            strItem = listGender.get(position);
                            if (position == 0) {
                                return;
                            } else {
                                tvGender.setText(strItem);
                                if ("Male".equals(strItem)) {
                                    sex = "0";
                                } else if ("Female".equals(strItem)) {
                                    sex = "1";
                                }
                            }
                            // 隐藏弹出窗口
                            if (popGender != null && popGender.isShowing()) {
                                popGender.dismiss();
                            }
                        }
                    });

            // 创建弹出窗口
            // 窗口内容为layoutLeft，里面包含一个ListView
            // 窗口宽度跟tvLeft一样
            popGender = new PopupWindow(layoutGender, llGender.getWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popGender.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.mid_filter_bg));
            popGender.setAnimationStyle(R.style.PopupAnimation);

            popGender.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popGender.setTouchable(true); // 设置popupwindow可点击
            popGender.setOutsideTouchable(true); // 设置popupwindow外部可点击
            popGender.setFocusable(true); // 获取焦点
            popGender.update();
            // 设置popupwindow的位置（相对tvLeft的位置）
            @SuppressWarnings("unused")
            int topBarHeight = llGender.getBottom();
            popGender.showAsDropDown(tvGender, 0, 10);
            popGender.setTouchInterceptor(new View.OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // 如果点击了popupwindow的外部，popupwindow也会消失
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        popGender.dismiss();
                        return true;
                    }
                    return false;
                }
            });

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
        ivAvatar.setImageBitmap(bitmap);
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
                        upload(base64Token);
                    }
                    break;
            }
        }
    }

    private void upload(String base64Token) {
        String url = MyApiService.UploadAvatar;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("avatar", base64Token);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if ("true".equals(object.optString("status"))) {
                        ToastUtil.showS(mContext, object.optString("msg"));
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
    }

    private void saveInfo() {
        String url = MyApiService.EditPreson;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("user_nickname", name);
        params.put("sex", sex);
        params.put("birthday", "");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if ("true".equals(object.optString("status"))) {
                        ToastUtil.showS(mContext, object.optString("msg"));
                        intent = new Intent(mContext, LoginActivity.class);
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
