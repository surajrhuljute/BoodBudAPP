package com.surajhuljute.bloodbud;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView btnForgot;
    private TextView btnRegister;
    private TextView tv_msg;
    ProgressDialog pd;

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;
    private int is_verified = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_login);

        //Initializing views
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        //displaying forgot msg sent successfully
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        String forgot_msg = getIntent().getStringExtra("FORGOT_MSG");

        tv_msg.setText(forgot_msg);

        btnRegister = (TextView) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnForgot = (TextView) findViewById(R.id.btnForgot);

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ForgotActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String email = bundle.getString("email");
            String password = bundle.getString("password");
            Toast.makeText(ActivityLogin.this, email, Toast.LENGTH_LONG).show();
            editTextEmail.setText(email);
            editTextPassword.setText(password);
            login();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value from sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        is_verified = sharedPreferences.getInt(Config.ISVERIFIED_SHARED_PREF, 0);

        //If we will get true
        if (loggedIn) {
            if (is_verified == 1) {
                //We will start the Profile Activity
                Intent intent = new Intent(ActivityLogin.this, VerificationActivity.class);
                startActivity(intent);
            } else {
                //We will start the Profile Activity
                Intent intent = new Intent(ActivityLogin.this, ProfileActivity.class);
                startActivity(intent);
            }
        }
    }

    private void login() {
        final ProgressDialog pd = ProgressDialog.show(ActivityLogin.this, null, "Please wait ..", false, false);
        //Getting values from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("unverified") || response.equalsIgnoreCase(Config.SUCCESS)) {
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = ActivityLogin.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);

                            if (response.equalsIgnoreCase("unverified")) {

                                editor.putString(Config.EMAIL_SHARED_PREF, email);
                                editor.putInt(Config.ISVERIFIED_SHARED_PREF, 1);
                                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                                //Saving values to editor
                                editor.commit();

                                //Starting profile activity
                                Intent intent = new Intent(ActivityLogin.this, VerificationActivity.class);
                                startActivity(intent);
                            }

                            if (response.equalsIgnoreCase(Config.SUCCESS)) {

                                editor.putString(Config.EMAIL_SHARED_PREF, email);
                                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);

                                //Saving values to editor
                                editor.commit();

                                //Starting profile activity
                                Intent intent = new Intent(ActivityLogin.this, ProfileActivity.class);
                                startActivity(intent);
                            }
                        }

                        //If we are getting success from server
                        else {
                            //If the server response is not success
                            //Displaying an error message on toast
                            pd.dismiss();
                            Toast.makeText(ActivityLogin.this, response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_EMAIL, email);
                params.put(Config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        //Calling the login function
        login();

    }
}