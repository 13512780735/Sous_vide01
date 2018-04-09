package com.likeit.sous_vide.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.likeit.sous_vide.R;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.model.MyFoodModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class MyFoodListAdapter extends BaseQuickAdapter<MyFoodModel, BaseViewHolder> {
    public MyFoodListAdapter(int layoutResId, List<MyFoodModel> data) {
        super(R.layout.catefoodlist_listview_items, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MyFoodModel myFoodModel) {
        ImageLoader.getInstance().displayImage(myFoodModel.getImgs().get(0), (ImageView) baseViewHolder.getView(R.id.iv_avatar));
        baseViewHolder.setText(R.id.tv_name,myFoodModel.getName());

    }
}
