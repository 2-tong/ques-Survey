package com.example.lxy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.LinkedList;

public class EditActivity extends AppCompatActivity
        implements View.OnClickListener{

    ListView lv;
    EditAdapter edit_adapter;
    private LinkedList<Ques> test_qlist() {

        LinkedList<Ques> ls = new LinkedList<>();
        ls.add(new Ques("第一题",0,0));

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
        ls.add(new ChoiceQues("第4题",8,2,strs));
        ls.add(new ChoiceQues("第5题",9,1,strs));
        ls.add(new ChoiceQues("第4题",10,2,strs));
        ls.add(new ChoiceQues("第5题",11,1,strs));
        ls.add(new ChoiceQues("第4题",12,2,strs));
        ls.add(new ChoiceQues("第5题",13,1,strs));
        ls.add(new ChoiceQues("第4题",14,2,strs));
        ls.add(new ChoiceQues("第5题",15,1,strs));
        ls.add(new ChoiceQues("第4题",16,2,strs));
        ls.add(new ChoiceQues("第5题",17,1,strs));

        return ls;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        if((intent.getIntExtra("surveyID",-1))==-1){
            edit_adapter = new EditAdapter(new LinkedList<Ques>(),this);
        }
        else
            edit_adapter = new EditAdapter(test_qlist(),this);
        Button single =(Button)findViewById(R.id.add_single);
        Button multi =(Button)findViewById(R.id.add_multiple);
        Button essay =(Button)findViewById(R.id.add_essay);

        single.setOnClickListener(this);
        multi.setOnClickListener(this);
        essay.setOnClickListener(this);

        lv = (ListView)findViewById(R.id.editlist);


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        lv.smoothScrollToPosition(edit_adapter.getCount()-1);
    }
}
