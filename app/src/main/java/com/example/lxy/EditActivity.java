package com.example.lxy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.LinkedList;

public class EditActivity extends AppCompatActivity {

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

        lv = (ListView)findViewById(R.id.editlist);
        edit_adapter = new EditAdapter(test_qlist(),this);

        lv.setAdapter(edit_adapter);
    }
}
