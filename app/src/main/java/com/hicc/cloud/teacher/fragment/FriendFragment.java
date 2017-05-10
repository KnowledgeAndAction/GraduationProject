package com.hicc.cloud.teacher.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.Logs;
import com.hicc.cloud.teacher.utils.SpUtils;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/24/024.
 */

public class FriendFragment extends BaseFragment {
    private static final String URL = "http://suguan.hicc.cn/hicccloudt/LoginT";
    private ListView listView;
    private LinearLayout ll_progress;
    private SwipeRefreshLayout sw_refresh; // 下拉刷新控件

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                // 请求失败
                case 1:
                    sw_refresh.setEnabled(true);
                    sw_refresh.setRefreshing(false);
                    if(ll_progress.VISIBLE == View.VISIBLE){
                        ll_progress.setVisibility(View.GONE);
                    }
                    ToastUtli.show(getContext(), "加载失败，请重新加载");
                    break;
                // 解析错误
                case 2:
                    sw_refresh.setEnabled(true);
                    sw_refresh.setRefreshing(false);
                    if(ll_progress.VISIBLE == View.VISIBLE){
                        ll_progress.setVisibility(View.GONE);
                    }
                    ToastUtli.show(getContext(), "加载失败");
                    break;
            }

        }
    };


    @Override
    public void fetchData() {
        intiData();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_firend, container, false);

        listView = (ListView) view.findViewById(R.id.list_view);
        sw_refresh = (SwipeRefreshLayout) view.findViewById(R.id.sw_refresh);
        ll_progress = (LinearLayout) view.findViewById(R.id.ll_progress);
        ll_progress.setVisibility(View.VISIBLE);
        // 初始设置不可下拉刷新
        sw_refresh.setEnabled(false);
        sw_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_orange_light);

        //下拉加载
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                intiData();
            }
        });


        return view;
    }

    // 网络请求
    private void intiData() {
        OkHttpUtils
                .get()
                .url(URL)
                .addParams("account", SpUtils.getStringSp(getContext(), ConstantValue.USER_NAME, ""))
                .addParams("pass", SpUtils.getStringSp(getContext(), ConstantValue.PASS_WORD, ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // 错误时可以下拉刷新
                        sw_refresh.setEnabled(true);
                        sw_refresh.setRefreshing(false);
                        if(ll_progress.VISIBLE == View.VISIBLE){
                            ll_progress.setVisibility(View.GONE);
                        }
                        Logs.i(e.toString());
                        ToastUtli.show(getContext(), "服务器繁忙，请重新加载");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        // 解析json
                        Logs.i("解析json");
                        getJsonInfo(response);
                    }
                });
    }

    // 解析json数据
    private void getJsonInfo(final String response) {
        new Thread() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean sucessed = jsonObject.getBoolean("sucessed");
                    if (sucessed) {
                        Logs.i("开始解析");
                        JSONObject data = jsonObject.getJSONObject("data");
                        // 带班信息
                        JSONArray classInfo = data.getJSONArray("classInfo");
                        for (int i = 0; i < classInfo.length(); i++) {
                            JSONObject info = classInfo.getJSONObject(i);

                        }

                        mHandler.sendEmptyMessage(0);
                    } else {
                        mHandler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    mHandler.sendEmptyMessage(2);
                    e.printStackTrace();
                }
            }
        }.start();
    }


    // 显示对话框
    protected void showDialog(final String phone) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        //设置对话框标题
        builder.setTitle("打电话给朋友");
        //设置对话框内容
        builder.setMessage("是否跳转到拨号界面");
        //设置积极的按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });
        //设置消极的按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);

        builder.show();
    }

}
