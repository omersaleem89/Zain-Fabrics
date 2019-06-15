package com.zainfabrics.pk.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.zainfabrics.pk.AppController;
import com.zainfabrics.pk.ItemClickSupport;
import com.zainfabrics.pk.R;
import com.zainfabrics.pk.fragments.adapters.SubCatAdapter;
import com.zainfabrics.pk.fragments.models.ImageListModel;
import com.zainfabrics.pk.fragments.models.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCatFragment extends Fragment {

//    GridView gridView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView textView_brandTitle;
    SubCatAdapter adapter;
    Activity activity;
    String categoryName;
    List<ImageListModel> brandsArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_brands, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_brands);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar2);
        textView_brandTitle = (TextView) v.findViewById(R.id.textView_brand_title);
        activity=getActivity();


        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryName=bundle.getString("categoryName");
        }
        textView_brandTitle.setText(categoryName + " Collection");

        progressBar.setVisibility(View.VISIBLE);
        getBrand();
        final ArrayList<ProductModel> productModelArrayList=null;
        return v;
    }

    public void getBrand(){
        String tag_string_req = "string_req";

        String url = "https://zainfabricspk.com/cadmin/mobapi/subcategory.php";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url + "?action=getBrand&categoryName="+categoryName, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObjectMain = new JSONObject(response);
                    Boolean respnseError = jsonObjectMain.getBoolean("error");
                   if(respnseError==false){
                    JSONArray dataArray = jsonObjectMain.getJSONArray("sub_cat_data");
                    for (int i=0;i<dataArray.length();i++) {
                        JSONObject jsonObjectBrand = dataArray.getJSONObject(i);
                        String brandID=jsonObjectBrand.getString("id");
                        String brandName=jsonObjectBrand.getString("name");
                        String brandImage=jsonObjectBrand.getString("image");
                        ImageListModel model=new ImageListModel(brandID,
                                "https://www.zainfabricspk.com/cadmin/img/subcat/"+brandImage,
                                brandName);
                        brandsArrayList.add(model);
                    }
                    adapter = new SubCatAdapter(activity, (ArrayList<ImageListModel>) brandsArrayList);
                       RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2 , GridLayoutManager.VERTICAL, false);
                       recyclerView.setLayoutManager(layoutManager);
                       recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                               progressBar.setVisibility(View.INVISIBLE);
                       ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                           @Override
                           public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                               final ArrayList<ProductModel> productModelArrayList=null;
                               String brandID = brandsArrayList.get(position).getId();
                               brandsArrayList.removeAll(brandsArrayList);
                               final Bundle bundle = new Bundle();
                               bundle.putString("brandID",brandID);
                               bundle.putSerializable("productModelArrayList",productModelArrayList);
                               Fragment fragment = new ProductListFragment();
                               fragment.setArguments(bundle);
                               FragmentManager fm = getActivity().getSupportFragmentManager();
                               fm.beginTransaction().replace(R.id.frame_layout,fragment).addToBackStack(null).commit();
                           }
                       });
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
                if(error instanceof TimeoutError)
                    VolleyLog.d("Time Error: ", error.getMessage());
                else if(error instanceof ServerError)
                    VolleyLog.d("Server Error: ", error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}

