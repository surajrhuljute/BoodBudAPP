package com.surajhuljute.bloodbud;



import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.support.annotation.NonNull;
import android.support.annotation.NonNull;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener{

    public static String uID;
    private String myJSONString;
    private static final String TAG_RESULTS="users";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_MOBILENO="mobileno";
    private static final String TAG_PASSWORD ="password";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_BLOODGROUP = "bloodgroup";

    private static final String TAG_DOB="dob";

    private static final String TAG_PERMANENTADDRESS = "permanentaddress";
    private static final String TAG_CITY ="city";
    private static final String TAG_STATE ="state";
    private static final String TAG_GENDER ="gender";
    TextView t;


    public static final String MY_JSON = "MY_JSON";
    JSONArray peoples = null;
    String userID;
    String id,mobileno,name,password,email,bloodgroup,dob,state,city,permanentaddress,gender;
    ArrayList<HashMap<String, String>> personList;
    ListView list;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        Intent intent = getIntent();
        myJSONString = intent.getStringExtra(HomeActivity.MY_JSON);

        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<>();

        showList();

t=(TextView)findViewById(R.id.details);

    }


    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                 id = c.getString(TAG_ID);

                 name = c.getString(TAG_NAME);
                password = c.getString(TAG_PASSWORD);
               mobileno=c.getString(TAG_MOBILENO);
                 email = c.getString(TAG_EMAIL);
                 bloodgroup = c.getString(TAG_BLOODGROUP);
                dob = c.getString(TAG_DOB);
                 permanentaddress = c.getString(TAG_PERMANENTADDRESS);
                 city = c.getString(TAG_CITY);
                state = c.getString(TAG_STATE);
                gender = c.getString(TAG_GENDER);
                HashMap<String,String> persons = new HashMap<>();



                persons.put(TAG_NAME,name);
                persons.put(TAG_BLOODGROUP,bloodgroup);
                persons.put(TAG_MOBILENO,mobileno);
                personList.add(persons);

            }

            ListAdapter adapter = new SimpleAdapter(
                    DemoActivity.this, personList, R.layout.list_item,
                    new String[]{TAG_NAME,TAG_MOBILENO,TAG_BLOODGROUP},
                    new int[]{R.id.donorname, R.id.details, R.id.bloodgrp}

            );

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    TextView textView = (TextView) view.findViewById(R.id.donorname);
                    userID = textView.getText().toString();


                    Intent intent = new Intent(DemoActivity.this, UserProfileActivity.class);
                    intent.putExtra(uID,userID.toString());
                    intent.putExtra(MY_JSON, myJSONString);
                    startActivity(intent);


                }
            });

        } catch (JSONException e) {


        }



    }
    public void donorInfo(View v)
    {
       Toast.makeText(this,"suraj",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {

      }
}
