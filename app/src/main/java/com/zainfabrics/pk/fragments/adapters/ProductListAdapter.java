package com.zainfabrics.pk.fragments.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zainfabrics.pk.R;
import com.zainfabrics.pk.fragments.ProductInfoFragment;
import com.zainfabrics.pk.fragments.models.ProductModel;

import java.util.ArrayList;

/**
 * Created by M Umer Saleem on 9/28/2017.
 */

public class ProductListAdapter extends BaseAdapter{
    ArrayList<ProductModel> productModelArrayList=new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;

    public ProductListAdapter(ArrayList<ProductModel> productModelArrayList, Activity activity) {
        this.productModelArrayList = productModelArrayList;
        this.activity = activity;
        inflater=activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return productModelArrayList.size();
    }

    @Override
    public ProductModel getItem(int i) {
        return productModelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view==null)
            view=inflater.inflate(R.layout.product_list_listitem,null);
        ImageView imageView=(ImageView) view.findViewById(R.id.product_list_listitem_imageView);
        TextView textViewTitle=(TextView) view.findViewById(R.id.product_list_listitem_textView_title);
//        TextView textViewCode=(TextView) view.findViewById(R.id.product_list_listitem_textView_code);
        TextView textViewPrice=(TextView) view.findViewById(R.id.product_list_listitem_textView_price);
        Button button=(Button) view.findViewById(R.id.product_list_listitem_btn_add_to_cart);
        final ProgressBar progressBar=(ProgressBar) view.findViewById(R.id.progress_list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductModel model= productModelArrayList.get(i);
                final Bundle bundle = new Bundle();
                bundle.putSerializable("model",model);
                bundle.putSerializable("productModelArrayList",productModelArrayList);
                Fragment fragment = new ProductInfoFragment();
                fragment.setArguments(bundle);
                ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment).addToBackStack(null)
                        .commit();
            }
        });
        ProductModel model=productModelArrayList.get(i);

        Glide.with(activity).load(model.getImage_thumb())
                .listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        })
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        textViewTitle.setText(model.getTitle());
//        textViewCode.setText("Code: "+model.getCode());
        textViewPrice.setText("Price: Rs."+model.getSalePrice()+"/-");
        return view;
    }
}
