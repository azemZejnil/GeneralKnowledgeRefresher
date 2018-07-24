package com.example.bodhi64.general_knowledge_refresher.viewholders;

import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bodhi64.general_knowledge_refresher.R;
import com.example.bodhi64.general_knowledge_refresher.interfaces.ItemClickListener;

/**
 * Created by bodhi64 on 7/16/18.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView category_image;
    public TextView category_name;
    public View titleBackground;


    private ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        category_image=(ImageView)itemView.findViewById(R.id.category_image);
        category_name=(TextView)itemView.findViewById(R.id.category_name);
        titleBackground=(View) itemView.findViewById(R.id.title_background);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setBackgroundColor(Palette palette, RecyclerView.ViewHolder holder) {
        titleBackground.setBackgroundColor(palette.getVibrantColor(Color.parseColor("#99000000")));
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(itemView,getAdapterPosition(),false);
    }
}
