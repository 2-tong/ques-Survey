package com.example.lxy;

import java.util.LinkedList;

public class Survey {
    private int table_id;
    private int uid;
    public String table_name;
    private int answer_count;
    private String status;
    private String post_time;
    public int except_n;

    public LinkedList<Ques> qlist;


    public Survey(int table_id, String table_name, int answer_count, String status,int uid,String post_time,int except_n) {
        this.table_id = table_id;
        this.table_name = table_name;
        this.answer_count = answer_count;
        this.status = status;
        this.uid=uid;
        this.post_time=post_time;
        this.except_n=except_n;
    }


    public int getTable_id() {
        return table_id;
    }

    public int getAnswer_count() {
        return answer_count;
    }

    public String getStatus() {
        return status;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setStatus(int status){
        if(status==1)
            this.status="草稿";
        if(status==2)
            this.status="运行中";
        if(status==3)
            this.status="已完成";
    }

    public String getPost_time() {
        return post_time;
    }

    public int getUid() {
        return uid;
    }

    public int getExcept_n(){
        return except_n;
    }
}
