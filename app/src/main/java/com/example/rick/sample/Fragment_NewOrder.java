package com.example.rick.sample;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_NewOrder extends Fragment {


    private RecyclerView recyclerView;
    private ImageView imageButton_Search;
    private FloatingActionButton buttonAdd;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private RecyclerView listView_ChooseProduct;
    private EditText editText_ProductCode;
    private EditText editText_ProductQuantity;
    private TextView editText_ProductCodeHint;
    private SalesOrder salesOrder;
    private CoordinatorLayout coordinatorLayout;

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
        progressDialog = new ProgressDialog(getActivity());
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        coordinatorLayout = (CoordinatorLayout) getView().findViewById(R.id.layout_neworder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Uploading order...");
                progressDialog.show();
                postOrder();
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


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

                final ProductDataAdapter adapter = new ProductDataAdapter(getActivity(),getProducts());
                listView_ChooseProduct.setAdapter(adapter);

                adapter.setOnItemClickListener(new ProductDataAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {

                        Product product = adapter.getProduct(position);

                        Log.d("SQL","p: "+product.product_id + " pos" + position);


                        editText_ProductCode.setText(product.product_id);
                        editText_ProductCodeHint.setText(product.description);
                        dialog.hide();
                    }
                });
                //populateChooseProductListView();



            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToOrder();
            }
        });

    }

    private void postOrder() {
        Api.postOrders(new Api.apiCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result.has("message"))
                {

                    try {
                            showSnackBarMessage(result.get("message").toString());
                            progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(String error) {
                showSnackBarMessage(error);
                progressDialog.dismiss();
            }
        });
    }

    public void populate()
    {
        editText_ProductCode = (EditText) getView().findViewById(R.id.CustomerOrder_editProduct);
        editText_ProductQuantity = (EditText) getView().findViewById(R.id.CustomerOrder_editProductQuantity);
        editText_ProductCodeHint = (TextView) getView().findViewById(R.id.CustomerOrder_txtProductHint);
        recyclerView = (RecyclerView)getView().findViewById(R.id.neworder_invoicedetails);
        buttonAdd = (FloatingActionButton) getView().findViewById(R.id.neworder_add);
        //recyclerView.setHasFixedSize(true);

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
        return Product.listAll(Product.class);
    }

    private void addProductToOrder()
    {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.product = editText_ProductCode.getText().toString();
        orderDetail.quantity = editText_ProductQuantity.getText().toString();
        orderDetail.order_id = getSalesOrder();
        orderDetail.save();
        populateLineItems();

    }

    private SalesOrder getSalesOrder() {
        if (salesOrder == null)
        {
            SalesOrder order = new SalesOrder();
            order.generateOrderNumber();
            order.user_id = "1";
            order.sync_id = false;
            order.customer_id = SalesOrder.TEMP_CUSTOMER.getCustomerID();
            order.order_status_id = 1;
            order.order_date = new Date();
            order.save();
            this.salesOrder = order;

        }

        return this.salesOrder;
    }

    public void getCurrentDate()
    {
        Date today = new Date();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    private Long getOrderId()
    {
        SalesOrder order = this.getSalesOrder();
        return order.getId();
    }

    private void populateLineItems()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        OrderDetailDataAdapter adapter = new OrderDetailDataAdapter(getActivity(),this.getSalesOrder().getLineItems());
        recyclerView.setAdapter(adapter);

    }

    private void initialiseOrder()
    {
        this.salesOrder = new SalesOrder();
        this.salesOrder.generateOrderNumber();
    }

    private void showSnackBarMessage(String message) {
        progressDialog.dismiss();
        Snackbar snackbar = Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showSnackBarMessageWithValidationErrors(String message,JSONArray array) {
        progressDialog.dismiss();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("title");
        alertDialog.setMessage(array.toString());
        Snackbar snackbar = Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_LONG)
                .setAction("SHOW", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
        snackbar.show();
    }



}
