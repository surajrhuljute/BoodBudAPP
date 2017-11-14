package com.surajhuljute.bloodbud;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
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

import java.util.HashMap;
import java.util.Map;


public class EditProfile extends BaseActivity {

    public EditText username,phone,statename,cityname,address;
    private ImageView update_user;
    private Switch is_available_switch;
    private TextView test;
    private static final String TAG_RESULTS="login_user";
    private static final String TAG_ID = "userID";
    private static final String TAG_NAME = "userName";
    private static final String TAG_PHONE = "userPhone";
    private static final String TAG_STATE ="stateName";
    private static final String TAG_CITY ="cityName";
    private static final String TAG_ADDRESS ="permanentAddress";
    public String email;
    public String userID;
    String is_available_status="0";
    JSONArray peoples = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        username = (EditText) findViewById(R.id.userName);
        phone =(EditText) findViewById(R.id.userPhone);
        statename =(EditText) findViewById(R.id.userState);
        cityname =(EditText) findViewById(R.id.userCity);
        address =(EditText) findViewById(R.id.userAddress);
        update_user = (ImageView) findViewById(R.id.update_user);
        is_available_switch= (Switch) findViewById(R.id.is_available);

        callAll();
        get_user_data();

        //////set the switch to ON
        is_available_switch.setChecked(false);
        //////attach a listener to check for changes in state
        is_available_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    is_available_status="1"; //edit here
                }else{
                    is_available_status="0"; //edit here
                }
            }
        });

        /*This method is for State and Cities AutoComplete in BaseActivity*/
        update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

    }

    //Getting Login user Data
    public void get_user_data(){

        pd = ProgressDialog.show(this,null,"Please Wait", true, true);

        final String login_user_key = email;

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_USER_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();

                        //If we are getting success from server
                        if(response.equalsIgnoreCase("failed")){
                            Toast.makeText(EditProfile.this,"Something went wrong",Toast.LENGTH_LONG).show();
                        }else{

                            try {
                                JSONObject jsonObj = new JSONObject(response);

                                peoples = jsonObj.getJSONArray(TAG_RESULTS);

                                JSONObject c = peoples.getJSONObject(0);
                                userID = c.getString(TAG_ID);
                                String userName = c.getString(TAG_NAME);
                                String userPhone = c.getString(TAG_PHONE);
                                String stateName = c.getString(TAG_STATE);
                                String cityName = c.getString(TAG_CITY);
                                String paddress = c.getString(TAG_ADDRESS);

                                //textview.setText(name.substring(0,1).toUpperCase() + name.substring(1));
                                username.setText(userName);
                                phone.setText(userPhone);
                                statename.setText(stateName);
                                cityname.setText(cityName);
                                address.setText(paddress);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.LOGIN_USER_KEY, login_user_key);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateUser() {
        String name = username.getText().toString().trim();
        String uphone = phone.getText().toString().trim();
        String state = statename.getText().toString().trim();
        String city = cityname.getText().toString().trim();
        String add = address.getText().toString().trim();
        String uID= userID;
        String is_available=is_available_status;

        update_user(name,uphone,state,city,add,uID,is_available);
    }

    private void update_user(String name, String uphone, String state, String city, String add, String uID, final String is_available) {

        class RegisterUser extends AsyncTask<String, Void, String> {
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               pd = ProgressDialog.show(EditProfile.this,null,"Please Wait", true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pd.dismiss();

                    if(s.equalsIgnoreCase("success")){
                        Toast.makeText(getApplicationContext(),"Profile updated successfully",Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),is_available,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("uphone",params[1]);
                data.put("state",params[2]);
                data.put("city",params[3]);
                data.put("add",params[4]);
                data.put("uID",params[5]);
                data.put("is_available",params[6]);

                String result = ruc.sendPostRequest(Config.UPDATE_LOGIN_USER,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name,uphone,state,city,add,uID,is_available);
    }

}
