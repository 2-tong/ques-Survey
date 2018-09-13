package com.example.lxy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

public class AnalyzeActivity extends AppCompatActivity {

    ListView lv;
    ServerConnector sctor;
    AnalyzeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        lv = (ListView)findViewById(R.id.analyze_list);
        TextView SN=(TextView)findViewById(R.id.suvery_title);

        Intent intent = getIntent();
        int S_id=intent.getIntExtra("SurveyID",-1);
        String S_name=intent.getStringExtra("SurveyName");
        SN.setText(S_name);

        sctor=new ServerConnector(handler);
        Message message=new Message();
        message.what=ServerConnector.ANALYZE;
        message.arg1=S_id;
        sctor.getMy_handler().sendMessage(message);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ServerConnector.ANALYZE:{
                    LinkedList<Analyze> l=(LinkedList<Analyze>) msg.obj;
                    adapter = new AnalyzeAdapter(l,AnalyzeActivity.this);
                    lv.setAdapter(adapter);
                    break;
                }
            }
        }
    };
}
