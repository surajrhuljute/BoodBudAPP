package com.surajhuljute.bloodbud;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener {

    private String myJSONString;

    private static final String JSON_ARRAY = "users";
    private static final String ID = "id";
    private static final String USERNAME = "name";
    private static final String PASSWORD = "password";

    private JSONArray users = null;

    private int TRACK = 0;

    private TextView editTextId;
    private TextView editTextUserName;
    private TextView editTextPassword;

    Button btnPrev;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        myJSONString = intent.getStringExtra(HomeActivity.MY_JSON);


        editTextId = (TextView) findViewById(R.id.name);
        editTextUserName = (TextView) findViewById(R.id.username);
        editTextPassword = (TextView) findViewById(R.id.password);

        btnPrev = (Button) findViewById(R.id.buttonPrev);
        btnNext = (Button) findViewById(R.id.buttonNext);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        extractJSON();

        showData();
    }


    private void extractJSON() {
        try {
            JSONObject jsonObject = new JSONObject(myJSONString);
            users = jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void moveNext() {
        if (TRACK < users.length()) {
            TRACK++;
        }
        showData();
    }

    private void movePrev() {
        if (TRACK > 0) {
            TRACK--;
        }
        showData();
    }

    private void showData() {
        try {
            JSONObject jsonObject = users.getJSONObject(TRACK);

            editTextId.setText(jsonObject.getString(ID));
            editTextUserName.setText(jsonObject.getString(USERNAME));
            editTextPassword.setText(jsonObject.getString(PASSWORD));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnNext) {
            moveNext();
        }
        if (v == btnPrev) {
            movePrev();
        }
    }
}
