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
            case ServerConnector.GET_SURVEYS: {
                if(msg.arg2==2){
                    user.setSurvey_list((LinkedList<Survey>)msg.obj);
                    adapter.list=user.getSurvey_list();
                    adapter.notifyDataSetChanged();
                }else {
                     user.setSurvey_list((LinkedList<Survey>)msg.obj);
                     adapter= new SurveyAdapter(user.getSurvey_list(),MainActivity.this);

                     Button button = new Button(MainActivity.this);
                     button.setText("创建问卷");
                     button.setBackgroundResource(R.drawable.round_bto);
                     button.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             Intent intent = new Intent(MainActivity.this,EditActivity.class);
                             intent.putExtra("userid",user.getUserid());
                             startActivity(intent);
                            //startActivityForResult(intent, 1);
                         }
                     });

                    lv.addFooterView(button);

                    lv.setAdapter(adapter);

                }
                break;
            }

            case ServerConnector.SET_STATUS:{
                if((Integer)msg.obj!=-1){
                    for (Survey s:user.getSurvey_list()){
                        if(s.getTable_id()==msg.arg2)
                            s.setStatus(msg.arg1);
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"状态更新失败",Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                break;
            }
            case ServerConnector.DELETE_SURVEY:{
                if(msg.arg1!=-1) {
                    adapter.Remove(msg.arg2);
                    Toast.makeText(MainActivity.this, "已删除", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "删除操作失败", Toast.LENGTH_SHORT).show();
                break;
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
        msg.what=ServerConnector.GET_SURVEYS;
        msg.arg1=intent.getIntExtra("user_id",-1);
        connector.getMy_handler().sendMessage(msg);


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                if ("运行中".equals(user.getSurvey_list().get(position).getStatus())) {
                    popupMenu.getMenu().add("收集问卷");
                    popupMenu.getMenu().add("停止收集问卷");
                    popupMenu.getMenu().add("删除问卷");
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getTitle().equals("收集问卷")) {
                                Intent intent1 = new Intent(MainActivity.this, AnswerActivity.class);
                                intent1.putExtra("surveyID",user.getSurvey_list().get(position).getTable_id());
                                startActivity(intent1);
                            }
                            else if("停止收集问卷".equals(menuItem.getTitle())){
                                Message message=new Message();
                                message.what=ServerConnector.SET_STATUS;
                                message.arg1=3;
                                message.arg2=user.getSurvey_list().get(position).getTable_id();
                                connector.getMy_handler().sendMessage(message);
                                //status设置为"已完成"
                            }
                            else if ("删除问卷".equals(menuItem.getTitle())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("提示");
                                builder.setMessage("您确认要删除此问卷吗？");
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Message message = new Message();
                                        message.what=ServerConnector.DELETE_SURVEY;
                                        message.arg1=user.getSurvey_list().get(position).getTable_id();
                                        message.arg2=position;
                                        connector.getMy_handler().sendMessage(message);
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                            return true;
                        }
                    });
                }
                else if("草稿".equals(user.getSurvey_list().get(position).getStatus())){
                    popupMenu.getMenu().add("编辑问卷");
                    popupMenu.getMenu().add("发布问卷");
                    popupMenu.getMenu().add("删除问卷");
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getTitle().equals("编辑问卷")) {
                                Intent intent2 = new Intent(MainActivity.this, EditActivity.class);

                                intent2.putExtra("userid",user.getUserid());
                                intent2.putExtra("surveyID", user.getSurvey_list().get(position).getTable_id());
                                intent2.putExtra("surveyName", user.getSurvey_list().get(position).getTable_name());
                                intent2.putExtra("except",user.getSurvey_list().get(position).getExcept_n());
                                startActivity(intent2);

                            } else if (menuItem.getTitle().equals("发布问卷")) {
                                Message message=new Message();
                                message.what=3;
                                message.arg1=2;
                                message.arg2=user.getSurvey_list().get(position).getTable_id();
                                connector.getMy_handler().sendMessage(message);
                                //status设置为运行中
                            } else if ("删除问卷".equals(menuItem.getTitle())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("提示");
                                builder.setMessage("您确认要删除此问卷吗？");
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Message message = new Message();
                                        message.what=ServerConnector.DELETE_SURVEY;
                                        message.arg1=user.getSurvey_list().get(position).getTable_id();
                                        message.arg2=position;
                                        connector.getMy_handler().sendMessage(message);
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                            return true;
                        }
                    });
                }
                else{
                    popupMenu.getMenu().add("分析问卷");
                    popupMenu.getMenu().add("删除问卷");
                    popupMenu.getMenu().add("生成分析柱状图");
                    popupMenu.getMenu().add("生成分析饼状图");
                    popupMenu.getMenu().add("问卷回复率及差异度");
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getTitle().equals("分析问卷")) {
                                //分析问卷
                                Intent itent = new Intent(MainActivity.this,AnalyzeActivity.class);
                                itent.putExtra("SurveyID",user.getSurvey_list().get(position).getTable_id());
                                itent.putExtra("SurveyName", user.getSurvey_list().get(position).getTable_name());
                                startActivity(itent);

                            }
                            else if ("生成分析柱状图".equals(menuItem.getTitle())){
                                Intent intent = new Intent(MainActivity.this,ChartActivity.class);
                                intent.putExtra("SurveyID",user.getSurvey_list().get(position).getTable_id());
                                startActivity(intent);
                            }
                            else if ("生成分析饼状图".equals(menuItem.getTitle())){
                                Intent intent = new Intent(MainActivity.this,Chart2Activity.class);
                                intent.putExtra("SurveyID",user.getSurvey_list().get(position).getTable_id());
                                startActivity(intent);
                            }
                            else if("问卷回复率及差异度".equals(menuItem.getTitle())){
                                Intent intent = new Intent(MainActivity.this,ValueActivity.class);
                                intent.putExtra("SurveyID",user.getSurvey_list().get(position).getTable_id());
                                intent.putExtra("SurveyName", user.getSurvey_list().get(position).getTable_name());
                                startActivity(intent);
                            }
                            else if ("删除问卷".equals(menuItem.getTitle())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("提示");
                                builder.setMessage("您确认要删除此问卷吗？");
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Message message = new Message();
                                        message.what=ServerConnector.DELETE_SURVEY;
                                        message.arg1=user.getSurvey_list().get(position).getTable_id();
                                        message.arg2=position;
                                        connector.getMy_handler().sendMessage(message);
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                            return true;
                        }
                    });
                }
                return true;
                }
            });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Message msg=new Message();
        msg.what=2;
        msg.arg1=user.getUserid();
        msg.arg2=2;
        connector.getMy_handler().sendMessage(msg);
    }

}
