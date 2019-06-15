package com.zainfabrics.pk;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;
import com.zainfabrics.pk.fragments.adapters.CartAdapter;
import com.zainfabrics.pk.fragments.models.ProductModel;

import java.util.ArrayList;

/**
 * Created by M Umer Saleem on 19-Nov-17.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    final static private ArrayList<ProductModel> arrayList=new ArrayList<>();

    static private int subTotal =0;
    static private int deliveryTotal =0;
    static private int netTotal =0;
    static private int netWeight =0;
    static private int stitchingPrice =0;

    private static AppController mInstance;
    PrefManager prefManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        prefManager=new PrefManager(this);
    }

    public ArrayList<ProductModel> getArrayList() {
        return arrayList;
    }
    public boolean checkProduct(ProductModel model){
        for (ProductModel model1:arrayList) {
                if(model1.getCode().equals(model.getCode()))
                    return false;
        }
        return true;
    }

    public void setArrayList(ProductModel model) {
        arrayList.add(model);
        subTotal +=(Integer.parseInt(model.getSalePrice())*Integer.parseInt(model.getCartQuantity()));
        netWeight +=(Integer.parseInt(model.getWeight())*Integer.parseInt(model.getCartQuantity()));
        if (model.getType().equals("Stitched"))
        {
            stitchingPrice = stitchingPrice +(500*Integer.parseInt(model.getCartQuantity()));
        }
        setDeliveryTotal();
        netTotal=deliveryTotal+subTotal+ stitchingPrice;

    }
    public void setDeliveryTotal(){
       if(prefManager.getCountryName().equals("United Kingdom")) {
           if(netWeight==0)
           {
               deliveryTotal =0;
           }
           else if(netWeight<1000)
           {
               deliveryTotal =2635;
           }
           else if (netWeight<2000){
               deliveryTotal =3035;
           }
           else if (netWeight<3000){
               deliveryTotal =3435;
           }
           else if (netWeight<4000){
               deliveryTotal =3835;
           }
           else if (netWeight<5000){
               deliveryTotal =4235;
           }
           else if (netWeight<6000){
               deliveryTotal =4635;
           }
           else if (netWeight<7000){
               deliveryTotal =5035;
           }
           else if (netWeight<8000){
               deliveryTotal =5435;
           }
           else if (netWeight<9000){
               deliveryTotal =5835;
           }
           else {
               deliveryTotal =6235;
           }
       }
       else{
            int q=getProductsQuantity();
           if(q==0)
           {
               deliveryTotal =0;
           }
            else if(q==1){
                deliveryTotal=150;
            }
            else {
                int rslt = (q-1) * 50;
                deliveryTotal=rslt+150;
            }
       }

}

    public void emptyArrayList() {
        arrayList.removeAll(arrayList);
        subTotal =0;
        deliveryTotal=0;
        netWeight=0;
        netTotal=0;
        stitchingPrice =0;
    }
    public String getDeliveryTotal(){
        return String.valueOf(deliveryTotal);
    }
    public String getSubTotal(){
        return String.valueOf(subTotal);
    }
    public void setNetTotal(){
         netTotal=subTotal+deliveryTotal+ stitchingPrice;
    }
    public String getNetTotal(){
        return String.valueOf(netTotal);
    }
    public String getStitching(){
        return String.valueOf(stitchingPrice);
    }
    public int getIntStitching(){
        return stitchingPrice;
    }
    public ArrayList<String> getProductCode(){
        ArrayList<String> codeList=new ArrayList<>();
        for(int i=0;i<arrayList.size();i++) {
            codeList.add(arrayList.get(i).getCode());
        }
        return codeList;
    }
    public ArrayList<String> getProductType(){
        ArrayList<String> typeList=new ArrayList<>();
        for(int i=0;i<arrayList.size();i++) {
            typeList.add(arrayList.get(i).getType());
        }
        return typeList;
    }
    public int getProductsQuantity(){
        int quantity=0;
        for(int i=0;i<arrayList.size();i++) {
            quantity +=Integer.parseInt(arrayList.get(i).getCartQuantity());
        }
        return quantity;
    }
    public ArrayList<String> getProductQuantity(){
        ArrayList<String> codeList=new ArrayList<>();
        for(int i=0;i<arrayList.size();i++) {
            codeList.add(arrayList.get(i).getCartQuantity());
        }
        return codeList;
    }

    public void removeItem(CartAdapter adapter, int pos, String delPrice,String delWeight,String delCartQuantity){
        subTotal -=(Integer.parseInt(delPrice)*Integer.parseInt(delCartQuantity));
        netWeight -=(Integer.parseInt(delWeight)*Integer.parseInt(delCartQuantity));
        if (arrayList.get(pos).getType().equals("Stitched"))
        {
            stitchingPrice = stitchingPrice -(500*Integer.parseInt(arrayList.get(pos).getCartQuantity()));
        }
        arrayList.remove(pos);
        setDeliveryTotal();
        setNetTotal();
        adapter.notifyDataSetChanged();
    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }



    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}