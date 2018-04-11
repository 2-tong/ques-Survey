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
        return ls;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        lv = (ListView)findViewById(R.id.editlist);
        edit_adapter = new EditAdapter(this,lv);
    }
}
