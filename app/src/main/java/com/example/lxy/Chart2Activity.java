package com.example.lxy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.LinkedList;

public class Chart2Activity extends AppCompatActivity {

    ServerConnector sctor;
    private Spinner spinner;
    private LinkedList<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart2);
        spinner=(Spinner)findViewById(R.id.choose_q2);

        Intent intent = getIntent();
        final int S_id=intent.getIntExtra("SurveyID",-1);

        sctor=new ServerConnector(handler);
        Message msg1=new Message();
        msg1.what=ServerConnector.GET_QLIST;
        msg1.arg1=S_id;
        sctor.getMy_handler().sendMessage(msg1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Message msg=new Message();
                msg.what=ServerConnector.GET_PIECHART;
                msg.arg1=S_id;
                msg.arg2=i;
                sctor.getMy_handler().sendMessage(msg);
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
                case ServerConnector.GET_PIECHART:{
                    ImageView iv = (ImageView) findViewById(R.id.pie_chart);
                    iv.setImageBitmap((Bitmap)msg.obj);
                    break;
                }
                case ServerConnector.GET_QLIST:{
                    LinkedList<Ques> l=(LinkedList<Ques>) msg.obj;
                    data_list=new LinkedList<String>();
                    for(int i=0;i<l.size();i++){
                        data_list.add(l.get(i).getBody());
                    }
                    //适配器
                    arr_adapter= new ArrayAdapter<String>(Chart2Activity.this, android.R.layout.simple_spinner_item, data_list);
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
