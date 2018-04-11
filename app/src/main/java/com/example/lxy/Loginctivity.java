package com.example.lxy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Loginctivity extends AppCompatActivity {

    private Button login;
    private Button register;
    private AutoCompleteTextView username;
    private EditText password;
    private CheckBox savePsw;
    private SharedPreferences SPset;
    private ServerConnector connector;

    private void initView(){
        login = (Button) findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        username = (AutoCompleteTextView)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        savePsw  = (CheckBox)findViewById(R.id.remberPsw);
    }

    private void SavePSW(Boolean isSave,String psw){

        SharedPreferences.Editor editor= SPset.edit();

        editor.putBoolean("REMEMBER",isSave);
        editor.putString("USERNAME",username.getText().toString());
        if(isSave)
            editor.putString("PASSWORD",psw);
        else
            editor.remove("PASSWORD");
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginctivity);

        initView();

        SPset = getPreferences(MODE_PRIVATE);
        username.setText(SPset.getString("USERNAME",""));
        if(SPset.getBoolean("REMEMBER",false))
            password.setText(SPset.getString("PASSWORD",""));
        savePsw.setChecked(SPset.getBoolean("REMEMBER",false));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connector =new ServerConnector(handler);
                String str[]=new String[2];
                str[0]= username.getText().toString();
                str[1]=password.getText().toString();

                Message msg = new Message();
                msg.what=1;
                msg.obj=str;

                connector.getMy_handler().sendMessage(msg);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Loginctivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:{
                    SavePSW(savePsw.isChecked(),password.getText().toString());

                    Intent intent = new Intent(Loginctivity.this,MainActivity.class);
                    intent.putExtra("user_name",username.getText().toString());
                    intent.putExtra("user_id",msg.arg1);

                    connector.looperquit();

                    startActivity(intent);

                    break;
                }
                default:;
            }

        }
    };
}
