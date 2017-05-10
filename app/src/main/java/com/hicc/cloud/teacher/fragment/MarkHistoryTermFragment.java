package com.hicc.cloud.teacher.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.adapter.MarkHistoryAdapter;
import com.hicc.cloud.teacher.bean.Mark;
import com.hicc.cloud.teacher.utils.Logs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 野 on 2016/10/13.
 */

public class MarkHistoryTermFragment extends BaseFragment {

    private List<Mark> markList = new ArrayList<Mark>();
    private MarkHistoryAdapter adapter;
    private RecyclerView mRecyclerView;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public void fetchData() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historymark, container, false);

        initUI(view);

        // 动态注册广播
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SET_DATA");
        getContext().registerReceiver(mBroadcastReceiver, intentFilter);


        return view;
    }

    // 广播接收者
    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logs.i("历史成绩界面收到广播了吗？？？？？？");

            markList = (List<Mark>) intent.getSerializableExtra("marklist");

            // 接收到广播  设置适配器
            adapter = new MarkHistoryAdapter(markList);
            mRecyclerView.setAdapter(adapter);
        }
    }

    private void initUI(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 4;
            }
        });
        mRecyclerView.setLayoutManager(manager);
    }

    private LinearLayoutManager getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        return linearLayoutManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消注册广播
        if (mBroadcastReceiver != null) {
            getContext().unregisterReceiver(mBroadcastReceiver);
        }
    }
}
