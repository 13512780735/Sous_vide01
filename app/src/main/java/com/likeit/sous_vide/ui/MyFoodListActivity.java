package com.likeit.sous_vide.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.likeit.sous_vide.R;
import com.likeit.sous_vide.adapter.MyFoodListAdapter;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.model.MyFoodModel;
import com.likeit.sous_vide.util.HttpUtil;
import com.likeit.sous_vide.util.LoaddingDialog;
import com.likeit.sous_vide.util.MyActivityManager;
import com.likeit.sous_vide.util.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyFoodListActivity extends AppCompatActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.RecyclerView)
    RecyclerView mRecyclerView;
    private MyFoodListActivity mContext;
    private String ukey;
    private List<MyFoodModel> data;
    private MyFoodListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food_list);
        MyActivityManager.getInstance().addActivity(this);
        mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ukey = UtilPreference.getStringValue(this, "ukey");
        ButterKnife.bind(this);
        data = new ArrayList<>();
        initData();
        initView();
    }

    private void initData() {
        final LoaddingDialog dialog = new LoaddingDialog(this);
        dialog.show();
        String url = MyApiService.MyfoodList;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                dialog.dismiss();
                Log.d("TAG999", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("true".equals(object.optString("status"))) {
                        JSONArray array = object.optJSONArray("info");
                        for (int i = 0; i < array.length(); i++) {
                            MyFoodModel myFoodModel = JSON.parseObject(array.optString(i), MyFoodModel.class);
                            data.add(myFoodModel);
                        }
                        mAdapter.notifyDataSetChanged();
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
    }

    private void initView() {
        tvHeader.setText("My Release");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyFoodListAdapter(R.layout.catefoodlist_listview_items, data);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(data.get(position).getId()));
                Intent intent = new Intent(mContext, MyFoodDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    @OnClick({R.id.backBtn})
    public void onClick(View v) {
        Intent intent = new Intent(mContext, PersonalCenterActivity.class);
        intent.putExtra("flag", "1");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(mContext, PersonalCenterActivity.class);
        intent.putExtra("flag", "1");
        startActivity(intent);
        finish();
    }
}
