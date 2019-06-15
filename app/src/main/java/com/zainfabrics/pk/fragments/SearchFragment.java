package com.zainfabrics.pk.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    GridView gridView;
    ArrayList<ProductModel> productModelArrayList=new ArrayList<>();
    ProgressBar progressBar;
    ProductListAdapter productListAdapter;
    @BindView(R.id.search_btn) Button buttonSearch;
    @BindView(R.id.search_editText) EditText editText;
    String editTextQuery;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this,v);
        gridView=(GridView) v.findViewById(R.id.search_list_gridview);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbarSearch);
        progressBar.setVisibility(View.INVISIBLE);
        buttonSearch.setEnabled(false);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTextQuery = editText.getText().toString().trim();
                searchValidation(editTextQuery);

            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                productModelArrayList.removeAll(productModelArrayList);
                getSearchProducts(editTextQuery);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

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
    public void getSearchProducts(String query){
        progressBar.setVisibility(View.VISIBLE);

        String tag_string_req = "string_req";

        String url = "https://zainfabricspk.com/cadmin/mobapi/search.php";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url + "?action=getProduct&brandID="+query, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObjectMain = new JSONObject(response);
                    Boolean respnseError = jsonObjectMain.getBoolean("error");
                    if(respnseError==false){
                        JSONArray dataArray = jsonObjectMain.getJSONArray("product_data");
                        if(dataArray.length()!=0) {
                            for (int i = 0; i < dataArray.length(); i++) {
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
                            productListAdapter = new ProductListAdapter(productModelArrayList, getActivity());
                            productListAdapter.notifyDataSetChanged();
                            gridView.setAdapter(productListAdapter);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Snackbar snackbar=Snackbar.make(getView(),"Nothing Found",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            snackbar.setActionTextColor(Color.WHITE);
                            snackbar.show();

                        }

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
    public void searchValidation(String query){

        if(query.isEmpty()){
            editText.setError("Please Enter Keyword");
            buttonSearch.setEnabled(false);
        }
        else {

            editText.setError(null);
            buttonSearch.setEnabled(true);

        }
    }

}
