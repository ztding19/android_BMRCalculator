package com.example.bmrcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class ResultActivity extends AppCompatActivity {
    String API_URL = "http://10.0.2.2/bmr/";
    String INSERT_PHP = "insert_record.php";
    String name;
    String age;
    String gender;
    String height;
    String weight;
    String BMI;
    String BMR;
    Float fBMI;
    Float fBMR;

    TextView textName;
    TextView textBMI;
    TextView textBMR;

    Button btnCancel;
    Button btnSave;
    HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent it = this.getIntent();
        InitViewElement();

        if(it != null) {
            Bundle bundle = it.getExtras();
            if (bundle!= null){
                try {
                    name = bundle.getString("name");
                    textName.setText(name);
                    age = bundle.getString("age");
                    gender = bundle.getString("gender");
                    height = bundle.getString("height");
                    weight = bundle.getString("weight");
                    float fAge = Float.parseFloat(age);
                    float fHeight = Float.parseFloat(height);
                    float fWeight = Float.parseFloat(weight);
                    fBMI = fWeight / (fHeight*0.01f*fHeight*0.01f);
                    BMI = String.format("%.2f", fBMI);
                    textBMI.setText(BMI);
                    if (gender.equals("Male"))
                        fBMR = 66f + (13.7f * fWeight + 5f * fHeight - 6.8f * fAge);
                    else
                        fBMR = 655f + (9.6f * fWeight + 1.8f * fHeight - 4.7f * fAge);
                    BMR = String.format("%.2f", fBMR);
                    textBMR.setText(BMR);

                    map.put("name", name);
                    map.put("age", age);
                    map.put("gender", gender);
                    map.put("height", height);
                    map.put("weight", weight);
                    map.put("BMI", BMI);
                    map.put("BMR", BMR);

                }catch (Exception e){
                    //Toast.makeText(ResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                    Log.v("ResultDisplayFailed", e.toString());
                }
            }
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setClass(ResultActivity.this, CreateActivity.class);
                startActivity(it);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String path = API_URL + INSERT_PHP;
                            executeHttpPost(path, map);
                            //Toast.makeText(ResultActivity.this, map.toString(), Toast.LENGTH_SHORT);
                            Log.d("Insert Thread", "Insert finished.");

                        } catch (Exception e) {
                            Log.v("Insert Exception", e.toString());
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();

                Intent it = new Intent();
                it.setClass(ResultActivity.this, MainActivity.class);
                startActivity(it);
            }
        });


    }

    public static String executeHttpPost(String path, HashMap<String, String> map) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            //conn.setReadTimeout(10000);
            //conn.setConnectTimeout(15000);
            conn.connect();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            //OutputStream os = conn.getOutputStream();
            //DataOutputStream wr = new DataOutputStream(os);
            wr.writeBytes(getJSONString(map));
            wr.flush();
            wr.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            conn.disconnect();
            Log.d("HttpPost", "map : " + getJSONString(map));
            return result.toString();
        } catch (IOException e) {
            Log.v("HttpPost", e.toString());
            return "";
        }
    }

    public static String getJSONString(HashMap<String, String> map) {
        JSONObject json = new JSONObject();
        for(String key:map.keySet()) {
            try {
                json.put(key, map.get(key));
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return json.toString();
    }


    private void InitViewElement(){
        textName = (TextView) findViewById(R.id.textName);
        textBMI = (TextView) findViewById(R.id.textBMI);
        textBMR = (TextView) findViewById(R.id.textBMR);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);
    }
}