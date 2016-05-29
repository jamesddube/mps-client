package com.example.rick.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by rick on 5/27/16.
 */
public class Fragment_Customers extends Fragment {

    private ArrayList<String> countries;

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


    }

    public void boot()
    {
        RecyclerView recyclerView = (RecyclerView)getView().findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        countries = new ArrayList<>();
        countries.add("Australia");
        countries.add("India");
        countries.add("United States of America");
        countries.add("Germany");
        countries.add("Russia");
        RecyclerView.Adapter adapter = new CustomerDataAdapter(countries);
        recyclerView.setAdapter(adapter);
    }
}
