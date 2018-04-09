package com.likeit.sous_vide.ui;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.adapter.FoodCateListAdapter;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.listenter.OnLoginInforCompleted02;
import com.likeit.sous_vide.model.FoodCateListModel;
import com.likeit.sous_vide.util.HttpUtil;
import com.likeit.sous_vide.util.LoaddingDialog;
import com.likeit.sous_vide.util.UtilPreference;
import com.likeit.sous_vide.widget.MyListview;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodListFragment extends DialogFragment {


    private TextView tvHeader;
    private MyListview myListview;
    private List<FoodCateListModel> data;
    private LoaddingDialog loaddingDialog;
    private FoodCateListAdapter mAdapter;
    private String name;
    private int catid;
    private OnLoginInforCompleted02 mOnLoginInforCompleted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        Bundle bundle = getArguments();
        name = bundle.getString("name");
        catid = Integer.valueOf(bundle.getString("catid"));
        loaddingDialog = new LoaddingDialog(getActivity());
        loaddingDialog.setCanceledOnTouchOutside(false);
        loaddingDialog.setCancelable(false);
        data = new ArrayList<>();
        initDate();
        loaddingDialog.show();
        initView(view);
        tvHeader.setText(name);
        return view;
    }
    public void setOnLoginInforCompleted(OnLoginInforCompleted02 onLoginInforCompleted) {
        mOnLoginInforCompleted = onLoginInforCompleted;
    }
    private void initView(View view) {
        tvHeader = view.findViewById(R.id.tv_header);
        mAdapter = new FoodCateListAdapter(getActivity(), data);
        myListview = view.findViewById(R.id.myListview);
        myListview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                name = data.get(position).getName();
                catid = data.get(position).getId();
                tvHeader.setText(name);
                mOnLoginInforCompleted.inputLoginInforCompleted(name,String.valueOf(catid));
                getDialog().dismiss();
            }
        });
    }

    private void initDate() {
        String url = MyApiService.GetCatelist;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(getActivity(), "ukey"));
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG888",response);
                loaddingDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if ("true".equals(object.optString("status"))) {
                        JSONArray array = object.optJSONArray("info");
                        for (int i = 0; i < array.length(); i++) {
                            FoodCateListModel foodCateListModel = new FoodCateListModel();
                            JSONObject obj = array.optJSONObject(i);
                            foodCateListModel.setName(obj.optString("name"));
                            foodCateListModel.setId(obj.optInt("id"));
                            data.add(foodCateListModel);
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
}
