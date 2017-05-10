package com.hicc.cloud.teacher.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hicc.cloud.R;
import com.vipul.hp_hp.timelineview.TimelineView;

/**
 * Created by Administrator on 2016/10/13/026.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {
    public TextView time;
    public TextView des;
    public  TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        time = (TextView) itemView.findViewById(R.id.tx_time);
        des = (TextView) itemView.findViewById(R.id.tx_des);
        mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
        mTimelineView.initLine(viewType);
    }
}
