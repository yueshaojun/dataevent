package com.adai.dataevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.adai.dataevent.datasource.DataContainer;
import com.adai.dataevent.group.OrderDataObserver;
import com.adai.dataevent.test.Data;
import com.adai.dataevent.test.Test;

import org.jetbrains.annotations.Nullable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Test test = new Test();
        test.setValue("yuetest");
        final Data data = new Data();
        DataContainer.INSTANCE.addData("data",data);
        DataContainer.INSTANCE.addData("key",test);
        DataContainer.INSTANCE.addToGroup("group", test, new OrderDataObserver<Object>() {
            @Override
            public boolean onReceived(@Nullable Object o) {
                Log.d("adaibugao","group ==="+o.toString());
                return true;
            }
        });
        DataContainer.INSTANCE.addToGroup("group", data, new OrderDataObserver<Object>() {
            @Override
            public boolean onReceived(@Nullable Object o) {
                Log.d("adaibugao","group ===1"+o.toString());
                return true;
            }
        });
        findViewById(R.id.tv_hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataContainer.INSTANCE.addEvent("event1");
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity","onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("MainActivity","onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity","onDestroy");
    }
}
