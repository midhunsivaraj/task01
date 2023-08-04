package com.midhun.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        ImageButton backbtn = findViewById(R.id.backbtn);
        Button addremovefav = findViewById(R.id.addtofav);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailActivity.this.finish();
            }
        });

        Intent intent = getIntent();
        final String name = intent.getStringExtra("key");
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

        SharedPreferences favpreferences =this.getSharedPreferences("favdata",0);
        final SharedPreferences.Editor editor = this.getSharedPreferences("favdata", MODE_PRIVATE).edit();
        String favid = favpreferences.getString(name, "false");

        if(favid.equals("false")){
            addremovefav.setText("Add to favourites");
            addremovefav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putString(name, "true");
                    editor.commit();
                }
            });
        }
        else{
            addremovefav.setText("Remove from favourites");
            addremovefav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putString(name, "false");
                    editor.commit();
                }
            });
        }


    }
}
