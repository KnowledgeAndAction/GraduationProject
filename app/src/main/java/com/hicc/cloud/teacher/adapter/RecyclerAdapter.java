package com.hicc.cloud.teacher.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.bean.SomeThing;

import java.util.List;

/**
 * Created by Administrator on 2017/1/23/023.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<SomeThing> mThingList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View shopView;
        ImageView shopImage;
        TextView shopNameText;
        TextView shopTimeText;
        TextView shopId;

        public ViewHolder(View itemView) {
            super(itemView);
            shopView = itemView;
            shopId = (TextView) itemView.findViewById(R.id.tv_name_id);
            shopImage = (ImageView) itemView.findViewById(R.id.shop_image);
            shopNameText = (TextView) itemView.findViewById(R.id.shop_name);
            shopTimeText = (TextView) itemView.findViewById(R.id.shop_time);
        }
    }


    public RecyclerAdapter(List<SomeThing> fruitList) {
        mThingList = fruitList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SomeThing shop = mThingList.get(position);
        holder.shopId.setText(shop.getNameId());
        holder.shopNameText.setText(shop.getName());
        holder.shopTimeText.setText(shop.getTime());
    }

    @Override
    public int getItemCount() {
        return mThingList.size();
    }
}
