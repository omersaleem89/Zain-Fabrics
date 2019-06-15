package com.zainfabrics.pk.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.clans.fab.FloatingActionButton;
import com.zainfabrics.pk.AppController;
import com.zainfabrics.pk.ItemClickSupport;
import com.zainfabrics.pk.MainActivity;
import com.zainfabrics.pk.PrefManager;
import com.zainfabrics.pk.R;
import com.zainfabrics.pk.fragments.adapters.RelatedProductsAdapter;
import com.zainfabrics.pk.fragments.models.ProductModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.zainfabrics.pk.AppController.TAG;

/**
 * A simple {@link Fragment} subclass.
 */


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductInfoFragment extends Fragment {
    ProductModel model;
    PrefManager prefManager;

    @BindView(R.id.photo_view) PhotoView photoView;
    @BindView(R.id.product_info_product_title) TextView productTitle;
    @BindView(R.id.product_info_textView_product_code) TextView productCode;
    @BindView(R.id.product_info_textView_product_shirt) TextView productShirt;
    @BindView(R.id.product_info_textView_product_trouser) TextView productTrouser;
    @BindView(R.id.product_info_textView_product_dupatta) TextView productDupatta;
    @BindView(R.id.product_info_textView_type) TextView textViewType;
    @BindView(R.id.product_info_product_price) TextView productPrice;
    @BindView(R.id.product_info_other_image_thumb) ImageView imageViewOther;
    @BindView(R.id.product_info_main_image_thumb) ImageView imageViewMainThumb;
    @BindView(R.id.btn_order) Button button;
    @BindView(R.id.btn_buy_now) Button buttonBuyNow;
    @BindView(R.id.recyclerView_productInfo_relatedProducts) RecyclerView recyclerViewRelated;
    @BindView(R.id.textView_productInfo_relatedProducts_seeall) TextView textViewSeeAll;
    @BindView(R.id.btn_minus) Button btnMinus;
    @BindView(R.id.product_info_product_sale_price) TextView productSalePrice;
    @BindView(R.id.btn_add) Button btnAdd;
    @BindView(R.id.textView_quantity) TextView textViewQuantity;
    @BindView(R.id.spinner) Spinner spinner;
    @BindView(R.id.product_info_progress) ProgressBar progressBarImage;
    @BindView(R.id.menu_item_share)  FloatingActionButton fabShare;
    @BindView(R.id.menu_item_call)  FloatingActionButton fabCall;
    @BindView(R.id.menu_item_messages) FloatingActionButton fabMsg;
    @BindView(R.id.menu_item_whatsapp) FloatingActionButton fabWhatsapp;
    @BindView(R.id.linear_layout_images) LinearLayout linearLayoutImages;
    ArrayList<ProductModel> productModelArrayList;



    public ProductInfoFragment() {

        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_info, container, false);
        ButterKnife.bind(this,v);

        prefManager=new PrefManager(getActivity());

        Bundle bundle=getArguments();
        model=(ProductModel) bundle.getSerializable("model");
        productModelArrayList=(ArrayList<ProductModel>) bundle.getSerializable("productModelArrayList");

        RelatedProductsAdapter adapter=new RelatedProductsAdapter(getActivity(),productModelArrayList);
        recyclerViewRelated.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRelated.setLayoutManager(layoutManager);
        productTitle.setText(model.getTitle());
        productCode.setText(model.getCode());
        productSalePrice.setText(model.getPrice());
        productShirt.setText(model.getShirt());
        productTrouser.setText(model.getTrouser());
        productDupatta.setText(model.getDupatta());


        Glide
                .with(getActivity())
                .load(model.getImage())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(800,1200) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        progressBarImage.setVisibility(View.GONE);
                        photoView.setImageBitmap(resource); // Possibly runOnUiThread()
                    }
                });






        if(prefManager.getCountryName().equals("Pakistan"))
        {
            spinner.setVisibility(View.GONE);
            textViewType.setVisibility(View.GONE);
        }
        productPrice.setText("Rs. "+model.getSalePrice()+"/-");
        if(!model.getPrice().equals(model.getSalePrice()))
            productSalePrice.setText("Rs. "+model.getPrice()+"/-");
        else
        {
            productSalePrice.setVisibility(View.GONE);
        }


        imageViewOther.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                photoView.setImageResource(R.drawable.testing);
                Handler handler = new Handler(getActivity().getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBarImage.setVisibility(View.VISIBLE);
                    }
                });

                Glide
                        .with(getActivity())
                        .load(model.getOther_img())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(800,1200) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                progressBarImage.setVisibility(View.GONE);
                                photoView.setImageBitmap(resource); // Possibly runOnUiThread()
                            }
                        });
            }
        });

        imageViewMainThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide
                        .with(getActivity())
                        .load(model.getImage())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(800,1200) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                progressBarImage.setVisibility(View.GONE);
                                photoView.setImageBitmap(resource); // Possibly runOnUiThread()
                            }
                        });
            }
        });
        if(model.getOther_img().equals("https://www.zainfabricspk.com/cadmin/img/pro/"))
        {
           linearLayoutImages.setVisibility(View.GONE);
        }


        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, model.getTitle()+" is available on https://www.zainfabricspk.com Please Visit this website. Thanks");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        fabWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp();
            }
        });

        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:03317668255"));
                startActivity(intent);
            }
        });

        fabMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String number = "03317668255";  // The number on which you want to send SMS
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)).putExtra("sms_body"," Hello! I want to order this dress: "+model.getCode()));
            }


        });


        Glide.with(getActivity()).load(model.getOther_img_thumb())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewOther);

        Glide.with(getActivity()).load(model.getImage_thumb())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewMainThumb);

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                String quantityStr = textViewQuantity.getText().toString();
                int quantity = Integer.parseInt(quantityStr);
                if(quantity == 0){
                    Snackbar.make(getView(), "Please Select Quantity", Snackbar.LENGTH_SHORT).show();
                }

                else {
                    Drawable drawable= getActivity().getResources().getDrawable(R.drawable.added_shoping_cart_icon);
                    ActionMenuItemView menuItemView=getActivity().findViewById(R.id.menu_shopping_cart);
                    menuItemView.setIcon(drawable);
                    Fragment fragment = new CartFragment();
                    final Bundle bundle = new Bundle();
                    bundle.putSerializable("model", model);
                    fragment.setArguments(bundle);
                    if (((AppController) getActivity().getApplication()).checkProduct(model)) {
                        ProductModel model = (ProductModel) getArguments().getSerializable("model");
                        model.setCart_quantity(quantityStr);
                        model.setType(String.valueOf(spinner.getSelectedItem()));
                        ((AppController) getActivity().getApplication()).setArrayList(model);
                        Snackbar snackbar=Snackbar.make(getView(),"Item Added to Cart",Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.show();
                    }
                    else{
                        Snackbar snackbar=Snackbar.make(getView(),"Item Already Added to Cart",Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.show();

                    }
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                fm.beginTransaction().replace(R.id.frame_layout,fragment).commit();
                }



            }
        });

        buttonBuyNow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                String quantityStr = textViewQuantity.getText().toString();
                int quantity = Integer.parseInt(quantityStr);
                if(quantity == 0){
                    Snackbar.make(getView(), "Please Select Quantity", Snackbar.LENGTH_SHORT).show();
                }

                else {
                    Drawable drawable= getActivity().getResources().getDrawable(R.drawable.added_shoping_cart_icon);
                    ActionMenuItemView menuItemView=getActivity().findViewById(R.id.menu_shopping_cart);
                    menuItemView.setIcon(drawable);
                    Fragment fragment = new CartFragment();
                    final Bundle bundle = new Bundle();
                    bundle.putSerializable("model", model);
                    fragment.setArguments(bundle);
                    if (((AppController) getActivity().getApplication()).checkProduct(model)) {
                        ProductModel model = (ProductModel) getArguments().getSerializable("model");
                        model.setCart_quantity(quantityStr);
                        model.setType(String.valueOf(spinner.getSelectedItem()));
                        ((AppController) getActivity().getApplication()).setArrayList(model);
                        Snackbar snackbar=Snackbar.make(getView(),"Item Added to Cart",Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.show();
                    }
                    else{
                        Snackbar snackbar=Snackbar.make(getView(),"Item Already Added to Cart",Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.show();

                    }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.frame_layout,fragment).commit();
                }



            }
        });
        textViewSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bundle bundle = new Bundle();
                bundle.putString("brandID","");
                bundle.putSerializable("productModelArrayList",productModelArrayList);
                Fragment fragment = new ProductListFragment();
                fragment.setArguments(bundle);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.frame_layout,fragment).addToBackStack(null).commit();
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityStr = textViewQuantity.getText().toString();
                int quantity = Integer.parseInt(quantityStr);
                if(quantity > 0){
                    quantity--;
                    textViewQuantity.setText(String.valueOf(quantity));
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityStr = textViewQuantity.getText().toString();
                int quantity = Integer.parseInt(quantityStr);
                if(quantity < Integer.parseInt(model.getQuantity())){
                    quantity++;
                    textViewQuantity.setText(String.valueOf(quantity));
                }
            }
        });

        ItemClickSupport.addTo(recyclerViewRelated).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                ProductModel model= productModelArrayList.get(position);
                final Bundle bundle = new Bundle();
                bundle.putSerializable("model",model);
                bundle.putSerializable("productModelArrayList",productModelArrayList);
                Fragment fragment = new ProductInfoFragment();
                fragment.setArguments(bundle);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.frame_layout,fragment).addToBackStack(null).commit();
            }
        });

        return v;
    }



    private void openWhatsApp() {
        String smsNumber = "923317668255";
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
            startActivity(sendIntent);
        } else {
            Toast.makeText(getActivity(), "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

}

