package com.example.lxy;

import android.content.Context;
import android.text.Editable;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import static android.content.ContentValues.TAG;


public class QuestionAdapter extends BaseAdapter {


    private LinkedList<Ques> qlist;
    private ListView listView;
    private Map<Integer,Integer> answermap;
    private Map<Integer,Boolean> multianswermap;
    private Map<Integer,String> essaymap;
    private Context context;
    private int index=-1;


    public LinkedList<Answer> Get_Answer(){
        LinkedList<Answer> alist = new LinkedList<>();
        for(Ques q:qlist){
            Boolean flag = false;
            switch (q.type_id){
                case 0:{
                    String str;
                    if(essaymap.containsKey(q.order)) {
                        str = essaymap.get(q.order);
                        Answer answer = new Answer(q.order,str);
                        alist.add(answer);
                    }
                    else
                        flag=true;
                    break;}
                case 1:{
                    if(answermap.containsKey(q.order)) {
                        Answer answer = new Answer(q.order,answermap.get(q.order));
                        alist.add(answer);
                    }
                    else
                        flag=true;
                    break;}
                case 2:{
                    ChoiceQues cq = (ChoiceQues)q;
                    int k=((ChoiceQues)cq).getoptioncount();
                    flag=true;
                    for(int i=0;i<k;i++){
                        if(multianswermap.containsKey(cq.order*100+i)) {
                            if (multianswermap.get(cq.order * 100 + i)) {
                                Answer answer = new Answer(cq.order, cq.order * 100 + i);
                                alist.add(answer);
                                flag=false;
                            }
                        }
                    }
                    break;}
            }
            if(flag&&q.Isnecessary()){
                Toast toast = Toast.makeText(context,"第"+(q.order+1)+"题未作答",Toast.LENGTH_SHORT);
                toast.show();
                return null;
            }

        }

        return alist;
    }

    public QuestionAdapter(LinkedList<Ques> qlist, Context context,ListView listView) {
        this.qlist = qlist;
        this.context = context;
        this.listView = listView;
        this.answermap= new HashMap<Integer, Integer>();
        this.multianswermap= new HashMap<>();
        this.essaymap=new HashMap<>();

    }

    static class ViewHolder{
        //int type;
        TextView body;
        EditText essay_answer;
        RadioGroup options;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder ;
        final int pos=i;
        if(i==qlist.size()){
            Button button = new Button(context);
            button.setText("提交问卷");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinkedList<Answer> list = QuestionAdapter.this.Get_Answer();

                    if(list !=null)
                        Log.i("getAnswers:a", "onClick: "+list.size());
                }
            });

            return button;
        }
        Ques ques = qlist.get(i);
        if(convertView == null){
            Log.i(TAG, "getViewInIF: "+i);
            viewHolder = new ViewHolder();
            if(ques.getType_id() == 0){
                convertView = View.inflate(context,R.layout.essay_ques_layout,null);
                viewHolder.essay_answer=(EditText)convertView.findViewById(R.id.essay_answer);
                viewHolder.body=(TextView)convertView.findViewById(R.id.essay_body);
                convertView.setTag(viewHolder);
            }
            else {
                convertView = View.inflate(context, R.layout.option_ques_layout, null);
                viewHolder.body=(TextView)convertView.findViewById(R.id.option_body);
                viewHolder.options=(RadioGroup) convertView.findViewById(R.id.options);
                convertView.setTag(viewHolder);
            }
        }

        else{
            Log.i(TAG, "getView:else "+i);
            viewHolder = (QuestionAdapter.ViewHolder)convertView.getTag();
        }

        viewHolder.body.setText(ques.body);
        if(ques.type_id == 0){
            viewHolder.essay_answer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                        index = pos;
                    return false;
                }
            });
            viewHolder.essay_answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!"".equals(editable.toString())){
                        essaymap.put(pos,editable.toString());
                    }
                    else
                        essaymap.remove(pos);

                }
            });


            if(index!=-1&&index==i){
                viewHolder.essay_answer.requestFocus();
            }
        }
        else {
            ChoiceQues cquse = (ChoiceQues)ques;

            viewHolder.options.removeAllViews();

            if(cquse.type_id==1){

                LinkedList<String> str = cquse.getOption();
                int k=0;
                int flag=-1;

                viewHolder.options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        Log.i(TAG, "onCheckedChanged: "+i);
                        answermap.put(i/100,i%100);
                    }
                });
                try{
                    Integer integer = pos;
                    if(answermap.containsKey(integer))
                        flag=answermap.get(integer);
                }
                catch (NullPointerException e){
                    Log.i(TAG, "getView: "+e);
                }
                for(String  s : str) {
                    RadioButton radioButton =new RadioButton(context);
                    if (k==flag){
                        radioButton.setChecked(true);
                    }
                    int id=(i*100+(k++));
                    radioButton.setId(id);
                    radioButton.setText(s);
                    viewHolder.options.addView(radioButton);
                }

            }
            if (cquse.type_id ==2){

                int k = 0;
                for(String s : cquse.getOption()){
                    CheckBox checkBox = new CheckBox(context);
                    checkBox.setText(s);
                    viewHolder.options.addView(checkBox);
                    try{
                        Integer integer = 100*pos+k;
                        if(multianswermap.containsKey(integer))
                            checkBox.setChecked(multianswermap.get(integer));
                    }
                    catch (NullPointerException e){
                        Log.i(TAG, "get2View: "+e);
                    }
                    checkBox.setId(100*pos+(k++));
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            compoundButton.getId();
                            multianswermap.put(compoundButton.getId(),b);

                        }
                    });
                }
            }
        }

        return convertView;
    }
}
