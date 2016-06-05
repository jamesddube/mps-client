package com.example.rick.sample;

import android.app.ProgressDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivitySync extends AppCompatActivity {
    FloatingActionButton button;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;

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
                            GsonParser.parseCustomers(result.getJSONArray("customers"));
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


}
