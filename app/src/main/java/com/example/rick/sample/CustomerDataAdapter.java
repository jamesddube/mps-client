package com.example.rick.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 5/27/16.
 */
public class CustomerDataAdapter extends RecyclerView.Adapter<CustomerDataAdapter.ViewHolder> {
    private List<Customer> customers;
    private LayoutInflater inflator;
    private static ClickListener clickListener;

    public CustomerDataAdapter(Context context,List<Customer> customers) {
        this.customers = customers;
        inflator = LayoutInflater.from(context);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = inflator.inflate(R.layout.template_recycler_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerDataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.customerName.setText(customers.get(i).getName());
       // viewHolder.imageView.setImageResource(R.drawable.ic_dashboard);
    }

    public Customer getCustomer(int position) {

        return this.customers.get(position);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView customerName;
        ImageView imageView;
        public ViewHolder(View view) {
            super(view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            customerName = (TextView)view.findViewById(R.id.customer_name);
            imageView = (ImageView) view.findViewById(R.id.customer_logo);


        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
    public void setOnItemClickListener(ClickListener clickListener) {
       CustomerDataAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

    }


}
