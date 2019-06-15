package com.zainfabrics.pk.fragments.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zainfabrics.pk.AppController;
import com.zainfabrics.pk.R;
import com.zainfabrics.pk.fragments.CartFragment;
import com.zainfabrics.pk.fragments.models.ProductModel;

import java.util.ArrayList;

/**
 * Created by M Umer Saleem on 9/28/2017.
 */

public class CartAdapter extends BaseAdapter{
    ArrayList<ProductModel> productModelArrayList=new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;

    public CartAdapter(ArrayList<ProductModel> productModelArrayList, Activity activity) {
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
            view=inflater.inflate(R.layout.cart_listitem,null);
        ImageView imageView=(ImageView) view.findViewById(R.id.product_list_listitem_imageView);
        TextView textViewTitle=(TextView) view.findViewById(R.id.product_list_listitem_textView_title);
        TextView textViewCode=(TextView) view.findViewById(R.id.product_list_listitem_textView_code);
        final TextView textViewPrice=(TextView) view.findViewById(R.id.product_list_listitem_textView_price);
        TextView textViewQuantity=(TextView) view.findViewById(R.id.product_list_listitem_textView_quantity);
        ImageView button=(ImageView) view.findViewById(R.id.btn_remove_cart);

        final ProductModel model= productModelArrayList.get(i);

        Glide.with(activity).load(model.getImage_thumb())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        textViewTitle.setText(model.getTitle());
        textViewCode.setText("Code: "+model.getCode());
        textViewPrice.setText("Price: Rs."+model.getSalePrice()+"/-");
        textViewQuantity.setText("Quantity: "+model.getCartQuantity());
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                ((AppController) activity.getApplication()).removeItem(CartAdapter.this,i,model.getSalePrice(),model.getWeight(),model.getCartQuantity());
                if(((AppController) activity.getApplication()).getArrayList().isEmpty()){
                    Drawable drawable=activity.getResources().getDrawable(R.drawable.shopiing_cart_icon);
                    ActionMenuItemView menuItemView=activity.findViewById(R.id.menu_shopping_cart);
                    menuItemView.setIcon(drawable);
                }

                Fragment fragment=new CartFragment();
                ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .commit();

            }
        });
        return view;
    }
}
