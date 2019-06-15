package com.zainfabrics.pk.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.ActionMenuItemView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.zainfabrics.pk.AppController;
import com.zainfabrics.pk.Order;
import com.zainfabrics.pk.PrefManager;
import com.zainfabrics.pk.R;
import com.zainfabrics.pk.fragments.models.ImageListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmOrderFragment extends Fragment {
    EditText editTextName, editTextAddress, editTextPhone1,editTextPhone2,editTextCom;
    Button btnConfirm;
    String name , address, phone1,phone2,comment;
    TextInputLayout textInput_name, textInput_address, textInput_phone1,textInput_phone2,textInputCom;
    ArrayList<ImageListModel> categoryModelArrayList = new ArrayList<>();
    PrefManager prefManager;



    public ConfirmOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        createView(v);
        prefManager=new PrefManager(getActivity());
        editTextName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = editTextName.getText().toString().trim();
                nameValidation(name);
            }
        });

        editTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                address = editTextAddress.getText().toString().trim();
                addressValidation(address);

            }
        });

        editTextPhone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                phone1 = editTextPhone1.getText().toString().trim();
                phoneValidation(phone1);
            }
        });

        editTextPhone2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                phone2 = editTextPhone2.getText().toString().trim();
                if(phone2.isEmpty()){
                    textInput_phone2.setError(null);
                    btnConfirm.setEnabled(true);
                }
                else
                if(phone2.length() > 11 || phone2.length() < 11){
                    textInput_name.setErrorEnabled(true);
                    textInput_phone2.setError("Invalid Number");
                    btnConfirm.setEnabled(false);
                }
                else{
                    textInput_phone2.setError(null);
                    textInput_name.setErrorEnabled(false);
                    btnConfirm.setEnabled(true);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editTextName.getText().toString().trim();
                address = editTextAddress.getText().toString().trim();
                phone1 = editTextPhone1.getText().toString().trim();
                phone2 = editTextPhone2.getText().toString().trim();
                comment=editTextCom.getText().toString().trim();
                if( name.isEmpty() || address.isEmpty() || phone1.isEmpty()){
                    if (name.isEmpty()){
                        textInput_name.setErrorEnabled(true);
                        textInput_name.setError("Enter Your Name");
                    }
                    if(address.isEmpty()){
                        textInput_name.setErrorEnabled(true);
                        textInput_address.setError("Enter Your Address");
                    }
                    if(phone1.isEmpty()){
                        textInput_name.setErrorEnabled(true);
                        textInput_phone1.setError("Enter Contact Number");
                    }
                }
                else{
                    try {
                        btnConfirm.setEnabled(false);
                        ArrayList<String> codeList= AppController.getInstance().getProductCode();
                        ArrayList<String> quantityList=AppController.getInstance().getProductQuantity();
                        ArrayList<String> typeList=AppController.getInstance().getProductType();
                        Date curDate = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String date = format.format(curDate);
                        setCustomer(name,address,phone1,phone2,comment,date,codeList,quantityList,typeList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // on button click this code will run
                    //code to do
                }

            }
        });

        return v;
    }

    public void createView(View v){
        editTextName = (EditText) v.findViewById(R.id.editText_name);
        editTextAddress = (EditText) v.findViewById(R.id.editText_address);
        editTextPhone1 = (EditText) v.findViewById(R.id.editText_phoneNumber1);
        editTextPhone2 = (EditText) v.findViewById(R.id.editText_phoneNumber2);
        editTextCom=(EditText) v.findViewById(R.id.editText_comment);

        textInput_name = (TextInputLayout) v.findViewById(R.id.textInputName);
        textInput_address = (TextInputLayout) v.findViewById(R.id.textInput_address);
        textInput_phone1 = (TextInputLayout) v.findViewById(R.id.textInput_phoneNumber1);
        textInput_phone2 = (TextInputLayout) v.findViewById(R.id.textInput_phoneNumber2);
        textInputCom = (TextInputLayout) v.findViewById(R.id.textInput_comment);

        btnConfirm = (Button) v.findViewById(R.id.btn_confirm_order);
    }


    public void nameValidation(String name){

        if(name.isEmpty()){
            textInput_name.setErrorEnabled(true);
            textInput_name.setError("Enter Your Name");
            btnConfirm.setEnabled(false);
        }
        else if(!name.matches("[a-zA-Z ]+")){
            textInput_name.setErrorEnabled(true);
            textInput_name.setError("Enter Alphabets Only");
            btnConfirm.setEnabled(false);
        }
        else {
            textInput_name.setError(null);
            textInput_name.setErrorEnabled(false);
            btnConfirm.setEnabled(true);

        }
    }

    public void addressValidation(String address){

        if(address.isEmpty()){

            textInput_name.setErrorEnabled(true);
            textInput_address.setError("Enter Your Address");
            btnConfirm.setEnabled(false);
        }
        else {
            textInput_address.setError(null);
            textInput_name.setErrorEnabled(false);
            btnConfirm.setEnabled(true);

        }
    }

    public void phoneValidation(String phone){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(phone.isEmpty()){
            textInput_name.setErrorEnabled(true);
            textInput_phone1.setError("Enter Contact Number");
            btnConfirm.setEnabled(false);
        }
        else if(phone.length() > 11 || phone.length() < 11){
            textInput_name.setErrorEnabled(true);
            textInput_phone1.setError("Invalid Number");
            btnConfirm.setEnabled(false);
        }
        else{
            textInput_phone1.setError(null);
            textInput_name.setErrorEnabled(false);
            btnConfirm.setEnabled(true);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
    public void setCustomer(String name, final String address, String phone1,String phone2, String comment,String date, final ArrayList<String> productCodes,final ArrayList<String> productQuantity,final ArrayList<String> typeList) throws JSONException {

        String delivery=((AppController) getActivity().getApplication()).getDeliveryTotal();
        String netTotal=((AppController) getActivity().getApplication()).getNetTotal();
        String country=prefManager.getCountryName();
        String tag_string_req = "string_req";
        String url = "https://zainfabricspk.com/cadmin/mobapi/order.php";
        Order order = new Order(name, phone1,phone2, address, comment, date, productCodes,productQuantity,netTotal,delivery,country,typeList);
        final String JSONorder = order.toJSON();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    if(response.equals("true")){
                        if(prefManager.getCountryName().equals("United Kingdom")) {
                            Snackbar snackbar = Snackbar.make(getView(), "Thans for the order. Our Represntative Will Contact You Soon.", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            snackbar.setActionTextColor(Color.WHITE);
                            snackbar.show();
                        }
                        else{
                            Snackbar snackbar = Snackbar.make(getView(), "Order Successful. You will get it in 3-5 business days", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            snackbar.setActionTextColor(Color.WHITE);
                            snackbar.show();
                        }
                        AppController.getInstance().emptyArrayList();
                        Drawable drawable= getActivity().getResources().getDrawable(R.drawable.shopiing_cart_icon);
                        ActionMenuItemView menuItemView=getActivity().findViewById(R.id.menu_shopping_cart);
                        menuItemView.setIcon(drawable);
                        getCategory();

                    }
                    else{
                        Snackbar snackbar=Snackbar.make(getView(),"Something went wrong.Please try again.",Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("Login response: ", response.toString());


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("Error: ", error.getMessage());

                VolleyLog.d("Error: ", error.getMessage());

                if(error instanceof TimeoutError)
                    Toast.makeText(getActivity(), "TimeoutError", Toast.LENGTH_SHORT).show();

                else if(error instanceof ServerError)
                    Toast.makeText(getActivity(), "ServerError", Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return JSONorder.getBytes("utf-8");
                } catch (Exception uee) {
                    //VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

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
