package com.likeit.sous_vide.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.likeit.sous_vide.R;
import com.likeit.sous_vide.adapter.CateListAdapter;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.listenter.OnLoginInforCompleted02;
import com.likeit.sous_vide.model.CateListModel;
import com.likeit.sous_vide.util.HttpUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnLoginInforCompleted02 {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.RecyclerView)
    RecyclerView mRecyclerView;
    private String name;
    private String catid;
    private List<CateListModel> data;
    private CateListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        catid = bundle.getString("catid");
        tvHeader.setText("Recipes:" + name);
        data = new ArrayList<>();
        initData();
        initView();
    }

    private void initData() {
        String url = MyApiService.food_cate_detail;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("catid", Integer.valueOf(catid));
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG8888",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("true".equals(object.optString("status"))) {
                        JSONArray array = object.optJSONArray("info");
                        for (int i = 0; i < array.length(); i++) {
                            CateListModel cateListModel = JSON.parseObject(array.optString(i), CateListModel.class);
                            data.add(cateListModel);
                        }
                        mAdapter.notifyDataSetChanged();
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CateListAdapter(R.layout.catefoodlist_listview_items, data);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(data.get(position).getId()));
                toActivity(FoodDetailActivity.class, bundle);
            }
        });
    }

    @OnClick({R.id.tv_header, R.id.backBtn,R.id.iv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.iv_right:
                toActivity(CustomMenuActivity.class);
                break;
            case R.id.tv_header:
                FoodListFragment dialog = new FoodListFragment();
                dialog.setOnLoginInforCompleted(this);
                Bundle bundle = new Bundle();
                bundle.putString("catid", catid);
                bundle.putString("name", name);
                dialog.setArguments(bundle);
                dialog.show(this.getSupportFragmentManager(), "FoodListFragment");
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (data != null) {
            data.clear();
            initData();
        } else {
            return;
        }

    }

    @Override
    public void inputLoginInforCompleted(String name, String id) {
        tvHeader.setText("Recipes:" + name);
        catid = id;
        onRefresh();
    }
}
