package com.hicc.cloud.teacher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.activity.RecordActivity;
import com.hicc.cloud.teacher.activity.ScanActivity;
import com.hicc.cloud.teacher.activity.ScanResultActivity;
import com.hicc.cloud.teacher.activity.SendActivity;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.uuzuche.lib_zxing.activity.CodeUtils;


/**
 * Created by Administrator on 2016/9/24/024.
 * Alter by i_cassell on 2016/9/25.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private static final int SCAN_CODE = 0;
    private LinearLayout ll_scan;
    private LinearLayout ll_shake;
    private LinearLayout ll_record;

    // 加载数据
    @Override
    public void fetchData() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initUI(view);

        return view;
    }

    private void initUI(View view) {
        ll_scan = (LinearLayout) view.findViewById(R.id.ll_scan);
        ll_shake = (LinearLayout) view.findViewById(R.id.ll_shake);
        ll_record = (LinearLayout) view.findViewById(R.id.ll_record);

        ImageView iv_scan = (ImageView) view.findViewById(R.id.iv_scan);
        ImageView iv_shake = (ImageView) view.findViewById(R.id.iv_shake);
        ImageView iv_record = (ImageView) view.findViewById(R.id.iv_record);

        ll_scan.setOnClickListener(this);
        ll_shake.setOnClickListener(this);
        ll_record.setOnClickListener(this);

        iv_scan.setOnClickListener(this);
        iv_shake.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 扫一扫
            case R.id.ll_scan:
            case R.id.iv_scan:
                startActivityForResult(new Intent(getContext(), ScanActivity.class),SCAN_CODE);
                break;
            // 寄件
            case R.id.ll_shake:
            case R.id.iv_shake:
                //  TODO 寄件
                startActivity(new Intent(getContext(), SendActivity.class));
                break;
            // 记录
            case R.id.ll_record:
            case R.id.iv_record:
                //  TODO 历史记录
                startActivity(new Intent(getContext(), RecordActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == SCAN_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    // TODO 解析后操作
                    Intent intent = new Intent(getContext(),ScanResultActivity.class);
                    intent.putExtra("result",result);
                    startActivity(intent);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtli.show(getContext(),"解析二维码失败");
                }
            }
        }
    }

}
