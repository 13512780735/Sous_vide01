package com.likeit.sous_vide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.model.FoodCateListModel;

import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class FoodCateListAdapter extends BaseAdapter {

    private Context context;
    private List<FoodCateListModel> data;
    private LayoutInflater inflater;
    private int checked = -1;//初始选择为-1，position没有-1嘛，那就是谁都不选咯

    public FoodCateListAdapter(Context context, List<FoodCateListModel> data) {
        this.context = context;
        this.data = data;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setChecked(int checked){//设定一个选中的标志位，在activity中传入值。
        this.checked = checked;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){//用于优化，不多说
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.foodcatelist_listview_items, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_chose);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        FoodCateListModel foodCateListModel=data.get(position);
        holder.tv.setText(foodCateListModel.getName());
        if(checked == position){
            holder.iv.setVisibility(View.VISIBLE);
        }else{
            holder.iv.setVisibility(View.GONE);
        }//这个else很重要噢，详细解读在下面
        return convertView;
    }
    class ViewHolder{//用于listview的优化，不多说
        public TextView tv;
        private ImageView iv;
    }
}
