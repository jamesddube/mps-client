package com.example.rick.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rick on 5/27/16.
 */
public class CustomerDataAdapter extends RecyclerView.Adapter<CustomerDataAdapter.ViewHolder> {
    private ArrayList<String> countries;

    public CustomerDataAdapter(ArrayList<String> countries) {
        this.countries = countries;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CustomerDataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_country.setText(countries.get(i));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_country;
        public ViewHolder(View view) {
            super(view);

            //tv_country = (TextView)view.findViewById(R.id.tv_country);
        }
    }
}
