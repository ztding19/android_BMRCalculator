package com.example.bmrcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {
    EditText inputName;
    EditText inputAge;
    EditText inputHeight;
    EditText inputWeight;
    RadioGroup rGroupGender;
    RadioButton rBtnMale;
    RadioButton rBtnFemale;
    Button btnCancel;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        InitViewElement();

        Intent it = this.getIntent();
        if(it != null) {
            Bundle bundle = it.getExtras();
            if (bundle!= null){
                String inputStr = bundle.getString("input");
                //if (inputStr != null && !inputStr.equals("")){}
            }
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsAnythingEmpty()){
                    Toast.makeText(CreateActivity.this, "You missed something!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("name", inputName.getText().toString());
                bundle.putString("age", inputAge.getText().toString());
                bundle.putString("height", inputHeight.getText().toString());
                bundle.putString("weight", inputWeight.getText().toString());
                if(rBtnFemale.isChecked())  bundle.putString("gender", "Female");
                else bundle.putString("gender", "Male");

                Intent it = new Intent();
                it.putExtras(bundle);
                it.setClass(CreateActivity.this, ResultActivity.class);
                startActivity(it);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setClass(CreateActivity.this, MainActivity.class);
                startActivity(it);
            }
        });
    }

    private void InitViewElement(){
        inputName = (EditText) findViewById(R.id.inputName);
        inputAge = (EditText) findViewById(R.id.inputAge);
        inputHeight = (EditText) findViewById(R.id.inputHeight);
        inputWeight = (EditText) findViewById(R.id.inputWeight);
        rGroupGender = (RadioGroup) findViewById(R.id.rGroupGender);
        rBtnMale = (RadioButton) findViewById(R.id.rBtnMale);
        rBtnFemale = (RadioButton) findViewById(R.id.rBtnFemale);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    private boolean IsAnythingEmpty(){
        if (inputName.getText().toString().equals("")) return true;
        else if (inputAge.getText().toString().equals("")) return true;
        else if (inputHeight.getText().toString().equals("")) return true;
        else if (inputWeight.getText().toString().equals("")) return true;
        else if (!rBtnFemale.isChecked() && !rBtnMale.isChecked())  return true;
        else return false;
    }
}