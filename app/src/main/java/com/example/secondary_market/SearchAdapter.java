package com.example.secondary_market;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SearchAdapter extends CommonAdapter<Bean>{

    public SearchAdapter(Context context, List<Bean> data, int layoutId) {
        super(context, data, layoutId);
    }
    @Override
    public void convert(ViewHolder holder, int position) {
        holder.setImageResource(R.id.item_search_iv_icon,mData.get(position).getIconId())
                .setText(R.id.item_search_tv_title,mData.get(position).getTitle())
                .setText(R.id.search_tv_price,mData.get(position).getPrice()+"元")
                .setText(R.id.search_tv_phone,mData.get(position).getPhone());
    }


}
