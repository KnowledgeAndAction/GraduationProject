package com.hicc.cloud.teacher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.bean.Firend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/24/024.
 */

public class FriendFragment extends BaseFragment {
    private ListView listView;
    private List<Firend> firendList = new ArrayList<>();

    @Override
    public void fetchData() {
        for (int i=0; i < 20; i++) {
            Firend firend = new Firend("张三"+i ,"1342265781"+i);
            firendList.add(firend);
        }

        listView.setAdapter(new MyAdapter());
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_firend, container, false);

        listView = (ListView) view.findViewById(R.id.list_view);

        return view;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return firendList.size();
        }

        @Override
        public Firend getItem(int position) {
            return firendList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_firend,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv_name.setText(getItem(position).getName());
            viewHolder.tv_phone.setText(getItem(position).getPhone());

            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_phone;
    }


}
