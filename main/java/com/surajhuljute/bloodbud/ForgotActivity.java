package com.surajhuljute.bloodbud;


import android.app.ProgressDialog;
import android.content.Intent;
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

import static com.surajhuljute.bloodbud.R.id.sendPassword;


public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText forgotEmail;
    private Button sendpassword;
    private TextView loginHere;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        loginHere = (TextView) findViewById(R.id.loginHere);
        forgotEmail = (EditText) findViewById(R.id.forgotEmail);

        sendpassword = (Button) findViewById(R.id.sendPassword);

        sendpassword.setOnClickListener(this);



        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotActivity.this, ActivityLogin.class);
                startActivity(intent);
            }
        });
    }

    private void forgot() {
        pd = ProgressDialog.show(ForgotActivity.this, null, "Please Wait", true, true);
        final String email = forgotEmail.getText().toString().trim();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FORGOT_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        //If we are getting success from server
                        if (response.equalsIgnoreCase(Config.SUCCESS)) {
                            //Starting profile activity
                            Intent intent = new Intent(ForgotActivity.this, ActivityLogin.class);
                            intent.putExtra("FORGOT_MSG", "New Password is sent to your Email-ID");
                            startActivity(intent);

                        } else {
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(ForgotActivity.this, response, Toast.LENGTH_LONG).show();
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
        //Calling the forgot function

        if (v == sendpassword) {
            forgot();
        }

    }

 /*public void sendMobNo() {
        pd = ProgressDialog.show(ForgotActivity.this, null, "Please Wait ", true, true);
        final String mobno = forgotmobno.getText().toString().trim();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FORGOT_URL_MOBNO,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        //If we are getting success from server
                        if (response.equalsIgnoreCase(Config.SUCCESS)) {
                            //Starting profile activity
                            Intent intent = new Intent(ForgotActivity.this, ActivityLogin.class);
                            intent.putExtra("FORGOT_MSG", "New Password is sent to your Mobno");
                            startActivity(intent);

                        } else {
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(ForgotActivity.this, response, Toast.LENGTH_LONG).show();
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
                params.put(Config.KEY_MOBNO, mobno);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }*/

}
