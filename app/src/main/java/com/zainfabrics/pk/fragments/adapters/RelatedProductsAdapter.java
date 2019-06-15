package com.zainfabrics.pk.fragments.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zainfabrics.pk.R;
import com.zainfabrics.pk.fragments.models.ProductModel;

import java.util.ArrayList;

/**
 * Created by Umer on 10/15/2017.
 */

public class RelatedProductsAdapter extends RecyclerView.Adapter<RelatedProductsAdapter.MyViewHolder> {

    ArrayList<ProductModel> categoryModelArrayList;
    Activity activity;
    LayoutInflater inflater;

    public RelatedProductsAdapter(Activity activity, ArrayList<ProductModel> categoryModelArrayList) {
        this.categoryModelArrayList = categoryModelArrayList;
        this.activity = activity;
        inflater=activity.getLayoutInflater();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ImageView imageMain;
        public TextView textView_title, textView_code, textView_price;
        public ProgressBar progressBar;
        public MyViewHolder(View view) {
            super(view);
            imageMain = (ImageView) view.findViewById(R.id.product_list_listitem_imageView);
            textView_title = (TextView) view.findViewById(R.id.product_list_listitem_textView_title);
//            textView_code = (TextView) view.findViewById(R.id.product_list_listitem_textView_code);
            textView_price = (TextView) view.findViewById(R.id.product_list_listitem_textView_price);
            progressBar=(ProgressBar) view.findViewById(R.id.progress_related);
        }
    }

    @Override
    public RelatedProductsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.secondary_recycler_view_item, parent, false);
        return new RelatedProductsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.textView_title.setText(categoryModelArrayList.get(position).getTitle());
//        holder.textView_code.setText("Code: "+categoryModelArrayList.get(position).getCode());
        holder.textView_price.setText("Rs."+categoryModelArrayList.get(position).getSalePrice()+"/-");
        String imgUrl = categoryModelArrayList.get(position).getImage_thumb();
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
                .into(holder.imageMain);
    }


    @Override
    public int getItemCount() {
        if(categoryModelArrayList.size() >10)
            return 10;
        else
            return categoryModelArrayList.size();
    }



}
