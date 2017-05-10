package com.hicc.cloud.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.adapter.TimeLineAdapter;
import com.hicc.cloud.teacher.bean.TimeLineModel;
import com.hicc.cloud.teacher.utils.Logs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/15/015.
 */
public class ScanResultActivity extends AppCompatActivity {

    private ImageView iv_back;
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        initUI();

        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        Logs.i(result);

        initDate();

    }

    private void initDate() {
        TimeLineModel model = new TimeLineModel();
        model.setTime("2017.5.4 - 16:49");
        model.setDes("交易成功");
        mDataList.add(model);

        TimeLineModel mode2 = new TimeLineModel();
        mode2.setTime("2017.5.4 - 08:05");
        mode2.setDes("【河北保定公司已收入】");
        mDataList.add(mode2);

        TimeLineModel mode3 = new TimeLineModel();
        mode3.setTime("2017.5.3 - 22:55");
        mode3.setDes("由【北京中转部】发往【河北保定公司】");
        mDataList.add(mode3);

        TimeLineModel mode4 = new TimeLineModel();
        mode4.setTime("2017.5.2 - 16:25");
        mode4.setDes("由【浙江杭州航空部】发往【北京中转部】");
        mDataList.add(mode4);

        TimeLineModel mode5 = new TimeLineModel();
        mode5.setTime("2017.5.2 - 10:36");
        mode5.setDes("由【申通沃德仓库】发往【浙江杭州航空部】");
        mDataList.add(mode5);

        TimeLineModel mode6 = new TimeLineModel();
        mode6.setTime("2017.5.2 - 09:42");
        mode6.setDes("【申通沃德仓库】的收件员【园区3】已收件");
        mDataList.add(mode6);

        mTimeLineAdapter = new TimeLineAdapter(mDataList);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);
    }

    private LinearLayoutManager getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        return linearLayoutManager;
    }
}
