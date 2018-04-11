package com.example.lxy;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText repassword;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=(EditText)findViewById(R.id.username_Reg);
        password=(EditText)findViewById(R.id.password_Reg);
        repassword=(EditText)findViewById(R.id.repassword_Reg);
        button=(Button)findViewById(R.id.finish);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = username.getText().toString();
                String psw = password.getText().toString();
                String repsw = repassword.getText().toString();
                boolean judge = isMobile(uname);
                if (uname.equals("")||psw.equals("")
                        ||repsw.equals("")){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("提示").setMessage("用户名或密码不能为空")
                            .setPositiveButton("确定",null).show();
                }
                else if(!psw.equals(repsw)){
                    Toast.makeText(RegisterActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                }

                else if(judge == false){
                    Toast.makeText(RegisterActivity.this, "手机号码输入有误", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(RegisterActivity.this,Loginctivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    public static boolean isMobile(String number) {

        String num = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }
}
