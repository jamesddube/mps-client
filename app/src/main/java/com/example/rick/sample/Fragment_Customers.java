package com.example.rick.sample;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 5/27/16.
 */
public class Fragment_Customers extends Fragment {

    RecyclerView recyclerView;

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

        CustomerDataAdapter adapter = new CustomerDataAdapter(getActivity(),getData());
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
        List<Customer> customers = new ArrayList<>();
        String[] name = {"Spar Mandara","OK Supermarket","TM Supermarket","DODS Bakery","Carvary Restaurant","Tony's Coffee Shop","Mudiro Butchery","Valahala","Muunze Bar","jh","limo","uii"};
        for (String aName : name) {
            Customer customer = new Customer();
            customer.setName(aName);
            customers.add(customer);
        }

        return customers;

    }

    public void newOrder()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Fragment_NewOrder.newInstance())
                .commit();
    }
}
