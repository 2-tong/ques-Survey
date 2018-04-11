package com.example.lxy;

import java.util.LinkedList;

public class Survey {
    private int table_id;
    private String table_name;
    private int answer_count;
    private String status;
    private int useday=2;

    private LinkedList<Ques> qlist;


    public Survey(int table_id, String table_name, int answer_count, String status) {
        this.table_id = table_id;
        this.table_name = table_name;
        this.answer_count = answer_count;
        this.status = status;
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

    public int getUseday() {
        return useday;
    }
}
