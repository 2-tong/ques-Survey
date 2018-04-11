package com.example.lxy;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.LinkedList;

public class ServerConnector implements Handler.Callback{

    private static String host = "";
    private static int port = 8080;


    private Handler ui_handler;
    private Handler my_handler;
    private HandlerThread h_thread;
    public ServerConnector(Handler handler){
        ui_handler = handler;
        h_thread = new HandlerThread("handlerT");
        h_thread.start();
        my_handler = new Handler(h_thread.getLooper(),this);
    }

    public Handler getMy_handler(){return my_handler;}

    public void looperquit(){
        h_thread.quit();
    }

    private int login(String username,String password){
        return 1;
    }

    private void get_surveylist(int id){


    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case 1:{
                String str[] = (String[]) message.obj;

                int id = login(str[0],str[1]);

                Message msg = new Message();
                msg.what=1;msg.arg1=id;
                ui_handler.sendMessage(msg);break;
            }
            case 2:{
                LinkedList<Survey> linkedList = new LinkedList<Survey>();
                linkedList.add(new Survey(1,"问卷1",2,"收集中"));
                linkedList.add(new Survey(2,"问卷2",2,"设计中"));
                linkedList.add(new Survey(3,"问卷3",2,"设计中"));
                linkedList.add(new Survey(4,"问卷4",2,"收集中"));
                linkedList.add(new Survey(5,"问卷5",2,"设计中"));
                linkedList.add(new Survey(6,"问卷6",2,"设计中"));
                linkedList.add(new Survey(7,"问卷7",2,"设计中"));
                linkedList.add(new Survey(8,"问卷8",2,"已完成"));
                linkedList.add(new Survey(9,"问卷9",2,"设计中"));
                linkedList.add(new Survey(10,"问卷10",2,"已完成"));
                linkedList.add(new Survey(11,"问卷11",2,"设计中"));
                Message message1 = new Message();
                message1.what = 2;
                message1.obj = linkedList;
                ui_handler.sendMessage(message1);
                break;
            }
            default:;
        }

        return false;
    }
}
