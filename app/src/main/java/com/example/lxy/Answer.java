package com.example.lxy;

public class Answer {
    private int order;
    private String essay_answer;
    private int choices;

    public Answer(int order, String essay_answer) {
        this.order = order;
        this.essay_answer = essay_answer;

    }

    public Answer(int order, int choices) {
        this.order = order;
        this.choices = choices;
    }

    public String getEssay_answer() {
        return essay_answer;
    }

    public int getChoices() {
        return choices;
    }
}
