package com.example.lxy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    User user;
    ListView lv;
    ServerConnector connector;
    SurveyAdapter adapter;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        switch (msg.what) {
            case 2: {
                user.setSurvey_list((LinkedList<Survey>)msg.obj);
                Log.i("info", "handleMessage: "+((LinkedList<Survey>) msg.obj).get(0).getTable_name());


                 adapter= new SurveyAdapter(user.getSurvey_list(),MainActivity.this);

                 Button button = new Button(MainActivity.this);
                 button.setText("创建问卷");
                 button.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Toast toast=Toast.makeText(MainActivity.this,"创建问卷",Toast.LENGTH_SHORT);
                         toast.show();
                         Intent intent = new Intent(MainActivity.this,EditActivity.class);
                         startActivity(intent);
                     }
                 });

                lv.addFooterView(button);

                lv.setAdapter(adapter);

            }

        }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = getIntent();

        connector = new ServerConnector(handler);

        int id = intent.getIntExtra("user_id",-1);
        String name = intent.getStringExtra("user_name");
        if(id==-1){
            Log.i("onC", "onCreate:用户ID获取错误" );
            this.onDestroy();
        }
        user = new User(id,name);

        lv = (ListView)findViewById(R.id.LIST);


        Message msg=new Message();
        msg.what=2;
        msg.arg1=intent.getIntExtra("USER_ID",-1);

        connector.getMy_handler().sendMessage(msg);


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,view);

                //popupMenu.getMenu().add(R.string.menu1);
                popupMenu.getMenu().add("编辑");
                popupMenu.getMenu().add("开始收集");
                popupMenu.getMenu().add("删除");
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().equals("编辑")){
                            Intent intent2 = new Intent(MainActivity.this,EditActivity.class);
                            intent2.putExtra("surveyID",user.getSurvey_list().get(position).getTable_id());
                            startActivity(intent2);

                        }
                        else if(menuItem.getTitle().equals("开始收集")){
                            Intent intent1 = new Intent(MainActivity.this,AnswerActivity.class);
                            startActivity(intent1);
                        }
                        else if("删除".equals(menuItem.getTitle())){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("确定删除？");
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    //user.getSurvey_list().remove(position);
                                    adapter.Remove(position);
                                    Toast.makeText(MainActivity.this,"已删除",Toast.LENGTH_SHORT).show();
                                    //toast.show();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        return true;
                    }
                });
                return true;
            }
        });
    }
}
