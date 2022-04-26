package com.codepath.peterhe.foodies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class CustomInfoWindowMemberAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private final Context mContext;

    public CustomInfoWindowMemberAdapter(Context context) {
        this.mContext = context;
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.item_member,null);
    }

    private void renderWindowText(Marker marker, View itemView) {
        itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        ParseUser member = (ParseUser) marker.getTag();
        TextView tvName = (TextView) itemView.findViewById(R.id.tv_MemberName_item);
        tvName.setText(member.getUsername());
        TextView tvDescription = itemView.findViewById(R.id.tv_MemberDescription_item);
        tvDescription .setText(member.get("description").toString());
        TextView tvTime = itemView.findViewById(R.id.tv_MemberCreatedTime_item);
        String time = member.getCreatedAt().toString().substring(4, 10);
        tvTime.setText(time);
        ParseFile image = member.getParseFile("profile");
        Glide.with(mContext).load(image.getUrl()).centerCrop().placeholder(R.drawable.ic_baseline_downloading_24).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                ImageView iv = itemView.findViewById(R.id.iv_MemberProfile_item);
                iv.setImageDrawable(resource);
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                    marker.showInfoWindow();
                }
            }
        });

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

