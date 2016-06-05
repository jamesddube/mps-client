package com.example.rick.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 5/27/16.
 */
public class Fragment_Customers extends Fragment {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            progressDialog.dismiss();
        }
    };

    public Fragment_Customers() {
        // Required empty public constructor
    }

    public static Fragment_Customers newInstance() {
        return new Fragment_Customers();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customers, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        boot();

    }

    public void boot()
    {
        recyclerView = (RecyclerView)getView().findViewById(R.id.rv);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        if(9==9)//Customer.getAll().size() )
        {
            populate(Customer.getAll());
        }
        else
        {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching Customers...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            Api.getCustomers(new Api.apiCallBack() {
                @Override
                public void onSuccess(JSONObject result) {


                    Log.d("API",result.toString());
                    List<Customer> customers = new ArrayList<>();

                    JSONArray array = null;
                    try {
                        array = result.getJSONArray("customers");


                    for(int i = 0; i< array.length();i++)
                    {
                        try{
                        customers.add(new Customer(
                            array.getJSONObject(i).getString("name"),
                        array.getJSONObject(i).getInt("vat_number"),
                        array.getJSONObject(i).getString("address"),
                        array.getJSONObject(i).getString("telephone"),
                        array.getJSONObject(i).getString("fax"),
                        array.getJSONObject(i).getString("email"),
                        array.getJSONObject(i).getString("city"),
                        "up"));
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    SugarRecord.saveInTx(customers);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.setMessage("Updating Customers...");
                    populate(Customer.getAll());
                    progressDialog.dismiss();
                }

                @Override
                public void onError(String error) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                }
            });


        }




    }

    public void populate(List<Customer> customers)
    {

            CustomerDataAdapter adapter = new CustomerDataAdapter(getActivity(),customers);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new CustomerDataAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Toast.makeText(getActivity(),String.valueOf(position),Toast.LENGTH_LONG).show();
                    newOrder();
                }

            });

    }

    public static List<Customer> getData()
    {
        return Customer.getAll();

    }

    public void newOrder()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Fragment_NewOrder.newInstance())
                .commit();
    }

    public void fetchCustomers()
    {
        Api.getCustomers(new Api.apiCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("API",result.toString());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
            }
        });
    }
}

