package com.example.bmrcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ResultActivity extends AppCompatActivity {

    TextView textName;
    TextView textBMI;
    TextView textBMR;
    Float BMI;
    Float BMR;
    Button btnCancel;
    Button btnSave;

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
                    String name = bundle.getString("name");
                    textName.setText(name);
                    float age = Float.parseFloat(bundle.getString("age"));
                    String gender = bundle.getString("gender");
                    float height = Float.parseFloat(bundle.getString("height"));
                    float weight = Float.parseFloat(bundle.getString("weight"));
                    BMI = weight / (height*0.01f*height*0.01f);
                    textBMI.setText(String.format("%.2f", BMI));
                    if (gender.equals("Male"))
                        BMR = 66f + (13.7f * weight + 5f * height - 6.8f * age);
                    else
                        BMR = 655f + (9.6f * weight + 1.8f * height - 4.7f * age);
                    textBMR.setText(String.format("%.2f", BMR));
                }catch (Exception e){
                    Toast.makeText(ResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
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


    }

    private void InitViewElement(){
        textName = (TextView) findViewById(R.id.textName);
        textBMI = (TextView) findViewById(R.id.textBMI);
        textBMR = (TextView) findViewById(R.id.textBMR);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);
    }
}