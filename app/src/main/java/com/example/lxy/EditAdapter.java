package com.example.lxy;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
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

    Survey newSurvey;
    ServerConnector serverConnector;
    public int editposition=-1;

    public EditAdapter(LinkedList<Ques> qlist, Context context,Survey survey,ServerConnector serverConnector) {
        super(qlist, context,serverConnector,survey.getTable_id());
        this.newSurvey=survey;
        this.serverConnector=serverConnector;
    }

    /**
     * 插入新题
     **/
    public void add_Newques(int type) {//
        LinkedList<String> strs = new LinkedList();//默认给出两个选项
        strs.add("选项1");
        strs.add("选项2");
        if (type == 0) {
            Ques q = new Ques("第" + (qlist.size()+1) + "题:", qlist.size(), type);
            qlist.add(q);
        } else
            qlist.add(new ChoiceQues("第" + (qlist.size()+1) + "题:", qlist.size(), type, strs));
        editposition=qlist.size()-1;//将编辑框加到新插入题的位置
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
        Log.i(TAG, "getView_Edit: "+i);
        if ((i != editposition) && (i != qlist.size())){
            view=super.getView(i, view, parent);//调用父类的getview方法
            return view;
        }
        else if (getItemViewType(i)==2) {
            //在最后添加一个完成编辑的按钮，提交保存问卷
            Button button1 = new Button(context);
            button1.setText("保存问卷");
            button1.setBackgroundResource(R.drawable.round_bto);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Message message =new Message();
                    message.what=ServerConnector.ADD_SURVEY;
                    newSurvey.qlist=EditAdapter.this.qlist;
                    message.obj=newSurvey;
                    serverConnector.getMy_handler().sendMessage(message);
                }
            });
            return button1;
        } else {//获取带编辑框类型的view
            LinearLayout ly;
            if (view == null)//当view为null时创建一个线性布局对象
                ly = new LinearLayout(context);
            else {//否则复用上一个view
                ly = (LinearLayout) view;
            }
            ly.removeAllViews();
            ly.setOrientation(LinearLayout.VERTICAL);
            ly.addView(super.getView(i, null, null));
            ly.addView(getEditer(i));
            return ly;
        }

    }

    /*
    * 添加编辑框
     */
    private View getEditer(int i) {

        final Ques ques = qlist.get(i);
        final int pos=i;
        View view = null;
        switch (ques.type_id) {//为问答题添加编辑框
            case 0: {
                view = View.inflate(context, R.layout.essay_editer, null);
                final EditText edit_body = (EditText)view.findViewById(R.id.edit_body);
                Button finish = (Button) view.findViewById(R.id.essay_finish);
                Button delete = (Button) view.findViewById(R.id.essay_delete);
                final CheckBox isn = (CheckBox) view.findViewById(R.id.essay_necessary);

                edit_body.setText(ques.body);

                edit_body.addTextChangedListener(new TextWatcher() {//为问答题题目编辑设置监听
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

                edit_body.setOnTouchListener(new View.OnTouchListener() {//设置焦点
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

                finish.setOnClickListener(new View.OnClickListener() {//设置点击完成这道题编辑的监听
                    @Override
                    public void onClick(View view) {
                        editposition = -1;
                        Toast.makeText(context, "已保存", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();//刷新
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                                Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
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

                    ly.addView(view1);//动态添加选择题的选项
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
                                Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });

                finish_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editposition = -1;
                        Toast.makeText(context, "已保存", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

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
