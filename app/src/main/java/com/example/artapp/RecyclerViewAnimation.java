package com.example.artapp;

import android.content.Context;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAnimation {
    private Context context;
    private int lastPosition = -1;

    public RecyclerViewAnimation(Context context) {
        this.context = context;
    }

    public void setAnimation(RecyclerView.ViewHolder holder, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.recycler_view_item_fall_down);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void resetLastPosition() {
        lastPosition = -1;
    }
}