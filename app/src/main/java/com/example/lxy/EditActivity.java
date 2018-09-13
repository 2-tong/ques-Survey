package com.example.lxy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;

public class EditActivity extends AppCompatActivity
        implements View.OnClickListener{

    ServerConnector sctor;
    Survey s;
    ListView lv;
    EditAdapter edit_adapter;
    private LinkedList<Ques> test_qlist() {

        LinkedList<Ques> ls = new LinkedList<>();
        ls.add(new Ques("第1题",0,0));

        LinkedList<String> strs = new LinkedList();
        strs.add("选项1");
        strs.add("选项2");
        strs.add("选项3");
        ls.add(new ChoiceQues("第2题",1,1,strs));

        ls.add(new ChoiceQues("第3题",2,1,strs));
        ls.add(new ChoiceQues("第4题",3,2,strs));
        ls.add(new ChoiceQues("第5题",4,1,strs));
        ls.add(new Ques("第6题",5,0));
        ls.add(new Ques("第7题",6,0));
        ls.add(new ChoiceQues("第8题",7,2,strs));
        ls.add(new ChoiceQues("第9题",8,2,strs));
        ls.add(new ChoiceQues("第10题",9,1,strs));
        ls.add(new ChoiceQues("第11题",10,2,strs));
        ls.add(new ChoiceQues("第12题",11,1,strs));
        ls.add(new ChoiceQues("第13题",12,2,strs));
        ls.add(new ChoiceQues("第14题",13,1,strs));
        ls.add(new ChoiceQues("第15题",14,2,strs));
        ls.add(new ChoiceQues("第16题",15,1,strs));
        ls.add(new ChoiceQues("第17题",16,2,strs));
        ls.add(new ChoiceQues("第18题",17,1,strs));

        return ls;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final Intent intent = getIntent();
        String surveyname = intent.getStringExtra("surveyName");
        int userId=intent.getIntExtra("userid",-1);
        int S_id=intent.getIntExtra("surveyID",-1);
        int except_n=intent.getIntExtra("except",-1);
        sctor=new ServerConnector(handler);
        if(S_id==-1){//创建问卷
            s=new Survey(-1,"新建问卷",0,"草稿",userId,null,50);
        }
        else {
            s=new Survey(S_id,surveyname,0,"草稿",userId,null,except_n);
            Message message = new Message();
            message.what=ServerConnector.GET_QLIST;
            message.arg1=S_id;
            sctor.getMy_handler().sendMessage(message);
        }
        edit_adapter = new EditAdapter(new LinkedList<Ques>(),this,s,sctor);
        Button single =(Button)findViewById(R.id.add_single);
        Button multi =(Button)findViewById(R.id.add_multiple);
        Button essay =(Button)findViewById(R.id.add_essay);
        EditText survey_name = (EditText)findViewById(R.id.surveyname);
        EditText except = (EditText)findViewById(R.id.expect);

        single.setOnClickListener(this);
        multi.setOnClickListener(this);
        essay.setOnClickListener(this);
        survey_name.setText(surveyname);
        survey_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                s.table_name=editable.toString();
            }
        });
        if(except_n!=-1)
            except.setText(""+except_n);
        except.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str=editable.toString();
                if(str!=null&&str.length()!=0)
                    s.except_n=Integer.parseInt(str);
            }
        });

        lv = (ListView)findViewById(R.id.editlist);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//长按获取编辑框
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                edit_adapter.editposition=i;
                edit_adapter.notifyDataSetChanged();
                return true;
            }
        });
        lv.setAdapter(edit_adapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.add_essay)
            edit_adapter.add_Newques(0);
        else if(view.getId()==R.id.add_single)
            edit_adapter.add_Newques(1);
        else
            edit_adapter.add_Newques(2);
        lv.smoothScrollToPosition(edit_adapter.getCount()-1);//自动滑动到正在编辑位置
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //final Intent intent = getIntent();
            //int userId=intent.getIntExtra("userid",-1);
            switch (msg.what) {
                case ServerConnector.ADD_SURVEY:{
                    if(msg.arg1==1) {
                        Toast.makeText(EditActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                        Toast.makeText(EditActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    break;
                }
                case ServerConnector.GET_QLIST:
                    edit_adapter.qlist=(LinkedList<Ques>) msg.obj;
                    edit_adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
