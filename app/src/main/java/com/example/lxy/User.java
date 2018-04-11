package com.example.lxy;

import java.util.LinkedList;

public class User {
    private String username;
    private int    userid;
    private LinkedList<Survey> Survey_list;

    //private MyDB database;

    public User(int id, String name){
        userid = id;
        username = name;
        //database = db;


        //urvey_list = database.getSuryList(userid);

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
}
