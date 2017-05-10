package com.hicc.cloud.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hicc.cloud.R;

/**
 * Created by Administrator on 2016/11/4/004.
 * 注册
 */
public class RegisteredActivity extends AppCompatActivity {
    private ImageView iv_back;
    private EditText et_username;
    private EditText et_pwd;
    private EditText et_pwd2;
    private Button bt_registered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        initUI();
    }

    private void initUI() {
        iv_back =(ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_username = (EditText) findViewById(R.id.et_username);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd2 = (EditText) findViewById(R.id.et_pwd2);
        bt_registered = (Button) findViewById(R.id.bt_registered);

        bt_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_username.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                String pwd2 = et_pwd2.getText().toString().trim();

                if(!userName.equals("") && !pwd.equals("") && !pwd2.equals("")){
                    if(pwd.equals(pwd2)){
                        Intent intent = new Intent();
                        intent.putExtra("username",userName);
                        intent.putExtra("pwd",pwd);
                        intent.putExtra("isReg",true);
                        RegisteredActivity.this.setResult(0,intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"请将信息填写完整",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
