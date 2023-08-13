package com.example.bmrcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

public class ModifyActivity extends AppCompatActivity {

    String name;
    String API_URL = "http://10.0.2.2/bmr/";
    String SELECT_PHP = "select_record.php";
    String DELETE_PHP = "delete_record.php";
    String UPDATE_PHP = "update_record.php";
    HashMap<String, String> map = new HashMap<String, String>();
    TextView txtName;
    EditText inputAge;
    EditText inputHeight;
    EditText inputWeight;
    RadioGroup rGroupGender;
    RadioButton rBtnMale;
    RadioButton rBtnFemale;
    Button btnCancel;
    Button btnConfirm;
    Button btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        InitViewElement();
        Intent it = this.getIntent();
        if(it != null){
            Bundle bundle = it.getExtras();
            if(bundle != null) {
                try{
                    name = bundle.getString("name");
                    map.put("name", name);
                    Handler handler = new Handler();
                    Runnable runnable  = new Runnable() {
                        @Override
                        public void run() {
                            String result = ResultActivity.executeHttpPost(API_URL + SELECT_PHP, map);
                            //Log.d("get SELECT...?", result);
                            try {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject item = new JSONArray(result).getJSONObject(0);
                                            txtName.setText(item.getString("Name"));
                                            inputAge.setText(item.getString("Age"));
                                            inputHeight.setText(item.getString("Height"));
                                            inputWeight.setText(item.getString("Weight"));
                                            if(item.getString("Gender").equals("Male")) rBtnMale.setChecked(true);
                                            else rBtnFemale.setChecked(true);
                                        } catch (JSONException ex) {
                                            Log.v("JSON", ex.toString());
                                        }
                                    }
                                });
                            } catch (Exception ex) {
                                Log.v("POST", ex.toString());
                            }
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();
                }catch (Exception e) {
                    Log.v("ReadBundle",e.toString());
                }
            }
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setClass(ModifyActivity.this, MainActivity.class);
                startActivity(it);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = ResultActivity.executeHttpPost(API_URL + DELETE_PHP, map);
                            Log.d("DeletePHP", result);
                        } catch (Exception ex) {
                            Log.v("DeletePHP", ex.toString());
                        }
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();

                Intent it = new Intent();
                it.setClass(ModifyActivity.this, MainActivity.class);
                startActivity(it);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", name);
                    map.put("age", inputAge.getText().toString());
                    map.put("height", inputHeight.getText().toString());
                    map.put("weight", inputWeight.getText().toString());
                    float fAge = Float.parseFloat(inputAge.getText().toString());
                    float fHeight = Float.parseFloat(inputHeight.getText().toString());
                    float fWeight = Float.parseFloat(inputWeight.getText().toString());
                    float fBMI = fWeight / (fHeight*0.01f*fHeight*0.01f);
                    float fBMR;
                    if(rBtnFemale.isChecked()){
                        map.put("gender", "Female");
                        fBMR = 655f + (9.6f * fWeight + 1.8f * fHeight - 4.7f * fAge);
                    }
                    else {
                        map.put("gender", "Male");
                        fBMR = 66f + (13.7f * fWeight + 5f * fHeight - 6.8f * fAge);
                    }
                    map.put("BMI", String.format("%.2f", fBMI));
                    map.put("BMR", String.format("%.2f", fBMR));
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String result = ResultActivity.executeHttpPost(API_URL + UPDATE_PHP, map);
                                Log.d("UpdatePHP", result);
                            } catch (Exception ex) {
                                Log.v("UpdatePHP", ex.toString());
                            }
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();

                    Intent it = new Intent();
                    it.setClass(ModifyActivity.this, MainActivity.class);
                    startActivity(it);
                } catch (Exception ex) {
                    Log.v("WTF", ex.toString());
                }

            }
        });

    }
    private void InitViewElement(){
        txtName = (TextView) findViewById(R.id.txtName);
        inputAge = (EditText) findViewById(R.id.inputAge);
        inputHeight = (EditText) findViewById(R.id.inputHeight);
        inputWeight = (EditText) findViewById(R.id.inputWeight);
        rGroupGender = (RadioGroup) findViewById(R.id.rGroupGender);
        rBtnMale = (RadioButton) findViewById(R.id.rBtnMale);
        rBtnFemale = (RadioButton) findViewById(R.id.rBtnFemale);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnDelete = (Button) findViewById(R.id.btnDelete);
    }
}