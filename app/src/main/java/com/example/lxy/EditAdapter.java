package com.example.lxy;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;

import static android.content.ContentValues.TAG;


public class EditAdapter extends QuestionAdapter {

    public int editposition = 2;

    public EditAdapter(LinkedList<Ques> qlist, Context context) {
        super(qlist, context);
    }

    public void add_Newques(int type) {
        LinkedList<String> strs = new LinkedList();
        strs.add("选项1");
        strs.add("选项2");
        if (type == 0) {
            Ques q = new Ques("第" + qlist.size() + "题:", qlist.size(), type);
            qlist.add(q);
        } else
            qlist.add(new ChoiceQues("第" + qlist.size() + "题:", qlist.size(), type, strs));
        editposition=qlist.size()-1;
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int i) {
        return i == editposition ? 3 : super.getItemViewType(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {



        if ((i != editposition) && (i != qlist.size())){
            view=super.getView(i, view, parent);
            view.setFocusable(false);
            return view;
        }
        else if (i == qlist.size()) {
            Button button1 = new Button(context);
            button1.setText("完成编辑");
            return button1;

        } else {

            LinearLayout ly;
            if (view == null)
                ly = new LinearLayout(context);
            else {
                ly = (LinearLayout) view;
                view.setFocusable(true);
            }
            ly.removeAllViews();
            ly.setOrientation(LinearLayout.VERTICAL);


            ly.addView(super.getView(i, null, null));
            ly.addView(getEditer(i));

            return ly;
        }

    }

    private View getEditer(int i) {

        final Ques ques = qlist.get(i);
        final int pos=i;
        View view = null;
        switch (ques.type_id) {
            case 0: {
                view = View.inflate(context, R.layout.essay_editer, null);
                final EditText edit_body = (EditText)view.findViewById(R.id.edit_body);
                Button finish = (Button) view.findViewById(R.id.essay_finish);
                Button delete = (Button) view.findViewById(R.id.essay_delete);
                final CheckBox isn = (CheckBox) view.findViewById(R.id.essay_necessary);

                edit_body.setText(ques.body);

                edit_body.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        ques.body=editable.toString();

                }
                });

                edit_body.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                            index = pos;
                        return false;
                    }
                });
                if(index!=-1&&index==pos){
                    edit_body.requestFocus();
                }

                finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //body.setText(qlist.get(pos).body);
                        editposition = -1;
                        notifyDataSetChanged();
                        Toast.makeText(context, "已保存", Toast.LENGTH_SHORT).show();
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("提示");
                        builder.setMessage("确定删除？");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editposition = -1;
                                qlist.remove(pos);
                                notifyDataSetChanged();
                                Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
                            }
                        }).show();

                    }
                });
                isn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                       ques.isnecessary=b;
                    }
                });
                break; }
            default: {
                final ChoiceQues cques=(ChoiceQues)ques;
                view = View.inflate(context, R.layout.option_editer, null);
                LinearLayout ly = (LinearLayout)view.findViewById(R.id.option_editer_line);
                Button finish_option = (Button)view.findViewById(R.id.option_finish);
                Button delete_option = (Button)view.findViewById(R.id.option_delete);
                Button add_choice = (Button)view.findViewById(R.id.add_option);
                CheckBox isn_option = (CheckBox) view.findViewById(R.id.option_isn);
                final EditText edit_option = (EditText)view.findViewById(R.id.option_body_editer);

                int k=0;
                for(String str:cques.getOption()){
                    final int kf=k++;
                    View view1=View.inflate(context,R.layout.addoption_layout,null);
                    EditText editText = (EditText)view1.findViewById(R.id.option_name);
                    Button button = (Button)view1.findViewById(R.id.delete_option);
                    editText.setText(str);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            cques.getOption().set(kf,editable.toString());
                        }
                    });

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((ChoiceQues) ques).getOption().remove(kf);
                            notifyDataSetChanged();
                        }
                    });

                    ly.addView(view1);
                }

                edit_option.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        ques.body=editable.toString();
                    }
                });

                add_choice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cques.getOption().add("新选项");
                        notifyDataSetChanged();
                    }
                });

                delete_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editposition=-1;
                        qlist.remove(pos);
                        notifyDataSetChanged();
                    }
                });

                finish_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editposition = -1;
                        notifyDataSetChanged();
                        Toast.makeText(context, "已保存", Toast.LENGTH_SHORT).show();
                    }
                });
                isn_option.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        cques.isnecessary=b;
                    }
                });
                break;}
        }
        return view;
    }

}
