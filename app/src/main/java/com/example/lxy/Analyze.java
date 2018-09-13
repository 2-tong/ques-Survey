package com.example.lxy;

public class Analyze {
    protected int order;
    protected String answer;
    protected int count;

    public Analyze(int order,String answer){
        this.order=order;
        this.answer=answer;
    }
    public Analyze(int order,String answer,int count){
        this.order=order;
        this.answer=answer;
        this.count=count;
    }
}

