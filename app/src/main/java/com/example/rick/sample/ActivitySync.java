package com.example.rick.sample;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ActivitySync extends AppCompatActivity {
    FloatingActionButton button;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        getFragmentManager().beginTransaction().replace(R.id.fragment_sync, new Settings.SyncFragment()).commit();
        progressDialog = new ProgressDialog(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layout_sync);


        button = (FloatingActionButton) findViewById(R.id.btn_sync);
        progressDialog.setIndeterminate(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSyncMessage("Synchronising...");
                progressDialog.show();
                Api.hardSync(new Api.apiCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {



                        showSyncMessage("Parsing results...");

                        try {

                            List<Customer> customers = GsonParser.parseCustomers(result.getJSONArray("customers"));

                            Thread thread = new Thread(new saveCustomers(customers));
                            thread.start();
                            handler = new Handler(){
                                @Override
                                public void handleMessage(Message msg) {

                                }
                            };
                            List<Product> products = GsonParser.parseProducts(result.getJSONArray("products"));
                            Thread thread2 = new Thread(new saveProducts(products));
                            thread2.start();

                        } catch (JSONException e) {
                            showSnackBarMessage("Parsing error");
                        }

                        showSnackBarMessage("Sync completed");
                    }

                    @Override
                    public void onError(String error) {
                       showSnackBarMessage(error);
                        progressDialog.dismiss();

                    }
                });
            }
        });


    }

    private void showSnackBarMessage(String message) {
        progressDialog.dismiss();
        Snackbar snackbar = Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showSyncMessage(String message) {

        progressDialog.setMessage(message);

    }


        class saveCustomers implements Runnable
        {
            saveCustomers(List<Customer> customers){
                this.customers = customers;
            }
            List<Customer> customers;
            Message m = Message.obtain();
            @Override
            public void run() {

                SugarRecord.saveInTx(customers);
                Log.d("THREADS","after");
                handler.sendMessage(m);
            }


        }

    class saveProducts implements Runnable
    {
        saveProducts(List<Product> products){
            this.products = products;
        }
        List<Product> products;
        Message m = Message.obtain();
        @Override
        public void run() {

            SugarRecord.saveInTx(products);
            Log.d("THREADS","after");
            handler.sendMessage(m);
        }


    }



}
