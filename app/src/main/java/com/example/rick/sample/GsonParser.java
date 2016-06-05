package com.example.rick.sample;

import com.google.gson.Gson;
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
            SugarRecord.saveInTx(customerList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return customerList;
    }
}
