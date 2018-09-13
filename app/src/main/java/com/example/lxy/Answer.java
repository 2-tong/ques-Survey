package com.example.lxy;

public class Answer {
    private int order;
    private String essay_answer;
    private int choices;
    private int ques_type;

    public Answer(int order, String essay_answer,int ques_type) {
        this.order = order;
        this.essay_answer = essay_answer;
        this.ques_type=ques_type;
    }

    public Answer(int order, int choices,int ques_type) {
        this.order = order;
        this.choices = choices;
        this.ques_type=ques_type;
    }

    public int getOrder(){return order;}
    public String getEssay_answer() {
        return essay_answer;
    }
    public int getQues_type(){return ques_type;}
    public int getChoices() {
        return choices;
    }
}
