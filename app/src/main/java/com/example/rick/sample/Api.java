package com.example.rick.sample;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 5/30/16.
 */
public class Api {

    static String token;

    public static void requestToken(String username, String password, final apiCallBack callBack) {
        JSONObject payload = new JSONObject();
        String grant_type = "password";

        try {
            payload.put("username", username);
            payload.put("password", password);
            payload.put("grant_type", grant_type);
            payload.put("client_id", Client.getClient_id());
            payload.put("client_secret", Client.getClient_secret());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final RequestQueue queue = Volley.newRequestQueue(App.getInstance());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoint.getUrl(Endpoint.Token), payload, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                callBack.onSuccess(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = "An error occurred";
                try {

                    String response = new String(error.networkResponse.data);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("error_description")) {

                        message = jsonObject.get("error_description").toString();
                    }


                    Log.d("API", response);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


                callBack.onError(message);
            }
        });

        queue.add(request);

    }

    public static void getCustomers(final apiCallBack callBack)
    {
        JSONObject payload = new JSONObject();
        String url = Endpoint.getUrl(null)+Endpoint.Customers + "?access_token="+token;
        final RequestQueue queue = Volley.newRequestQueue(App.getInstance());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,url,payload , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(Api.parseError(error));
            }
        });

        queue.add(objectRequest);


    }

    public static  void hardSync(final Api.apiCallBack apiCallBack)
    {

        RequestQueue queue = Volley.newRequestQueue(App.getInstance());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Api.Endpoint.getUrl("sync"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        apiCallBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiCallBack.onError(parseError(error));
            }
        });

        queue.add(objectRequest);
    }

    public static void postOrders(final apiCallBack apiCallBack)
    {
        try {
            JSONObject payload = new JSONObject();
            JSONObject orders = new JSONObject();
            JSONArray serialised = GsonParser.getSerializedOrders();
            orders.put("orders",serialised);
            Log.d("JSON",orders.toString());
            //orders.getJSONArray("orders");

            RequestQueue queue = Volley.newRequestQueue(App.getInstance());
            JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.POST,
                    Api.Endpoint.getUrl("sample"),
                    orders,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            apiCallBack.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            apiCallBack.onError(parseError(error));
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            queue.add(orderRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public static String parseResponse()
    {
        return null;
    }

    public static String parseError(VolleyError error)
    {
        String response = "Could not connect to the Server";
        try {
            String string = new String(error.networkResponse.data);


            JSONObject object = new JSONObject(string);

            if (object.has("message")) {
                response = object.get("message").toString();
            }
        }catch (JSONException e)
        {
            response = "Could not parse response";
        }
        catch (NullPointerException e)
        {
            return response;
        }
        return response;
    }

    public interface apiCallBack {
        void onSuccess(JSONObject result);

        void onError(String error);
    }


    public static abstract class Endpoint {
        public static String Customers = "customers";
        public static String Users = "users";
        public static String Products = "products";
        public static String Token = "oauth/token";
        public static String Orders = "sample";

        @Nullable
        public static String getUrl(String url) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
            String domain = prefs.getString("server_url", "10.0.2.2");
            return "http://" + domain + "/mps/public/api/" + url;
        }
    }

    public static  void sync(final apiCallBack apiCallBack)
    {

    }
}
