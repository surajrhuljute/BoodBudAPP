package com.surajhuljute.bloodbud;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, vCode,userState,userCity;

    private EditText editTextMobileNo, editTextpermanentAddress;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private Button newBtnRegister, buttonVerify, buttonResend;
    private TextView button_login;
    private RequestQueue requestQueue;
    private Spinner userBloodgroup,spinner_date,spinner_month,spinner_year;
    ProgressDialog pd;
    RadioButton rb1, rb2;
    RadioGroup rg1;
    private String email, password, mobileno;
    ArrayList<String> listState = new ArrayList<String>();
    // for listing all cities
    ArrayList<String> listCity = new ArrayList<String>();
    // access all auto complete text views
    AutoCompleteTextView act;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userBloodgroup =(Spinner) findViewById(R.id.spinner_bloodgroup);
        spinner_date=(Spinner) findViewById(R.id.spinner_date);
        spinner_month=(Spinner) findViewById(R.id.spinner_date);
        spinner_year=(Spinner) findViewById(R.id.spinner_year);
        userCity=(EditText) findViewById(R.id.actCity);
        userState=(EditText) findViewById(R.id.actState);



        rb1 = (RadioButton) findViewById(R.id.rb_male);
        rb2 = (RadioButton) findViewById(R.id.rb_female);
        rg1 = (RadioGroup) findViewById(R.id.radioGroup);
        editTextMobileNo = (EditText) findViewById(R.id.editTextMobileNo);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);


        editTextpermanentAddress = (EditText) findViewById(R.id.editTextPermanentAddress);


        newBtnRegister = (Button) findViewById(R.id.newBtnRegister);
        newBtnRegister.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(this);

        button_login = (TextView) findViewById(R.id.buttonLogin);
        addItemsOnSpinner1();
        addItemsOnSpinner3();

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
                startActivity(intent);
            }

        });

        callAll();
    }

    private void confirmOTP() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.activity_verification, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonVerify = (AppCompatButton) confirmDialog.findViewById(R.id.buttonVerify);
        vCode = (EditText) confirmDialog.findViewById(R.id.vCode);

        //Creating an alertdialog builder
        buttonResend = (AppCompatButton) confirmDialog.findViewById(R.id.buttonResend);
        buttonResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(VerificationActivity.this, "Account verified successfully", Toast.LENGTH_LONG).show();
                sendOTP(email);
            }
        });
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
                final ProgressDialog loading = ProgressDialog.show(MainActivity.this, "Authenticating", "Please wait while we check the entered code", false, false);

                //Getting the user entered otp from edittext
                final String otp = vCode.getText().toString().trim();

                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONFIRM_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //if the server response is success
                                if (response.equalsIgnoreCase("success")) {

                                    //Starting a new activity for auto login
                                    Toast.makeText(MainActivity.this, "Account verified successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email", email);
                                    bundle.putString("password", password);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                    //dismissing the progressbar
                                    loading.dismiss();
                                } else {
                                    //Displaying a toast if the otp entered is wrong
                                    Toast.makeText(MainActivity.this, "Wrong OTP Please Try Again", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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


    public void callAll() {
        obj_list();

        addCity();
        addState();
    }

    // Get the content of cities.json from assets directory and store it as string
    public String getJson() {
        String json = null;
        try {
            // Opening cities.json file
            InputStream is = getAssets().open("cities.json");
            // is there any content in the file
            int size = is.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            is.read(buffer);
            // close the stream --- very important
            is.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

    // This add all JSON object's data to the respective lists
    void obj_list() {
        // Exceptions are returned by JSONObject when the object cannot be created
        try {
            // Convert the string returned to a JSON object
            JSONObject jsonObject = new JSONObject(getJson());
            // Get Json array
            JSONArray array = jsonObject.getJSONArray("array");
            // Navigate through an array item one by one
            for (int i = 0; i < array.length(); i++) {
                // select the particular JSON data
                JSONObject object = array.getJSONObject(i);
                String city = object.getString("name");
                String state = object.getString("state");
                // add to the lists in the specified format

                listCity.add(city);
                listState.add(state);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Add the data items to the spinner


    // The second auto complete text view
    void addCity() {
        act = (AutoCompleteTextView) findViewById(R.id.actCity);
        adapterSetting(listCity);
    }

    // The third auto complete text view
    void addState() {
        Set<String> set = new HashSet<String>(listState);
        act = (AutoCompleteTextView) findViewById(R.id.actState);
        adapterSetting(new ArrayList(set));
    }

    // setting adapter for auto complete text views
    void adapterSetting(ArrayList arrayList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
        act.setAdapter(adapter);
        hideKeyBoard();
    }


    // hide keyboard on selecting a suggestion
    public void hideKeyBoard() {
        act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }
    public void addItemsOnSpinner1() {
        Spinner sprCoun;
        sprCoun = (Spinner) findViewById(R.id.spinner_date);
        List<String> list = new ArrayList<String>();
        for (int i = 1; i < 31; i++) {
            list.add(String.valueOf(i));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sprCoun.setAdapter(dataAdapter);
        }
    }
    public void addItemsOnSpinner3() {
        Spinner sprCoun;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        sprCoun = (Spinner) findViewById(R.id.spinner_year);
        List<String> list = new ArrayList<String>();
        for (int i = 1960; i < year-13; i++) {
            list.add(String.valueOf(i));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sprCoun.setAdapter(dataAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == newBtnRegister) {

            registerUser();

        }
    }

    private void registerUser() {

        String gender;

        mobileno = editTextMobileNo.getText().toString().trim();
        String name = editTextName.getText().toString().trim().toUpperCase();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();




        String permanentAddress = editTextpermanentAddress.getText().toString().trim().toUpperCase();
        String bloodgroup = userBloodgroup.getSelectedItem().toString();
        String date,month,year;
        date = spinner_date.getSelectedItem().toString();
        month = spinner_month.getSelectedItem().toString();
        year = spinner_year.getSelectedItem().toString();
        String state = userState.getText().toString().trim();
        String city = userCity.getText().toString().trim();
        if (rg1.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_LONG).show();

        } else {
            gender = ((RadioButton) findViewById(rg1.getCheckedRadioButtonId())).getText().toString();
            register(mobileno, name, email, password, bloodgroup,date,month,year,state,city,permanentAddress,gender);


        }


    }

    private void register(String mobileno,String name,String email,String password,String bloodgroup,String date,String month,String year,String state,String city,String permanentAddress, String gender) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = ProgressDialog.show(MainActivity.this, null, "Please Wait", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pd.dismiss();
                try {
                    if (s.equalsIgnoreCase("success")) {
                        confirmOTP();
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
                data.put("mobileno", params[0]);
                data.put("name", params[1]);
                data.put("email", params[2]);
                data.put("password", params[3]);
                data.put("bloodgroup", params[4]);
                data.put("date", params[5]);
                data.put("month", params[6]);
                data.put("year", params[7]);
                data.put("state", params[8]);
                data.put("city", params[9]);
                data.put("permanentAddress", params[10]);
                data.put("gender", params[11]);
                String result = ruc.sendPostRequest(Config.REGISTER_URL, data);

                return result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(mobileno,name,email,password,bloodgroup,date,month,year,state,city,permanentAddress,gender);

    }

    private void sendOTP(String email) {

        class RegisterUser extends AsyncTask<String, Void, String> {
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = ProgressDialog.show(MainActivity.this, null, "Please Wait", false, false);
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

}