package com.surajhuljute.bloodbud;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private EditText searchVal;
    private Button btnSearch;
    private TextView textViewJSON;
    public static final String MY_JSON = "MY_JSON";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchVal = (EditText) findViewById(R.id.searchVal);
        textViewJSON = (TextView) findViewById(R.id.textViewJSON);
        textViewJSON.setMovementMethod(new ScrollingMovementMethod());

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        textViewJSON = (TextView) findViewById(R.id.textViewJSON);
    }

    private void search(){
        pd = ProgressDialog.show(HomeActivity.this,null,"Please Wait", true, true);
        final String search_keyword = searchVal.getText().toString().trim();
        final String email_key = email;
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SEARCH_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        //If we are getting success from server
                        if(response.equalsIgnoreCase(null)){
                            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                            intent.putExtra("Something went wrong","Success");
                            startActivity(intent);

                        }else{
                            Intent intent = new Intent(HomeActivity.this, DemoActivity.class);
                            intent.putExtra(MY_JSON,response.toString());
                            startActivity(intent);
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
                params.put(Config.KEY_SEARCH_VAL, search_keyword);
                params.put("email", email_key);

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
        search();
    }
}
