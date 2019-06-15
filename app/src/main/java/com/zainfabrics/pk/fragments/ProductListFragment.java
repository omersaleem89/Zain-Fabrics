package com.zainfabrics.pk.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import com.zainfabrics.pk.AppController;
import com.zainfabrics.pk.R;
import com.zainfabrics.pk.fragments.adapters.ProductListAdapter;
import com.zainfabrics.pk.fragments.models.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends Fragment {
   GridView gridView;
    String brandID;
    Activity activity;
    ArrayList<ProductModel> productModelArrayList=new ArrayList<>();

    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_product_list, container, false);
        gridView=(GridView) v.findViewById(R.id.product_list_gridview);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar3);
        activity=getActivity();


        progressBar.setVisibility(View.VISIBLE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            brandID = bundle.getString("brandID");
            if(brandID.equals("")) {
                productModelArrayList = (ArrayList<ProductModel>) bundle.getSerializable("productModelArrayList");
                ProductListAdapter productListAdapter=new ProductListAdapter(productModelArrayList,getActivity());
                gridView.setAdapter(productListAdapter);
                productListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }else
                productModelArrayList.removeAll(productModelArrayList);
                getProducts();
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProductModel model= productModelArrayList.get(i);
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



    public void getProducts(){
        String tag_string_req = "string_req";

        String url = "https://zainfabricspk.com/cadmin/mobapi/product.php";


        StringRequest strReq = new StringRequest(Request.Method.GET,
                url + "?action=getProduct&brandID="+brandID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

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
                        ProductListAdapter productListAdapter=new ProductListAdapter(productModelArrayList,activity);
                        gridView.setAdapter(productListAdapter);
                        productListAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                    else {
                        String errorMessage=jsonObjectMain.getString("error_msg");
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
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
