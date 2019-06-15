package com.zainfabrics.pk.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.zainfabrics.pk.AppController;
import com.zainfabrics.pk.ItemClickSupport;
import com.zainfabrics.pk.R;
import com.zainfabrics.pk.fragments.adapters.CategoryBrandAdapter;
import com.zainfabrics.pk.fragments.adapters.RelatedProductsAdapter;
import com.zainfabrics.pk.fragments.models.ImageListModel;
import com.zainfabrics.pk.fragments.models.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.views.BannerSlider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    ArrayList<ImageListModel> categoryModelArrayList =new ArrayList<>();
    CategoryBrandAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView recyclerViewLatest;
    FragmentActivity activity;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        categoryModelArrayList=(ArrayList<ImageListModel>) getArguments().getSerializable("categoryModelArrayList");
        activity=getActivity();
        BannerSlider bannerSlider = (BannerSlider) v.findViewById(R.id.banner_slider);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_homeFragment);
        recyclerViewLatest=(RecyclerView) v.findViewById(R.id.recyclerView_home_latest);
        adapter = new CategoryBrandAdapter(getActivity(), categoryModelArrayList);
        RecyclerView.LayoutManager thirdLayoutManager = new GridLayoutManager(getActivity(),2, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(thirdLayoutManager);
        recyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        String categoryName = categoryModelArrayList.get(position).getName();
                        Fragment fragment = new SubCatFragment();
                        final Bundle bundle = new Bundle();
                        bundle.putString("categoryName",categoryName);
                        fragment.setArguments(bundle);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.frame_layout,fragment).addToBackStack(null).commit();
                    }
                });

        List<Banner> banners=new ArrayList<>();
        banners.add(new DrawableBanner(R.drawable.slide1));
        banners.add(new DrawableBanner(R.drawable.slide2));
        banners.add(new DrawableBanner(R.drawable.slide3));
        bannerSlider.setBanners(banners);

        getProducts();

        return v;
    }
    public void getProducts(){
        String tag_string_req = "string_req";

        String url = "https://zainfabricspk.com/cadmin/mobapi/latestProduct.php?action=getProduct";


        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    final ArrayList<ProductModel> productModelArrayList=new ArrayList<>();
                    JSONObject jsonObjectMain = new JSONObject(response);
                    Boolean respnseError = jsonObjectMain.getBoolean("error");
                    if(respnseError==false){
                        JSONArray dataArray = jsonObjectMain.getJSONArray("product_data");
                        for (int i=0;i<dataArray.length();i++) {
                            JSONObject jsonObjectSubCat = dataArray.getJSONObject(i);
                            String productCode=jsonObjectSubCat.getString("code");
                            String productName=jsonObjectSubCat.getString("title");
                            String productPrice=jsonObjectSubCat.getString("price");
                            String productSalePrice=jsonObjectSubCat.getString("sale_price");
                            String productShirt=jsonObjectSubCat.getString("shirt");
                            String productTrouser=jsonObjectSubCat.getString("trouser");
                            String productDupatta=jsonObjectSubCat.getString("dupatta");
                            String productImage=jsonObjectSubCat.getString("image");
                            String productThumbImage=jsonObjectSubCat.getString("image_thumb");
                            String productOtherImage=jsonObjectSubCat.getString("other_img");
                            String productThumbOtherImage=jsonObjectSubCat.getString("other_img_thumb");
                            String productQuantity=jsonObjectSubCat.getString("quantity");
                            String productWeight=jsonObjectSubCat.getString("weight");
                            ProductModel model=new ProductModel(productCode,
                                    productName,
                                    productPrice,
                                    productSalePrice,
                                    productShirt,
                                    productTrouser,
                                    productDupatta,
                                    "https://www.zainfabricspk.com/cadmin/img/pro/"+productImage,
                                    "https://www.zainfabricspk.com/cadmin/img/pro/thumbs/"+productThumbImage,
                                    "https://www.zainfabricspk.com/cadmin/img/pro/"+productOtherImage,
                                    "https://www.zainfabricspk.com/cadmin/img/pro/thumbs/"+ productThumbOtherImage,
                                    productQuantity,
                                    productWeight);

                            productModelArrayList.add(model);
                        }

                        RelatedProductsAdapter adapter=new RelatedProductsAdapter(activity,productModelArrayList);
                        recyclerViewLatest.setAdapter(adapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewLatest.setLayoutManager(layoutManager);
                        ItemClickSupport.addTo(recyclerViewLatest).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                ProductModel model= productModelArrayList.get(position);
                                final Bundle bundle = new Bundle();
                                bundle.putSerializable("model",model);
                                bundle.putSerializable("productModelArrayList",productModelArrayList);
                                Fragment fragment = new ProductInfoFragment();
                                fragment.setArguments(bundle);
                                FragmentManager fm = activity.getSupportFragmentManager();
                                fm.beginTransaction().replace(R.id.frame_layout,fragment).addToBackStack(null).commit();
                            }
                        });

                    }
                    else {
                        String errorMessage=jsonObjectMain.getString("error_msg");
                        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Login response: ", response.toString());


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("Error: ", error.getMessage());

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
