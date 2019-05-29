package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.datasource.DataContainer;
import com.example.myapplication.datasource.DataObserver;

import org.jetbrains.annotations.Nullable;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        DataContainer.INSTANCE.registerDataObserver("key", this, new DataObserver<Test>() {
            @Override
            public void onReceived(@Nullable Test test) {
                if (test == null){
                    return;
                }
                Log.d("ttttt","onReceived ="+test.getValue());
            }
        });
        Log.d("ttttt","result ="+DataContainer.INSTANCE.getData("data"));
    }

}
