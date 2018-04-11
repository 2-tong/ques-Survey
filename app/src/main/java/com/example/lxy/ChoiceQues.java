package com.example.lxy;

import java.util.LinkedList;

public class ChoiceQues extends Ques {
    private LinkedList<String> option;

    public ChoiceQues(String body,int order,int type_id,LinkedList<String> option) {
        super(body,order,type_id);
        this.option = option;
    }

    public int getoptioncount(){
        return option.size();
    }
    public LinkedList<String> getOption() {
        return option;
    }
}
