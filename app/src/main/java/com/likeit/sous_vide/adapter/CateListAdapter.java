package com.likeit.sous_vide.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.likeit.sous_vide.R;
import com.likeit.sous_vide.http.network.api_service.MyApiService;
import com.likeit.sous_vide.model.CateListModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class CateListAdapter extends BaseQuickAdapter<CateListModel,BaseViewHolder>{
    public CateListAdapter(int layoutResId, List<CateListModel> data) {
        super(R.layout.catefoodlist_listview_items, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CateListModel cateListModel) {
        ImageLoader.getInstance().displayImage(cateListModel.getMore().getPhotos().get(0).getUrl(), (ImageView) baseViewHolder.getView(R.id.iv_avatar));
        baseViewHolder.setText(R.id.tv_name,cateListModel.getName());
    }
}
