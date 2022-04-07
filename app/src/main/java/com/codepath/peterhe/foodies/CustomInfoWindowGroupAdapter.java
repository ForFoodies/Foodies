package com.codepath.peterhe.foodies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGroupAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private final Context mContext;

    public CustomInfoWindowGroupAdapter(Context context) {
        this.mContext = context;
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.item_group,null);
    }

    private void renderWindowText(Marker marker, View itemView) {
        Group group = (Group) marker.getTag();
        TextView tvName = (TextView) itemView.findViewById(R.id.tv_groupName_item1);
        tvName.setText(group.getName());
        TextView tvDescription = itemView.findViewById(R.id.tv_groupDescription_item1);
        tvDescription .setText(group.getDescription());
        TextView tvTime = itemView.findViewById(R.id.tv_GroupTimeDate_item1);
        String time = group.getTime() + " "+group.getDate();
        tvTime.setText(time);
        TextView tvNum = itemView.findViewById(R.id.tv_GroupNum_item1);
        String num = group.getCurrent().toString() + "/"+group.getMax().toString();
        tvNum.setText(num);
        TextView restName = itemView.findViewById(R.id.tv_GroupRestaurantName_item);
        restName.setText(group.getRestName());
        restName.setVisibility(View.VISIBLE);
        TextView restAddress = itemView.findViewById(R.id.tv_GroupRestaurantAddress_item);
        restAddress.setText(group.getAddress());
        restAddress.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }
}
