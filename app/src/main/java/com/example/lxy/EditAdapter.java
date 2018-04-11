package com.example.lxy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.LinkedList;


public class EditAdapter extends BaseAdapter {

    private ListView listView;
    private Context context;
    private LinkedList<Ques> qlist;

    public EditAdapter(Context context,ListView listView){
        this.context = context;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return qlist.size()+1;
    }

    @Override
    public Object getItem(int i) {
        if(i==qlist.size())
            return null;
        return qlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount(){
        return 3;
    }

    @Override
    public int getItemViewType(int i) {
        if(i==qlist.size())
            return 2;
        return qlist.get(i).type_id==0?0:1;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(i==qlist.size()) {
            Button button = new Button(context);
            button.setText("完成编辑");
            return button;
        }
        return null;
    }
}
