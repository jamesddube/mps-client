package com.example.rick.sample;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 6/5/16.
 */
public class GsonParser {

    public static List<Customer> parseCustomers(JSONArray customerArray)
    {
        List<Customer> customerList = new ArrayList<>();
        try {

            for(int i= 0; i < customerArray.length();i++)
            {
                Gson gson = new Gson();
                JSONObject object = customerArray.getJSONObject(i);
                customerList.add(gson.fromJson(object.toString(), Customer.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return customerList;
    }

    public static List<Product> parseProducts(JSONArray productArray)
    {
        List<Product> productList = new ArrayList<>();
        try {

            for(int i= 0; i < productArray.length();i++)
            {
                Gson gson = new Gson();
                JSONObject object = productArray.getJSONObject(i);
                productList.add(gson.fromJson(object.toString(), Product.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productList;
    }

    public static JSONArray getSerializedOrders()
    {
        JSONArray serialised = null;
        List<SalesOrder> salesOrderList  = SalesOrder.getUnprocessedOrders();

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(salesOrderList, new TypeToken<List<Customer>>() {}.getType());

        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            try {
                serialised = new JSONArray(jsonArray.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return serialised;


    }


}
