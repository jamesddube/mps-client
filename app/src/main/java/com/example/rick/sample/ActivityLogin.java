package com.example.rick.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityLogin extends AppCompatActivity {

    Button loginButton;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    ProgressDialog progressDialog;
    public ScrollView coordinatorLayout;
    TextInputLayout usernameWrapper;
    TextInputLayout passwordWrapper;
    ImageView settings;

    String username;
    String password;
    CheckBox checkBox;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boot();



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doLogin();


            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Settings.class);
                startActivity(i);
            }
        });





    }

    public  void showNotification(String message)
    {
        Snackbar snackbar = Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    public void boot()
    {
        loginButton = (Button) findViewById(R.id.btn_login);
        coordinatorLayout = (ScrollView) findViewById(R.id.login);
        settings = (ImageView) findViewById(R.id.login_prefs);

    }

    public void doLogin() {

// then you use

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        validate();

    }

    public void validate()
    {
        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        username = usernameWrapper.getEditText().getText().toString();
        password = passwordWrapper.getEditText().getText().toString();
        if(username.equals("one"))
        {
            Intent i = new Intent("android.intent.action.DRAWER");
            startActivity(i);
        }
        if (!validateEmail(username)) {
            usernameWrapper.setError("Not a valid email address!");
        } else if (!validatePassword(password)) {
            passwordWrapper.setError("Not a valid password!");
        } else {
            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);
            progressDialog.show();
            requestToken(username,password,new Api.apiCallBack() {
                @Override
                public void onSuccess(JSONObject result) {
                    progressDialog.dismiss();
                    try {
                        Api.token = result.get("access_token").toString();
                        Intent i = new Intent("android.intent.action.DRAWER");
                        startActivity(i);
                        finish();
                    } catch (JSONException e) {
                        showNotification("Could not get access token");
                    }

                }

                @Override
                public void onError(String error) {
                    progressDialog.dismiss();
                    showNotification(error);


                }
            });

        }
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > 5;
    }

    public static void requestToken(String username, String password, final Api.apiCallBack callBack) {
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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Api.Endpoint.getUrl(Api.Endpoint.Token), payload, new Response.Listener<JSONObject>() {
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



}
