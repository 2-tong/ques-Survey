package com.example.lxy;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;

import static android.content.ContentValues.TAG;

public class AnalyzeAdapter extends BaseAdapter {
    LinkedList<Analyze> list;
    Context context;

    public AnalyzeAdapter(LinkedList<Analyze> list, Context context) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Log.i(TAG, "getView: run:" + i);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.analyzelist, null);
            viewHolder.order = (TextView) convertView.findViewById(R.id.order);
            viewHolder.answer = (TextView) convertView.findViewById(R.id.answer);
            viewHolder.count = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Analyze a =list.get(i);
        viewHolder.order.setText(""+a.order);
        viewHolder.answer.setText(a.answer);
        viewHolder.count.setText(""+a.count);
        return convertView;
    }
    static class ViewHolder {
        TextView order;
        TextView answer;
        TextView count;
    }
}
