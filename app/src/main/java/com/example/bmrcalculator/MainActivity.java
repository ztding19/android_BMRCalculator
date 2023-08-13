package com.example.bmrcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {
    String API_URL = "http://10.0.2.2/bmr/";
    String QUERY_PHP = "load_record.php";
    Button btnCreate;
    ListView dataList;
    String[] data = new String[]{
            "Name\nBMI, BMR"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewElement();
        RefreshList();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("input","test");

                Intent it = new Intent();
                it.putExtras(bundle);
                it.setClass(MainActivity.this, CreateActivity.class);
                startActivity(it);
            }
        });
        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, data[i], Toast.LENGTH_SHORT);
                Bundle bundle = new Bundle();
                bundle.putString("name", data[i].split("\n")[0].toString());
                Intent it = new Intent();
                it.putExtras(bundle);
                it.setClass(MainActivity.this, ModifyActivity.class);
                startActivity(it);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshList();
    }
    //https://stackoverflow.com/questions/57697796/how-to-make-a-new-thread-android-studio
    //https://stackoverflow.com/questions/21278442/how-to-fetch-a-simple-json-data-in-android-from-php?rq=3

    private void showList() {
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        dataList.setAdapter(adapter);
    }
    public static String loadURLData(String msgsUrl) {
        try{
            URL url = new URL(msgsUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            InputStreamReader streamReader = new InputStreamReader(conn.getInputStream());

            BufferedReader br = new BufferedReader(streamReader);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null) {
                sb.append(line);
                //sb.append("\n");
            }
            br.close();
            String resp = sb.toString();
            //Log.d("Load data", "loadURLData: " + resp);
            conn.disconnect();
            return resp;
        }catch (IOException iOEx) {
            Log.d("LoadURLData", iOEx.toString());
            return "";
        }
    }

    private void RefreshList(){
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String resp = loadURLData(API_URL + QUERY_PHP);
                try {
                    JSONArray items = new JSONArray(resp);
                    ArrayList<String> dataFromJSON = new ArrayList<>();
                    for(int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        dataFromJSON.add(item.getString("Name").toString() + "\n" + item.getString("BMI").toString() + ", " + item.getString("BMR"));
                    }
                    data = dataFromJSON.toArray(data);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showList();
                        }
                    });
                }
                catch (JSONException ex) {
                    Log.d("ReadPhpException", ex.toString());
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    private void initViewElement(){
        dataList =(ListView)findViewById(R.id.dataList);
        btnCreate = (Button)findViewById(R.id.btnCreate);
    }
}