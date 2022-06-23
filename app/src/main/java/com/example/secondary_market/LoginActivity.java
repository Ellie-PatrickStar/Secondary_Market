package com.example.secondary_market;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText EtStuNumber, EtStuPwd;
    private Boolean bPwdSwitch = false;
    private EditText etPwd;
    private EditText etAccount;
    private CheckBox cbRememberPwd;
    private String username;
    LinkedList<User> users = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etPwd = findViewById(R.id.et_password);
        Button btnRegister = findViewById(R.id.btn_register);
        etAccount = findViewById(R.id.et_username);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);
        Button btLogin = findViewById(R.id.btn_login);
        EtStuNumber=findViewById(R.id.et_username);
        EtStuPwd=findViewById(R.id.et_password);
        //跳转到注册界面
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String spFileName = getResources().getString(R.string.shared_preferences_file_name);
        String accountKey = getResources().getString(R.string.login_account_name);
        String passwordKey = getResources().getString(R.string.login_password);
        String rememberPasswordKey = getResources().getString(R.string.login_remember_password);

        SharedPreferences spFile = getSharedPreferences(spFileName, MODE_PRIVATE);
        String account = spFile.getString(accountKey, null);
        String password = spFile.getString(passwordKey, null);
        Boolean rememberPassword = spFile.getBoolean(rememberPasswordKey, false);


        if (account != null && !TextUtils.isEmpty(account)) {
            etAccount.setText(account);
        }

        if (password != null && !TextUtils.isEmpty(password)) {
            etPwd.setText(password);
        }
        cbRememberPwd.setChecked(rememberPassword);

//        点击button保存
        btLogin.setOnClickListener(this);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                if (CheckInput()) {
                    UserDbHelper dbHelper = new UserDbHelper(getApplicationContext(), UserDbHelper.DB_NAME, null, 1);
                    users = dbHelper.readUsers();
                    for (User user : users) {
                        //如果可以找到,则输出登录成功,并跳转到主界面
                        if (user.getUsername().equals(EtStuNumber.getText().toString()) && user.getPassword().equals(EtStuPwd.getText().toString())) {
                            flag = true;
                            Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            username = EtStuNumber.getText().toString();
                            bundle.putString("username", username);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                    //否则提示登录失败,需要重新输入
                    if (!flag) {
                        Toast.makeText(LoginActivity.this, "学号或密码输入错误!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    public boolean CheckInput() {

        String StuNumber = EtStuNumber.getText().toString();
        String StuPwd = EtStuPwd.getText().toString();
        if(StuNumber.trim().equals("")) {
            Toast.makeText(LoginActivity.this,"学号不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StuPwd.trim().equals("")) {
            Toast.makeText(LoginActivity.this,"密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
        public void onClick (View v){
            String spFileName = getResources().getString(R.string.shared_preferences_file_name);
            String accountKey = getResources().getString(R.string.login_account_name);
            String passwordKey = getResources().getString(R.string.login_password);
            String rememberPasswordKey = getResources().getString(R.string.login_remember_password);

            SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spFile.edit();

            String account = spFile.getString(accountKey, null);
            String password = spFile.getString(passwordKey, null);
            Boolean rememberPassword = spFile.getBoolean(rememberPasswordKey, false);

            if (account != null && !TextUtils.isEmpty(account)) {
                etAccount.setText(account);
            }
            if (password != null && !TextUtils.isEmpty(password)) {
                etPwd.setText(password);
            }
            if (rememberPassword != false && !TextUtils.isEmpty(password)) {
                cbRememberPwd.setChecked(rememberPassword);
            }
            if (cbRememberPwd.isChecked()) {
                password = etPwd.getText().toString();
                account = etAccount.getText().toString();

                editor.putString(accountKey, account);
                editor.putString(passwordKey, password);
                editor.putBoolean(rememberPasswordKey, true);
                editor.apply();
            } else {
                editor.remove(accountKey);
                editor.remove(passwordKey);
                editor.remove(rememberPasswordKey);
                editor.apply();
            }

        }
    }


