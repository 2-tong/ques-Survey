package com.example.lxy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import static android.content.ContentValues.TAG;
import java.util.LinkedList;

public class ValueActivity extends AppCompatActivity {

    ListView lv;
    TextView count;
    TextView rate;
    TextView total;
    ServerConnector sctor;
    ValueAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value);
        lv = (ListView)findViewById(R.id.chalist);
        TextView SN=(TextView)findViewById(R.id.stitle);
        count=(TextView)findViewById(R.id.re_count);
        rate=(TextView)findViewById(R.id.rate);
        total=(TextView)findViewById(R.id.total);

        Intent intent = getIntent();
        int S_id=intent.getIntExtra("SurveyID",-1);
        String S_name=intent.getStringExtra("SurveyName");
        SN.setText(S_name);

        sctor=new ServerConnector(handler);
        Message message=new Message();
        message.what=ServerConnector.GET_CHAYI;
        message.arg1=S_id;
        sctor.getMy_handler().sendMessage(message);

        Message message1=new Message();
        message1.what=ServerConnector.GET_RATE;
        message1.arg1=S_id;
        sctor.getMy_handler().sendMessage(message1);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ServerConnector.GET_CHAYI:{

                    LinkedList<Chayi> l=(LinkedList<Chayi>) msg.obj;
                    adapter = new ValueAdapter(l,ValueActivity.this);
                    lv.setAdapter(adapter);
                    break;
                }
                case ServerConnector.GET_RATE:{
                    int get[]=(int[])msg.obj;
                    float r = 100*get[0]/get[1];
                    count.setText(""+get[0]);
                    rate.setText(""+r);
                    total.setText(""+get[1]);
                    break;
                }
            }
        }
    };
}
