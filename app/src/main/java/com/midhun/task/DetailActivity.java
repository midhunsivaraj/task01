package com.midhun.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView symbol = findViewById(R.id.name);
        TextView fullname = findViewById(R.id.fullname);
        TextView rank = findViewById(R.id.rank);
        TextView firstdata = findViewById(R.id.firstdata);
        TextView lastdata = findViewById(R.id.lastdata);
        TextView platform = findViewById(R.id.platform);

        Intent intent = getIntent();
        String name = intent.getStringExtra("key");
        SharedPreferences preferences =this.getSharedPreferences("fulldata",0);
        String cryptodata = preferences.getString(name, "default value");

        try {
            JSONObject jsonObject = new JSONObject(cryptodata);
            symbol.setText(jsonObject.getString("symbol"));
            fullname.setText(jsonObject.getString("name"));
            rank.setText("Rank : "+jsonObject.getString("rank"));
            firstdata.setText("First data : "+jsonObject.getString("first_historical_data"));
            lastdata.setText("Last data : "+jsonObject.getString("last_historical_data"));

            if(jsonObject.getString("platform")!="null"){
                JSONObject platformdata = jsonObject.getJSONObject("platform");
                platform.setText("Platform : "+platformdata.getString("name"));
            }
            else{
                platform.setText("Platform : No platform");
            }

            //Toast.makeText(this, jsonObject.getString("platform"), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
