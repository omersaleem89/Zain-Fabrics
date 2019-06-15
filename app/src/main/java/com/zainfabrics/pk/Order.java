package com.zainfabrics.pk;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by M Umer Saleem on 10/15/2017.
 */

public class Order {
    String Name;
    String Phone1;
    String Phone2;
    String comment;
    String netTotal;
    String delievery;
    String country;
    String Address;
    String Date;
    ArrayList<String> type;
    ArrayList<String> Products_codes;
    ArrayList<String> productQuantity;

    public Order()
    {
        Name = Phone1 = Address = Date =netTotal=comment=delievery=country= "";
        Products_codes = new ArrayList<>();
        productQuantity=new ArrayList<>();
    }

    public Order(String name, String phone1, String phone2,String address, String comment, String date, ArrayList<String> productCodes,ArrayList<String> productQuantity,String netTotal,String delievery,String country, ArrayList<String> type)
    {
        Name = name;
        Phone1 = phone1;
        Phone2 = phone2;
        Address = address;
        Date = date;
        this.comment=comment;
        this.netTotal=netTotal;
        this.delievery=delievery;
        this.country=country;
        Products_codes = new ArrayList<>(productCodes);
        this.productQuantity=new ArrayList<>(productQuantity);
        this.type=new ArrayList<>(type);
    }

    public String toJSON() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("Name", Name);
        object.put("Phone1", Phone1);
        object.put("Phone2", Phone2);
        object.put("Address", Address);
        object.put("comment", comment);
        object.put("netTotal", netTotal);
        object.put("delievery", delievery);
        object.put("country", country);
        object.put("Date", Date);
        JSONArray ids = new JSONArray(Products_codes);
        JSONArray quantity=new JSONArray(productQuantity);
        JSONArray types=new JSONArray(type);
        object.put("productQuantity",quantity);
        object.put("Product_codes",ids);
        object.put("types",types);
        return object.toString();
    }
}
