package com.example.lxy;

import java.util.LinkedList;

public class User {
    private String username;
    private int    userid;
    private LinkedList<Survey> Survey_list;


    public User(int id, String name){
        userid = id;
        username = name;

    }

    public void setSurvey_list(LinkedList<Survey> survey_list) {
        Survey_list = survey_list;
    }

    public LinkedList<Survey> getSurvey_list() {
        return Survey_list;
    }

    public String getUsername() {
        return username;
    }

    public int getUserid() {
        return userid;
    }
}
