package com.example.lxy;

public class Ques {
    protected String body;
    protected int order;
    protected int type_id;
    protected Boolean isnecessary=true;

    public Ques(String body,int order,int type_id){
        this.body = body;
        this.order = order;
        this.type_id = type_id;
    }
    public Ques(String body,int order,int type_id,Boolean isN){
        this.body = body;
        this.order = order;
        this.type_id = type_id;
        isnecessary=isN;
    }

    public int getOrder() {
        return order;
    }

    public int getType_id() {
        return type_id;
    }

    public String getBody() {
        return body;
    }

    public Boolean Isnecessary() {
        return isnecessary;
    }
}
