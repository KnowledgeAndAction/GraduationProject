package com.hicc.cloud.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.adapter.RecyclerAdapter;
import com.hicc.cloud.teacher.bean.SomeThing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4/004.
 */
public class RecordActivity extends AppCompatActivity {
    private ImageView iv_back;
    private RecyclerView recyclerView;
    private List<SomeThing> thingsList = new ArrayList<>();
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initUI();

        initData();
    }

    private void initData() {
        SomeThing someThing1 = new SomeThing("申通快递：11055456455","史记全套","2017-04-19  15:38:10");
        thingsList.add(someThing1);
        SomeThing someThing2 = new SomeThing("圆通快递：11065487455","第一行代码","2017-03-16  17:35:42");
        thingsList.add(someThing2);
        SomeThing someThing3 = new SomeThing("申通快递：11063556481","自控力","2017-03-15  09:33:24");
        thingsList.add(someThing3);
        SomeThing someThing4 = new SomeThing("韵达快递：16354756455","明朝那些事儿","2017-02-25  10:34:21");
        thingsList.add(someThing4);

        adapter = new RecyclerAdapter(thingsList);
        recyclerView.setAdapter(adapter);
    }

    private void initUI() {
        iv_back =(ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
