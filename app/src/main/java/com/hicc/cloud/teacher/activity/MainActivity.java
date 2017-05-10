package com.hicc.cloud.teacher.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.bean.ExitEvent;
import com.hicc.cloud.teacher.fragment.BaseFragment;
import com.hicc.cloud.teacher.fragment.FriendFragment;
import com.hicc.cloud.teacher.fragment.HomeFragment;
import com.hicc.cloud.teacher.fragment.InformationFragment;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.hicc.cloud.teacher.view.MyTabLayout;
import com.hicc.cloud.teacher.view.ScrollViewPager;
import com.hicc.cloud.teacher.view.TabItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主页
 */

public class MainActivity extends AppCompatActivity implements MyTabLayout.OnTabClickListener{
    private MyTabLayout mTabLayout;
    BaseFragment fragment;
    ScrollViewPager mViewPager;
    ArrayList<TabItem>tabs;
    private static Boolean isExit = false;
    private EditText et_search;
    private boolean isCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

        // 注册监听退出登录的事件
        EventBus.getDefault().register(this);
    }


    // 设置eventBus收到消息后的事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ExitEvent event) {
        finish();
    }


    private void initView(){
        mTabLayout=(MyTabLayout)findViewById(R.id.tablayout);
        mViewPager=(ScrollViewPager)findViewById(R.id.viewpager);
        // 设置viewpager是否禁止滑动
        mViewPager.setNoScroll(false);

        // 搜索框
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCheck){
                    et_search.setHint("");
                    isCheck = !isCheck;
                }else{
                    et_search.setHint("搜索");
                    isCheck = !isCheck;
                }
            }
        });

        // 推送消息记录
        ImageView iv_content = (ImageView) findViewById(R.id.iv_content);
        iv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtli.show(getApplicationContext(),"努力开发中");
            }
        });

        // 添加
        ImageView iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtli.show(getApplicationContext(),"努力开发中");
            }
        });
    }

    private void initData(){
        tabs=new ArrayList<TabItem>();
        tabs.add(new TabItem(R.drawable.selector_tab_home, R.string.tab_home, HomeFragment.class));
        tabs.add(new TabItem(R.drawable.selector_tab_friend, R.string.tab_friend, FriendFragment.class));
        tabs.add(new TabItem(R.drawable.selector_tab_infomation, R.string.tab_information, InformationFragment.class));
        mTabLayout.initData(tabs, this);
        mTabLayout.setCurrentTab(0);

        final FragAdapter adapter = new FragAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // tab的点击事件
    @Override
    public void onTabClick(TabItem tabItem) {
        mViewPager.setCurrentItem(tabs.indexOf(tabItem));
    }

    // Fragment适配器
    public class FragAdapter extends FragmentPagerAdapter {
        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            try {
                return tabs.get(arg0).tagFragmentClz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

    }

    // 监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exitBy2Click();
        }
        return false;
    }

    // 双击退出程序
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
