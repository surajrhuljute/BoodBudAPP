package com.surajhuljute.bloodbud;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends BaseActivity{
    TextView textname,textbloodgroup,textGender,textcity,textstate,textpermanentaddress,textage, donated_blood;

    ImageView btn_donated;
    public  String uID;


    String userID;
    ArrayList<HashMap<String, String>> personList;
    ListView list;
    ProgressDialog pd;
    String id, mobileno, name, password, bloodgroup,dob,state,city,gender, permanentaddress;
    private static final String TAG_ADD = "password";
    public static final String MY_JSON = "MY_JSON";

    private static final String TAG_RESULTS = "users";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_MOBILENO = "mobileno";
    private static final String TAG_PASSWORD = "password";
  //  private static final String TAG_EMAIL = "email";
    private static final String TAG_BLOODGROUP = "bloodgroup";

    private static final String TAG_DOB = "dob";

    private static final String TAG_PERMANENTADDRESS = "permanentaddress";
    private static final String TAG_CITY ="city";
    private static final String TAG_STATE ="state";
    private static final String TAG_GENDER ="gender";
    JSONArray peoples = null;
    private String myJSONString;
    String donationDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        uID = intent.getStringExtra(DemoActivity.uID);
        myJSONString=intent.getStringExtra(DemoActivity.MY_JSON);



        textname = (TextView) findViewById(R.id.uname);
        textbloodgroup = (TextView) findViewById(R.id.bloodgroup);
        textage = (TextView) findViewById(R.id.age);
        textcity = (TextView) findViewById(R.id.currentcity);
        textpermanentaddress = (TextView) findViewById(R.id.permanentaddress);


textstate=(TextView)findViewById(R.id.currentstate);
        textGender=(TextView)findViewById(R.id.gender);


        donated_blood = (TextView) findViewById(R.id.donated_blood);
        btn_donated = (ImageView) findViewById(R.id.btn_donated);
        btn_donated.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                int mYear=mcurrentDate.get(Calendar.YEAR);
                int mMonth=mcurrentDate.get(Calendar.MONTH);
                int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(UserProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    /*      Your code   to get date and time    */
                        //Toast.makeText(getApplicationContext(), selectedday + "/" + selectedmonth + "/" + selectedyear, Toast.LENGTH_LONG).show();
                        donationDate=selectedyear+"-"+selectedmonth+"-"+selectedday;
                        mark_donate(uID,email,donationDate);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date of blood donation");
                mDatePicker.show();  }
        });

show();

    }
    private void mark_donate(String uID,String email,String donationDate) {

        class RegisterUser extends AsyncTask<String, Void, String> {
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = ProgressDialog.show(UserProfileActivity.this,null,"Please Wait", true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pd.dismiss();

                if(s.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Thank you for donatating blood",Toast.LENGTH_LONG).show();
                    btn_donated.setVisibility(View.GONE);
                    donated_blood.setText("You have already donated blood to this person");
                }else{
                    Toast.makeText(getApplicationContext(),"Please try again",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("uID",params[0]);
                data.put("email",params[1]);
                data.put("donationDate",params[2]);
                String result = ruc.sendPostRequest(Config.DONATED_BLOOD,data);
                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(uID,email,donationDate);
    }

    public void show(){
        try {
        JSONObject jsonObj = new JSONObject(myJSONString);
        peoples = jsonObj.getJSONArray(TAG_RESULTS);

    for (int i = 0; i < peoples.length(); i++) {
        JSONObject c = peoples.getJSONObject(i);

        name = c.getString(TAG_NAME);
        if(name.equalsIgnoreCase(uID)){
            id = c.getString(TAG_ID);
            password = c.getString(TAG_PASSWORD);
            mobileno = c.getString(TAG_MOBILENO);
           // email = c.getString(TAG_EMAIL);
            bloodgroup = c.getString(TAG_BLOODGROUP);
          String  dob = c.getString(TAG_DOB);
            permanentaddress = c.getString(TAG_PERMANENTADDRESS);

            city = c.getString(TAG_CITY);
            state = c.getString(TAG_STATE);
            gender = c.getString(TAG_GENDER);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          try {
              Calendar dobc = Calendar.getInstance();

              dobc.setTime(sdf.parse(dob));

              dob = String.valueOf(getAge(dobc));
          }catch(Exception e)
          {

          }
        textpermanentaddress.setText(permanentaddress);
        textage.setText(dob);
        textname.setText(name);
        textbloodgroup.setText(bloodgroup);
         textcity.setText(city);
            textstate.setText(state);
            textGender.setText(gender);
        }
    }
}
catch (JSONException e) {
                e.printStackTrace();
            }
        }



    public static int getAge(Calendar dob) throws Exception {
        Calendar today = Calendar.getInstance();

        int curYear = today.get(Calendar.YEAR);
        int dobYear = dob.get(Calendar.YEAR);

        int age = curYear - dobYear;

        // if dob is month or day is behind today's month or day
        // reduce age by 1
        int curMonth = today.get(Calendar.MONTH);
        int dobMonth = dob.get(Calendar.MONTH);
        if (dobMonth > curMonth) { // this year can't be counted!
            age--;
        } else if (dobMonth == curMonth) { // same month? check for day
            int curDay = today.get(Calendar.DAY_OF_MONTH);
            int dobDay = dob.get(Calendar.DAY_OF_MONTH);
            if (dobDay > curDay) { // this year can't be counted!
                age--;
            }
        }

        return age;
    }
    }
