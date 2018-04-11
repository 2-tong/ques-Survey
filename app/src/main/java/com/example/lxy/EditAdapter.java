package com.example.lxy;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.LinkedList;


public class EditAdapter extends QuestionAdapter {

    private int editposition=2;

    public EditAdapter(LinkedList<Ques> qlist, Context context) {
        super(qlist, context);
    }

    public void add_Newques(int type){
        LinkedList<String> strs = new LinkedList();
        strs.add("选项1");
        strs.add("选项2");
        if(type == 0) {
            Ques q = new Ques("第" + qlist.size() + "题:", qlist.size(),type);
            qlist.add(q);
        }
        else
            qlist.add(new ChoiceQues("第" + qlist.size() + "题:",qlist.size(),type,strs));
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount(){
        return 4;
    }

    @Override
    public int getItemViewType(int i) {
        return i==editposition?3:super.getItemViewType(i);
    }

    @Override
    public View getView(int i,View view,ViewGroup parent){

        if((i!=editposition)&&(i!=qlist.size()))
            return super.getView(i, view, parent);
        else if(i==qlist.size()){
            Button button1 = new Button(context);
            button1.setText("完成编辑");
            return button1;

        }
        else
        {
            LinearLayout ly;
            if(view==null)
                ly=new LinearLayout(context);
            else
                ly=(LinearLayout) view;

            ly.addView(super.getView(i,null,null));
            ly.addView(getEditer(i));

            return ly;
        }

    }

    private View getEditer(int i){


        
        return null;
    }

}
