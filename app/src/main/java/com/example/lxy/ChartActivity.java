package com.example.lxy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.graphics.Bitmap;


import java.util.LinkedList;

public class ChartActivity extends AppCompatActivity {

    ServerConnector sctor;
    private Spinner spinner;
    private LinkedList<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        spinner=(Spinner)findViewById(R.id.choose_q);

        Intent intent = getIntent();
        final int S_id=intent.getIntExtra("SurveyID",-1);

        sctor=new ServerConnector(handler);
        Message msg=new Message();
        msg.what=ServerConnector.GET_QLIST;
        msg.arg1=S_id;
        sctor.getMy_handler().sendMessage(msg);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Message message=new Message();
                message.what=ServerConnector.GET_BARCHART;
                message.arg1=S_id;
                message.arg2=i;
                sctor.getMy_handler().sendMessage(message);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ServerConnector.GET_BARCHART:{
                    ImageView iv = (ImageView) findViewById(R.id.bar_chart);
                    iv.setImageBitmap((Bitmap)msg.obj);
                    //((ImageView)findViewById(R.id.bar_chart)).setImageBitmap((Bitmap)msg.obj);
                    break;
                }
                case ServerConnector.GET_QLIST:{
                    //数据
                    LinkedList<Ques> l=(LinkedList<Ques>) msg.obj;
                    data_list=new LinkedList<String>();
                    for(int i=0;i<l.size();i++){
                        data_list.add("第"+(l.get(i).getOrder()+1)+"题:"+l.get(i).getBody());
                    }
                    //适配器
                    arr_adapter= new ArrayAdapter<String>(ChartActivity.this, android.R.layout.simple_spinner_item, data_list);
                    //设置样式
                    arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //加载适配器
                    spinner.setAdapter(arr_adapter);
                    break;
                }

            }

        }
    };
}
