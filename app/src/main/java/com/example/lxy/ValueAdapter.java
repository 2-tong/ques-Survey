package com.example.lxy;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

import static android.content.ContentValues.TAG;

public class ValueAdapter extends BaseAdapter {
    LinkedList<Chayi> list;
    Context context;

    public ValueAdapter(LinkedList<Chayi> list, Context context) {
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
        ValueAdapter.ViewHolder viewHolder = null;
        Log.i(TAG, "getView: run:" + i);

        if (convertView == null) {
            viewHolder = new ValueAdapter.ViewHolder();
            convertView = View.inflate(context, R.layout.chayilist, null);
            viewHolder.q_order = (TextView) convertView.findViewById(R.id.q_order);
            viewHolder.max_cha = (TextView) convertView.findViewById(R.id.max_cha);
            viewHolder.min_cha = (TextView) convertView.findViewById(R.id.min_cha);
            viewHolder.is_ok = (TextView) convertView.findViewById(R.id.is_ok);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ValueAdapter.ViewHolder)convertView.getTag();
        }
        Chayi a =list.get(i);
        viewHolder.q_order.setText(""+a.q_order);
        viewHolder.max_cha.setText(""+a.max_cha);
        viewHolder.min_cha.setText(""+a.min_cha);
        viewHolder.is_ok.setText(a.is_ok);
        return convertView;
    }
    static class ViewHolder {
        TextView q_order;
        TextView max_cha;
        TextView min_cha;
        TextView is_ok;
    }
}
