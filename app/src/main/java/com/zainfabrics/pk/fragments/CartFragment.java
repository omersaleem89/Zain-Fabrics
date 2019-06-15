package com.zainfabrics.pk.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.zainfabrics.pk.R;
import com.zainfabrics.pk.fragments.adapters.CartAdapter;
import com.zainfabrics.pk.fragments.models.ImageListModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    ListView listView;
    CartAdapter adapter;
    @BindView(R.id.cart_confirm_order_button) Button button;
    @BindView(R.id.cart_continue_shopping_button) Button buttonShop;
    @BindView(R.id.cart_textView_sub_total) TextView textViewSubTotal;
    @BindView(R.id.cart_textView_delivery_total) TextView textViewDelivery;
    @BindView(R.id.cart_textView_net_total) TextView textViewNetTotal;
    @BindView(R.id.cart_textView_stitching_charges) TextView textViewStitching;
    @BindView(R.id.cart_linear_stitching) LinearLayout linearLayoutStitching;
    ArrayList<ImageListModel> categoryModelArrayList = new ArrayList<>();

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this,v);
        textViewSubTotal.setText(((AppController) getActivity().getApplication()).getSubTotal());
        textViewDelivery.setText(((AppController) getActivity().getApplication()).getDeliveryTotal());
        textViewNetTotal.setText(((AppController) getActivity().getApplication()).getNetTotal());
        if(((AppController) getActivity().getApplication()).getIntStitching()==0){
            linearLayoutStitching.setVisibility(View.GONE);
        }
        else {
            textViewStitching.setText(((AppController) getActivity().getApplication()).getStitching());
        }
        listView=(ListView) v.findViewById(R.id.cart_listview);
        adapter=new CartAdapter(((AppController) getActivity().getApplication()).getArrayList(),getActivity());
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((AppController) getActivity().getApplication()).getArrayList().isEmpty()){
                    Snackbar snackbar=Snackbar.make(getView(),"Please Select Items",Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    snackbar.setActionTextColor(Color.WHITE);
                    snackbar.show();
                }else {
                    Fragment fragment = new ConfirmOrderFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.frame_layout, fragment).addToBackStack(null).commit();
                }
            }
        });
        buttonShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonShop.setEnabled(false);
                getCategory();
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    public void getCategory() {
        String tag_string_req = "string_req";

        String url = "https://www.zainfabricspk.com/cadmin/mobapi/category.php?action=getCategory";


        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean respnseError = jsonObject.getBoolean("error");
                    if (respnseError == false) {
                        JSONArray jsonArrayCategory = jsonObject.getJSONArray("category_data");
                        for (int i = 0; i < jsonArrayCategory.length(); i++) {
                            JSONArray jsonArray = jsonArrayCategory.getJSONArray(i);
                            String name = jsonArray.getString(0);
                            String image = jsonArray.getString(1);
                            ImageListModel model = new ImageListModel(
                                    "https://www.zainfabricspk.com/cadmin/img/cat/thumbs/"+image,
                                    name);
                            categoryModelArrayList.add(model);
                        }
                        final Bundle bundle = new Bundle();
                        bundle.putSerializable("categoryModelArrayList",categoryModelArrayList);
                        Fragment fragment = new HomeFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.frame_layout,fragment).commit();
                    } else {
                        String errorMessage = jsonObject.getString("error_msg");
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
                    Toast.makeText(getActivity(), "TimeoutError", Toast.LENGTH_SHORT).show();

                else if(error instanceof ServerError)
                    Toast.makeText(getActivity(), "ServerError", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }
}
