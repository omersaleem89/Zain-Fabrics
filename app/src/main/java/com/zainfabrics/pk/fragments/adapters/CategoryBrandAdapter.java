package com.zainfabrics.pk.fragments.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zainfabrics.pk.R;
import com.zainfabrics.pk.fragments.models.ImageListModel;
import java.util.ArrayList;

/**
 * Created by Talal on 10/15/2017.
 */

public class CategoryBrandAdapter extends RecyclerView.Adapter<CategoryBrandAdapter.MyViewHolder> {

    ArrayList<ImageListModel> categoryModelArrayList;
    Activity activity;
    LayoutInflater inflater;

    public CategoryBrandAdapter(Activity activity, ArrayList<ImageListModel> categoryModelArrayList) {
        this.categoryModelArrayList = categoryModelArrayList;
        this.activity = activity;
        inflater=activity.getLayoutInflater();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_main;
        public ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            image_main = (ImageView) view.findViewById(R.id.imageView_brands_main_listitem);
            progressBar=(ProgressBar) view.findViewById(R.id.progress_category);
        }
    }

    @Override
    public CategoryBrandAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.brands_main_listitem, parent, false);
        return new CategoryBrandAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String imgUrl = categoryModelArrayList.get(position).getImage();
        Glide.with(activity).load(imgUrl)
        .listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        })
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image_main);
    }


    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }


}
