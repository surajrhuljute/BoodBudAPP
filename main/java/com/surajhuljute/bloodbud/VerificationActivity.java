 package com.surajhuljute.bloodbud;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
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

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonVerify;
    private RequestQueue requestQueue;
    private String email;
    private EditText vCode;
    private Button buttonResend;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");

        buttonResend = (Button) findViewById(R.id.buttonResend);

        //Initializing the RequestQueue
        requestQueue = Volley.newRequestQueue(this);
        try {
            confirmOTP();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    Boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit",
                Toast.LENGTH_SHORT).show();

    }

    //This method would confirm the otp
    private void confirmOTP() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.activity_verification, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonVerify = (AppCompatButton) confirmDialog.findViewById(R.id.buttonVerify);
        vCode = (EditText) confirmDialog.findViewById(R.id.vCode);

        buttonResend = (AppCompatButton) confirmDialog.findViewById(R.id.buttonResend);
        buttonResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(VerificationActivity.this, "Account verified successfully", Toast.LENGTH_LONG).show();
                sendOTP(email);
            }
        });
        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //alert.setCancelable(false);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        //Displaying the alert dialog
        alertDialog.show();

        //On the click of the confirm button from alert dialog
        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
                final ProgressDialog loading = ProgressDialog.show(VerificationActivity.this, "Authenticating", "Please wait while we check the entered code", false, false);

                //Getting the user entered otp from edittext
                final String otp = vCode.getText().toString().trim();

                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONFIRM_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //dismissing the progressbar
                                loading.dismiss();
                                //if the server response is success
                                if (response.equalsIgnoreCase("success")) {
                                    //removing is_verified from SP
                                    SharedPreferences sharedPreferences = VerificationActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.remove(Config.ISVERIFIED_SHARED_PREF);
                                    editor.apply();

                                    Toast.makeText(VerificationActivity.this, "Account verified successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(VerificationActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                } else {
                                    //Displaying a toast if the otp entered is wrong
                                    Toast.makeText(VerificationActivity.this, "Wrong OTP Please Try Again", Toast.LENGTH_LONG).show();
                                    try {
                                        //Asking user to enter otp again
                                        confirmOTP();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
                                Toast.makeText(VerificationActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        params.put(Config.KEY_OTP, otp);
                        params.put(Config.KEY_EMAIL, email);
                        return params;
                    }
                };
                //Adding the request to the queue
                requestQueue.add(stringRequest);
            }
        });
    }

    private void sendOTP(String email) {

        class RegisterUser extends AsyncTask<String, Void, String> {
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = ProgressDialog.show(VerificationActivity.this, null, "Please Wait", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pd.dismiss();
                try {
                    if (s.equalsIgnoreCase("success")) {
                        confirmOTP();
                        Toast.makeText(getApplicationContext(), "OTP Send Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("email", params[0]);

                String result = ruc.sendPostRequest(Config.RESEND_OTP_URL, data);

                return result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(email);
    }
    @Override
    public void onClick(View v) {

    }


}
