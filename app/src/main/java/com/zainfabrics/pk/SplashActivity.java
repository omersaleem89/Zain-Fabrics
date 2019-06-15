package com.zainfabrics.pk;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.zainfabrics.pk.fragments.models.ImageListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by M Umer Saleem on 19-Nov-17.
 */


public class SplashActivity extends AppCompatActivity {
    ArrayList<ImageListModel> categoryModelArrayList = new ArrayList<>();
    ProgressBar progressBar;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{
            View decorView = getWindow().getDecorView();
// Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        progressBar = (ProgressBar) findViewById(R.id.progressbarSplash);
        prefManager=new PrefManager(this);



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
                        startActivity(new Intent(SplashActivity.this, MainActivity.class)
                                .putExtra("categoryModelArrayList", categoryModelArrayList));
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        String errorMessage = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SplashActivity.this, "TimeoutError", Toast.LENGTH_SHORT).show();

                else if(error instanceof ServerError)
                    Toast.makeText(SplashActivity.this, "ServerError", Toast.LENGTH_SHORT).show();


                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }

    @Override
    protected void onResume() {
        super.onResume();
                if(isNetworkAvailable()==true) {
                    progressBar.setVisibility(View.VISIBLE);
                    getCategory();
                }
                else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.fm_splah), "No Internet Connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}