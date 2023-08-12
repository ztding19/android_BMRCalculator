package com.example.bmrcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    Button btnCreate;
    ListView dataList;
    String[] data = new String[]{
            "Name\nBMR"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewElement();
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        dataList.setAdapter(adapter);
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

    }
    private void initViewElement(){
        dataList =(ListView)findViewById(R.id.dataList);
        btnCreate = (Button)findViewById(R.id.btnCreate);
    }
}