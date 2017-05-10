package com.hicc.cloud.teacher.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.hicc.cloud.teacher.bean.Clas;
import com.hicc.cloud.teacher.bean.Division;
import com.hicc.cloud.teacher.bean.ExitEvent;
import com.hicc.cloud.teacher.bean.Grade;
import com.hicc.cloud.teacher.bean.PhoneInfo;
import com.hicc.cloud.teacher.bean.Professional;
import com.hicc.cloud.teacher.db.StudentInfoDB;
import com.hicc.cloud.teacher.fragment.BaseFragment;
import com.hicc.cloud.teacher.fragment.FriendFragment;
import com.hicc.cloud.teacher.fragment.HomeFragment;
import com.hicc.cloud.teacher.fragment.InformationFragment;
import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.Logs;
import com.hicc.cloud.teacher.utils.PhoneInfoUtil;
import com.hicc.cloud.teacher.utils.SpUtils;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.hicc.cloud.teacher.view.MyTabLayout;
import com.hicc.cloud.teacher.view.ScrollViewPager;
import com.hicc.cloud.teacher.view.TabItem;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * 主页
 */

public class MainActivity extends AppCompatActivity implements MyTabLayout.OnTabClickListener{
    private static final String URL_PHONEINFO = "http://suguan.hicc.cn/feedback1/phoneInfo.do";
    private static final String URL = "http://suguan.hicc.cn/hicccloudt/getCode";
    private static final String URL_NEW_APP = "http://suguan.hicc.cn/feedback1/newApp.do";
    private MyTabLayout mTabLayout;
    BaseFragment fragment;
    ScrollViewPager mViewPager;
    ArrayList<TabItem>tabs;
    private ProgressDialog progressDialog;
    private static Boolean isExit = false;
    private EditText et_search;
    private boolean isCheck = true;
    private StudentInfoDB db;
    private String mAppUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = StudentInfoDB.getInstance(this);

        initView();
        initData();

        // 检测更新
        checkVersionCode();

        // 加载数据到数据库中
        creatData();

        // 每次登陆将手机信息上传到服务器
        postPhoneInfo();

        // 注册监听退出登录的事件
        EventBus.getDefault().register(this);
    }

    // 上传手机信息到服务器
    private void postPhoneInfo() {
        PhoneInfo phoneInfo = PhoneInfoUtil.getPhoneInfo(this);
        // 发送GET请求
        OkHttpUtils
                .get()
                .url(URL_PHONEINFO)
                .addParams("userId", SpUtils.getStringSp(this,ConstantValue.USER_NAME,""))
                .addParams("phone", phoneInfo.getPhoneBrand())
                .addParams("phoneType", phoneInfo.getPhoneBrandType())
                .addParams("sys", phoneInfo.getAndroidVersion())
                .addParams("IMEI", phoneInfo.getIMEI())
                .addParams("CPU", phoneInfo.getCpuName())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("上传手机信息失败："+e.toString());
                        // 上传失败  重新上传
                        postPhoneInfo();
                        return;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        // 解析json
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean sucessed = jsonObject.getBoolean("flag");
                            if(sucessed){
                                Logs.i("上传手机信息成功");
                            }else{
                                // 上传失败
                                Logs.i("手机信息:服务器未响应，上传失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Logs.i("手机信息:解析异常:"+e.toString());
                        }
                    }
                });
    }

    // 设置eventBus收到消息后的事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ExitEvent event) {
        finish();
    }

    // 获取专业代码 存到数据库
    private void creatData() {
        if(SpUtils.getBoolSp(this,ConstantValue.FIRST_DATA,true)){
            // 发送GET请求
            OkHttpUtils
                    .get()
                    .url(URL)
                    .addParams("code", "16")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Logs.i(e.toString());
                            // 加载失败  下次进入应用重新加载
                            SpUtils.putBoolSp(getApplicationContext(),ConstantValue.FIRST_DATA,true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Logs.i(response);
                            // 解析json
                            getJsonInfo(response);
                        }
                    });
        }
    }

    // 解析json
    private void getJsonInfo(final String response) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean sucessed = jsonObject.getBoolean("sucessed");
                    if(sucessed){
                        JSONObject data = jsonObject.getJSONObject("data");
                        for(int i=13; i <= 16; i++){
                            JSONArray Grade = data.getJSONArray("Description_"+i);
                            for(int j=0; j < Grade.length(); j++){
                                JSONObject info = Grade.getJSONObject(j);

                                // 年级
                                com.hicc.cloud.teacher.bean.Grade grade = new Grade();
                                int GradeCode = info.getInt("GradeCode");
                                grade.setGradeCode(GradeCode);
                                db.saveGrade(grade);

                                // 学部
                                Division division = new Division();
                                String DivisionDes = info.getString("DivisionDescription");
                                division.setDivisionDes(DivisionDes);
                                if(!DivisionDes.equals("null")){
                                    int DivisionCode = info.getInt("DivisionCode");
                                    division.setDivisionCode(DivisionCode);
                                }
                                int gradeCode = info.getInt("GradeCode");
                                division.setGradeCode(gradeCode);
                                db.saveDivision(division);

                                // 专业
                                Professional professional = new Professional();
                                String ProfessionalDes = info.getString("ProfessionalDescription");
                                professional.setProfessionalDes(ProfessionalDes);
                                if(!ProfessionalDes.equals("null")){
                                    int ProfessionCode = info.getInt("ProfessionalId");
                                    professional.setProfessionalCode(ProfessionCode);
                                    int divisionCode = info.getInt("DivisionCode");
                                    professional.setDivisionCode(divisionCode);
                                }
                                db.saveProfessional(professional);

                                // 班级
                                Clas clas = new Clas();
                                int classCode = info.getInt("Nid");
                                clas.setClassCode(classCode);
                                String ClassDes = info.getString("ClassDescription");
                                clas.setClassDes(ClassDes);
                                clas.setGradeCode(gradeCode);
                                String classQQGroup = info.getString("ClassQQGroup");
                                clas.setClassQQGroup(classQQGroup);
                                if(!ProfessionalDes.equals("null")){
                                    int professionCode = info.getInt("ProfessionalId");
                                    clas.setProfessionalCode(professionCode);
                                }
                                db.saveClass(clas);
                            }
                        }
                        Logs.i("加载数据成功");
                        // 下次进入应用不在加载
                        SpUtils.putBoolSp(getApplicationContext(),ConstantValue.FIRST_DATA,false);
                    }else{
                        // 加载失败  下次进入应用重新加载
                        SpUtils.putBoolSp(getApplicationContext(),ConstantValue.FIRST_DATA,true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // 加载失败  下次进入应用重新加载
                    SpUtils.putBoolSp(getApplicationContext(),ConstantValue.FIRST_DATA,true);
                }
            }
        }.start();
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

    // 检测更新
    private void checkVersionCode() {
        // 发送GET请求
        OkHttpUtils
                .get()
                .url(URL_NEW_APP)
                .addParams("appId", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("获取最新app信息失败："+e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        Logs.i("获取最新app信息成功");
                        // 解析json
                        getAppInfoJson(response);
                    }
                });
    }

    // 解析服务器返回的app信息数据
    private void getAppInfoJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean falg = jsonObject.getBoolean("falg");
            if (falg) {
                JSONObject data = jsonObject.getJSONObject("data");
                double v = Double.valueOf(data.getString("appVersion"));
                int version = (int) v;
                // 如果服务器的版本号大于本地的  就更新
                if(version > getVersionCode()){
                    // 获取下载地址
                    mAppUrl = data.getString("appUrl");
                    // 获取新版app描述
                    String appDescribe = data.getString("appDescribe");
                    // 如果sd卡可用
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        // 展示下载对话框
                        showUpDataDialog(appDescribe, mAppUrl);
                    }
                }
            } else {
                Logs.i("获取最新app信息失败："+jsonObject.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Logs.i("解析最新app信息失败："+e.toString());
        }
    }

    // 显示更新对话框
    protected void showUpDataDialog(String description, final String appUrl) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        //设置对话框左上角图标
        builder.setIcon(R.mipmap.icon);
        //设置对话框标题
        builder.setTitle("发现新版本");
        //设置对话框内容
        builder.setMessage(description);
        //设置积极的按钮
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //请求权限
                    requestCameraPermission();
                } else {
                    //下载apk
                    downLoadApk(appUrl);
                    // 显示一个进度条对话框
                    showProgressDialog();
                }
            }
        });
        /*//设置消极的按钮
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/
        //监听取消按钮  强制更新
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            //当点击返回的按钮时执行
            @Override
            public void onCancel(DialogInterface dialog) {
                //TODO 退出应用
                finish();
                System.exit(0);
            }
        });

        builder.show();
    }

    // 下载文件
    private void downLoadApk(String appUrl) {
        OkHttpUtils
                .get()
                .url(appUrl)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),"云上工商.apk") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtli.show(getApplicationContext(),"下载失败："+e.toString());
                        Logs.i("下载失败："+e.toString()+","+id);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        ToastUtli.show(getApplicationContext(),"下载成功,保存路径:"+ ConstantValue.downloadpathName);
                        Logs.i("下载成功,保存路径:"+ConstantValue.downloadpathName);
                        // 安装应用
                        installApk(response);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        // 设置进度
                        progressDialog.setProgress((int) (100 * progress));
                    }
                });
    }

    // 下载的进度条对话框
    protected void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setTitle("下载安装包中");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    // 安装应用
    protected void installApk(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        //文件作为数据源
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent,0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // 从安装应用界面返回后的处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkVersionCode();
    }

    // 获取本应用版本号
    private int getVersionCode() {
        // 拿到包管理者
        PackageManager pm = getPackageManager();
        // 获取包的基本信息
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            // 返回应用的版本号
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 请求权限
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    // 请求权限结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //下载apk
                downLoadApk(mAppUrl);
                // 显示一个进度条对话框
                showProgressDialog();
            } else {
                ToastUtli.show(getApplicationContext(),"请求写入文件");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
