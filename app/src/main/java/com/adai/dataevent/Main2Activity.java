package com.adai.dataevent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.adai.dataevent.datasource.DataContainer;
import com.adai.dataevent.datasource.DataObserver;
import com.adai.dataevent.event.Event;
import com.adai.dataevent.event.EventObserver;
import com.adai.dataevent.group.OrderDataObserver;
import com.adai.dataevent.test.Data;
import com.adai.dataevent.test.Test;

import org.jetbrains.annotations.Nullable;

public class Main2Activity extends AppCompatActivity {
    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mEvent = new Event();

        DataContainer.INSTANCE.registerDataObserver("key", this,
                new DataObserver<Test>() {
                    @Override
                    public void onReceived(@Nullable Test test) {
                        if (test == null) {
                            return;
                        }
                        Log.d("adaibugao", "onReceived =" + test.getValue());
                    }
                });
        DataContainer.INSTANCE.registerStickyDataObserver("key", this,
                new DataObserver<Test>() {
                    @Override
                    public void onReceived(@Nullable Test test) {
                        if (test == null) {
                            return;
                        }
                        Log.d("adaibugao", "sticky onReceived =" + test.getValue());
                    }
                });

        Log.d("adaibugao", "result =" + DataContainer.INSTANCE.getData("data"));
        DataContainer.INSTANCE.registerEventObserver("event1", this,
                new EventObserver() {
                    @Override
                    public void onEvent() {
                        Log.d("adaibugao", "onEvent");
                    }
                });

        DataContainer.INSTANCE.registerDataObserver("data", this,
                new DataObserver<Data>() {
                    @Override
                    public void onReceived(@Nullable Data data) {
                        Log.d("adaibugao", "onReceived data" + data);
                    }
                });

        DataContainer.INSTANCE.addToGroup("group","event1", new Event(),
                new OrderDataObserver<Object>() {
                    @Override
                    public boolean onReceived(@Nullable Object o) {
                        Log.d("adaibugao", "group ===2" + o.toString());
                        return true;
                    }
                });
        DataContainer.INSTANCE.registerGroupObserver("group", new EventObserver() {
            @Override
            public void onEvent() {
                Log.d("adaibugao", "group over ");
            }
        });
        final int[] clickCount = {-1};
        findViewById(R.id.tv_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount[0]++;
                switch (clickCount[0]) {
                    case 0: {
                        Test test = new Test();
                        test.setValue("xxxxx");
                        DataContainer.INSTANCE.getData("key").postValue(test);
                        break;
                    }
                    case 1: {
                        Data d = new Data();
                        DataContainer.INSTANCE.getData("data").postValue(d);
                        break;
                    }
                    case 2: {
                        DataContainer.INSTANCE.getData("event1").postValue(new Event());
                        break;
                    }

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
