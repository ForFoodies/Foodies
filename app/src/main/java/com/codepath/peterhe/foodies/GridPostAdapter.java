package com.codepath.peterhe.foodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;

import java.util.List;

public class GridPostAdapter extends BaseAdapter {
    Context context;
    List<Post> posts;
    LayoutInflater inflater;

    public GridPostAdapter(Context context,List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.profile_grid_item,null);
        }

        ImageView imageView = convertView.findViewById(R.id.ivProfileGrid);
        TextView textView = convertView.findViewById(R.id.tvProfileGrid);
        ParseFile image = posts.get(position).getImage();
        Glide.with(context).load(image.getUrl()).centerCrop().transform(new RoundedCorners(10)).into(imageView);
        textView.setText(posts.get(position).getCaption());
        return convertView;
    }
}
