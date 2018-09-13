package com.example.lxy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;

public class AnswerActivity extends AppCompatActivity {

     ListView lv;
     QuestionAdapter ques_adapter;
     ServerConnector sctor;
    int S_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        lv = (ListView)findViewById(R.id.answerlist);
        final Intent intent = getIntent();
        S_id=intent.getIntExtra("surveyID",-1);
        sctor=new ServerConnector(handler);
        Message message=new Message();
        message.what=ServerConnector.GET_QLIST;
        message.arg1=S_id;
        sctor.getMy_handler().sendMessage(message);

    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ServerConnector.GET_QLIST:{
                    LinkedList<Ques> l=(LinkedList<Ques>) msg.obj;
                    ques_adapter = new QuestionAdapter(l,AnswerActivity.this,sctor,S_id);
                    lv.setAdapter(ques_adapter);
                    break;
                }
                case ServerConnector.CMIT_ANSWER:{
                    if(msg.arg1!=-1){
                        Toast.makeText(AnswerActivity.this,"答案提交成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                        Toast.makeText(AnswerActivity.this,"答案提交失败",Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
}



