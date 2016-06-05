package com.example.rick.sample;


import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_NewOrder extends Fragment {


    private RecyclerView recyclerView;
    private ImageView imageButton_Search;
    private Dialog dialog;
    private RecyclerView listView_ChooseProduct;
    private EditText editText_ProductCode;

    public Fragment_NewOrder() {
        // Required empty public constructor
    }

    public static Fragment_NewOrder newInstance() {
        return new Fragment_NewOrder();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_neworder, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageButton_Search =(ImageView) getView().findViewById(R.id.CustomerOrder_imgSearch);

        populate();

        imageButton_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.template_choose_product_dialog);
                dialog.setTitle("Chose Product");
                dialog.show();
                listView_ChooseProduct = (RecyclerView) dialog.findViewById(R.id.RV_ChooseProduct);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                listView_ChooseProduct.setLayoutManager(layoutManager);

                ProductDataAdapter adapter = new ProductDataAdapter(getActivity(),getProducts());
                listView_ChooseProduct.setAdapter(adapter);

                adapter.setOnItemClickListener(new ProductDataAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {

                        editText_ProductCode.setText(String.valueOf(position));
                        dialog.hide();
                    }
                });
                //populateChooseProductListView();



            }
        });

    }

    public void populate()
    {
        editText_ProductCode = (EditText) getView().findViewById(R.id.CustomerOrder_editProduct);
        recyclerView = (RecyclerView)getView().findViewById(R.id.neworder_invoicedetails);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

       OrderDetailDataAdapter adapter = new OrderDetailDataAdapter(getActivity(),getData());
        recyclerView.setAdapter(adapter);
    }

    public static List<OrderDetail> getData()
    {
        List<OrderDetail> lineItems = new ArrayList<>();
        String[] name = {"Spar Mandara","OK Supermarket","TM Supermarket","DODS Bakery","Carvary Restaurant","Tony's Coffee Shop","Mudiro Butchery","Valahala","Muunze Bar","jh","limo","uii"};
        for (String aName : name) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.product = aName;
            orderDetail.quantity="8";
            lineItems.add(orderDetail);

        }

        return lineItems;

    }

    private void populateChooseProductListView()
    {

        recyclerView = (RecyclerView)getView().findViewById(R.id.RV_ChooseProduct);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ProductDataAdapter adapter = new ProductDataAdapter(getActivity(),getProducts());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ProductDataAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Snackbar.make(v,position,Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private List<Product> getProducts() {
        List<Product> lineItems = new ArrayList<>();
        String[] name = {"Spar Mandara","OK Supermarket","TM Supermarket","DODS Bakery","Carvary Restaurant","Tony's Coffee Shop","Mudiro Butchery","Valahala","Muunze Bar","jh","limo","uii"};
        for (String aName : name) {
            Product product = new Product();
            product.product = aName;
            product.quantity="8";
            lineItems.add(product);

        }

        return lineItems;

    }

    private void save()
    {
        
    }


}
