package com.codepath.peterhe.foodies;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private final Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        this.mContext = context;
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.item_restaurant,null);

    }

    private void renderWindowText(Marker marker, View itemView) {
        YelpRestaurant restaurant = (YelpRestaurant) marker.getTag();
            Glide.with(mContext).load(restaurant.getImageUrl()).centerCrop().placeholder(R.drawable.ic_baseline_downloading_24).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    ImageView iv = itemView.findViewById(R.id.restaurantImage);
                    iv.setImageDrawable(resource);
                    if (marker.isInfoWindowShown()) {
                        marker.hideInfoWindow();
                        marker.showInfoWindow();
                    }
                }
            });
        //(ImageView) itemView.findViewById(R.id.restaurantImage)
            String name = "";
            if (restaurant.getName().length()>18) {
                name = restaurant.getName().substring(0,15);
                name += "...";
            } else {
                name = restaurant.getName();
            }

            TextView tvName = (TextView) itemView.findViewById(R.id.tvRestaurantName);
            tvName.setText(name);
            RatingBar rtBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            rtBar.setRating((float) restaurant.getRating());
            TextView tvNumReviews = itemView.findViewById(R.id.tvNumReviews);
            tvNumReviews.setText(restaurant.getNumReviews() + " Reviews");
            TextView tvAddress = itemView.findViewById(R.id.tvAddress);
            String address = restaurant.getLocation().getAddress1() + ", "+ restaurant.getLocation().getCity() + ", "+restaurant.getLocation().getState()+", "+restaurant.getLocation().getCountry()+", "+restaurant.getLocation().getZip_code();
            tvAddress.setText(address);
            String restaurantcategories = "";
            for (int i = 0; i<restaurant.getCategories().size();i++) {
                if (i >= 2) {
                    break;
                }
                restaurantcategories +=restaurant.getCategories().get(i).getTitle() + ", ";
            }
            restaurantcategories = restaurantcategories.substring(0, restaurantcategories.length()-2);
            TextView tvCategory =  itemView.findViewById(R.id.tvCategory);
            tvCategory.setText(restaurantcategories);
            TextView tvDistance = itemView.findViewById(R.id.tvDistance);
            tvDistance.setText(restaurant.displayDistance());
            TextView tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPrice.setText(restaurant.getPrice());
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
