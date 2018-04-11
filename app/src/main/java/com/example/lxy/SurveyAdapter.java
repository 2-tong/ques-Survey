package com.example.lxy;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.LinkedList;

import static android.content.ContentValues.TAG;

/**
 *
 */
public class SurveyAdapter extends BaseAdapter {
    LinkedList<Survey> list;
    Context context;

    public SurveyAdapter(LinkedList<Survey> list, Context context){
        super();
        this.context = context;
        this.list = list;
    }

    public void alter(){
        notifyDataSetChanged();
    }

    public void Remove(int position){
        list.remove(position);
        alter();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if(position == list.size()){
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;
        Log.i(TAG, "getView: run:"+position);

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context,R.layout.surveylist,null);
            viewHolder.collections=(TextView)convertView.findViewById(R.id.collections);
            viewHolder.status=(TextView)convertView.findViewById(R.id.status);
            viewHolder.surveyname=(TextView)convertView.findViewById(R.id.surveyname);
            viewHolder.useday=(TextView)convertView.findViewById(R.id.useday);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Survey s =list.get(position);
        viewHolder.useday.setText(""+s.getUseday());
        viewHolder.surveyname.setText(s.getTable_name());
        viewHolder.status.setText(s.getStatus());
        viewHolder.collections.setText(""+s.getAnswer_count());
        return convertView;
    }

    static class ViewHolder{
        TextView surveyname;
        TextView useday;
        TextView status;
        TextView collections;
    }
}
