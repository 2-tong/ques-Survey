package com.example.lxy;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static android.content.ContentValues.TAG;

public class ServerConnector implements Handler.Callback{

    private static String host = "http://192.168.43.140:5000/";
    private static int port = 5000;
    public static final int LONGIN=1;
    public static final int GET_SURVEYS=2;
    public static final int SET_STATUS=3;
    public static final int REGISTE=4;
    public static final int DELETE_SURVEY=5;
    public static final int ADD_SURVEY=6;
    public static final int GET_QLIST=7;
    public static final int CMIT_ANSWER=8;
    public static final int ANALYZE=9;
    public static final int GET_BARCHART=10;
    public static final int GET_PIECHART=11;
    public static final int GET_CHAYI=12;
    public static final int GET_RATE=13;

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

    private String sendJson2Server(String json_s,String route){
        try{
            URL url = new URL(host+route+"/");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(2000);
            //urlConnection.setReadTimeout(5000);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            urlConnection.connect();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(),"UTF-8"));

            writer.write(json_s);
            writer.flush();
            writer.close();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader read=new BufferedReader(new InputStreamReader(inputStream));
                String str;
                StringBuffer stringBuffer = new StringBuffer();
                while ((str=read.readLine())!=null)
                    stringBuffer.append(str);
                read.close();
                inputStream.close();
                String responStr=stringBuffer.toString();
                Log.i(TAG, route+":"+stringBuffer.toString());
                return responStr;

            }
        }
        catch(MalformedURLException  e){
            Log.i(TAG, "login: "+e);
        }
        catch(IOException e){
            Log.i(TAG, "login: "+e);
        }
        return null;
    }

    private Bitmap getPicFServer(String json_s,String route){

        Bitmap bitmap = null;
        try {
            URL imgUrl = new URL(host+route+"/");
            HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setConnectTimeout(2000);
            //conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.connect();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));

            writer.write(json_s);
            writer.flush();
            writer.close();

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
                Log.i(TAG, route+":"+"OK");
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap get_barchart(int sid,int order){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("survey_id",sid);
            jsonObject.put("order",order);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap bitmap =getPicFServer(jsonObject.toString(),"get_barchart");
        return bitmap;
    }

    private Bitmap get_piechart(int sid,int order){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("survey_id",sid);
            jsonObject.put("order",order);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap bitmap =getPicFServer(jsonObject.toString(),"get_piechart");
        return bitmap;
    }

    private int login(String username,String password){
       JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s=sendJson2Server(jsonObject.toString(),"login");
        if(s==null)
            return -1;
        try {
            jsonObject=new JSONObject(s);
            return  jsonObject.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int registe(String  json_str){

        String s=sendJson2Server(json_str,"registe");
        if(s==null)
            return 0;
        if(s.equals("OK"))
            return 1;
        else if(s.equals("REPEAT"))
            return 2;
        else return 0;

    }

    private LinkedList<Survey> get_surveylist(int id){
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("userid",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String js = sendJson2Server(jsonObject1.toString(),"get_survey");
        LinkedList<Survey> survey_list= new LinkedList();
        try {
            JSONArray jsonArray =new JSONArray(js);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int s_id = jsonObject.optInt("surveyid");
                String title = jsonObject.optString("title");
                int count = jsonObject.optInt("count");
                String posttime = jsonObject.optString("posttime");
                int except = jsonObject.optInt("except");
                String status = jsonObject.optString("status");
                if(status.equals("design"))
                    status="草稿";
                else if(status.equals("run"))
                    status="运行中";
                else
                    status="已完成";
                Survey survey=new Survey(s_id,title,count,status,id,posttime,except);
                survey_list.add(survey);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return survey_list;
    }

    private int set_status(int survey_id,int status){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("survey_id",survey_id);
            jsonObject.put("status",status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String js = sendJson2Server(jsonObject.toString(),"set_status");
        if(js==null)
            return -1;
        if(js.equals("OK"))
            return 1;
        else return -1;
    }

    private int delete_survey(int survey_id){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("survey_id",survey_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String js = sendJson2Server(jsonObject.toString(),"delete_survey");
        if(js==null)
            return -1;
        if(js.equals("OK"))
            return 1;
        else return -1;
    }

    private String survey2json(Survey s){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray=new JSONArray();
        if(s.getUid()==0)
            return "OK";
        try {
            jsonObject.put("surveyname",s.getTable_name());
            jsonObject.put("except",s.getExcept_n());
            jsonObject.put("userid",s.getUid());
            jsonObject.put("surveyID",s.getTable_id());
            for(int i=0;i<s.qlist.size();i++){
                Ques ques=s.qlist.get(i);
                JSONObject jObject = new JSONObject();
                jObject.put("body",ques.body);
                jObject.put("type",ques.type_id);
                jObject.put("order",i);
                if(ques.type_id!=0) {
                    ChoiceQues cques = (ChoiceQues)ques;
                    JSONArray jArray = new JSONArray();
                    for (int j = 0; j<cques.getOption().size();j++){
                        jArray.put(cques.getOption().get(j));
                    }
                    jObject.put("options",jArray);
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("queslist",jsonArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private int add_survey(Survey s){
        String s1=sendJson2Server(survey2json(s),"add_survey");
        Log.i(TAG, "add_survey: "+s1);
        if(s1==null)
            return -1;
        if(s1.equals("OK"))
            return 1;
        else return -1;
    }

    private LinkedList<Ques> get_qlist(int sid){
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("surveyid",sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String js = sendJson2Server(jsonObject1.toString(),"get_qlist");
        LinkedList<Ques> qlist= new LinkedList();
        try {
            JSONArray jsonArray =new JSONArray(js);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String ques_body = jsonObject.optString("ques_body");
                String type = jsonObject.optString("ques_type");
                int ques_type;
                if(type.equals("single"))
                    ques_type=1;
                else if(type.equals("multi"))
                    ques_type=2;
                else ques_type=0;
                int ques_order = jsonObject.optInt("ques_order");
                if(ques_type!=0) {
                    JSONArray jsonArray1 = jsonObject.optJSONArray("options");
                    LinkedList<String> option = new LinkedList();
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                        String op_body = jsonObject2.optString("op_body");
                        option.add(op_body);
                    }
                    ChoiceQues cques = new ChoiceQues(ques_body,ques_order,ques_type,option);
                    qlist.add(cques);
                }
                else {
                    Ques ques = new Ques(ques_body, ques_order, ques_type);
                    qlist.add(ques);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  qlist;
    }

    private int cmit_answer(int sid,LinkedList<Answer> alist){
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("surveyid",sid);
            JSONArray jsonArray =new JSONArray();
            JSONArray oporder=new JSONArray();
            for(int i=0;i<alist.size();i++){
                JSONObject jsobject=new JSONObject();
                if(alist.get(i).getQues_type()==0) {
                    jsobject.put("order", alist.get(i).getOrder());
                    jsobject.put("content",alist.get(i).getEssay_answer());
                }
                else if(alist.get(i).getOrder()!=alist.get(i+1).getOrder()){
                    char c=(char)(alist.get(i).getChoices()+65);

                    jsobject.put("order", alist.get(i).getOrder());
                    jsobject.put("content",String.valueOf(c));

                    JSONObject jobj=new JSONObject();
                    jobj.put("op_order",alist.get(i).getChoices());
                    jobj.put("order", alist.get(i).getOrder());
                    oporder.put(jobj);
                }
                else{
                    StringBuilder strb=new StringBuilder();

                    while((i<alist.size()-1)&&(alist.get(i).getOrder()==alist.get(i+1).getOrder())){

                        char cha=(char)((alist.get(i).getChoices()%100)+65);
                        strb.append(cha);

                        JSONObject jobj=new JSONObject();
                        jobj.put("op_order",(alist.get(i).getChoices()%100));
                        jobj.put("order", alist.get(i).getOrder());
                        oporder.put(jobj);

                        i++;
                    }
                    char cha=(char)((alist.get(i).getChoices()%100)+65);
                    strb.append(cha);

                    JSONObject jobj=new JSONObject();
                    jobj.put("op_order",(alist.get(i).getChoices()%100));
                    jobj.put("order", alist.get(i).getOrder());
                    oporder.put(jobj);

                    jsobject.put("order", alist.get(i).getOrder());
                    jsobject.put("content",strb.toString());
                }
                jsonArray.put(jsobject);
            }
            jsonObject.put("alist",jsonArray);
            jsonObject.put("orderlist",oporder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s1=sendJson2Server(jsonObject.toString(),"cmit_answer");
        Log.i(TAG, "cmit_answer: "+s1);
        if(s1==null)
            return -1;
        if(s1.equals("OK"))
            return 1;
        else return -1;
    }

    private LinkedList<Analyze> analyze(int sid){
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("SurveyID",sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String js = sendJson2Server(jsonObject1.toString(),"analyze");
        LinkedList<Analyze> list= new LinkedList();
        String answer=new String();
        int count=0;
        try {
            JSONArray jsonArray =new JSONArray(js);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject =jsonArray.getJSONObject(i);
                int order = jsonObject.optInt("order")+1;
                JSONArray jarray=jsonObject.optJSONArray("answer");
                for(int j=0;j<jarray.length();j++){
                    JSONObject jsobject =jarray.getJSONObject(j);
                    answer = jsobject.optString("option");
                    count = jsobject.optInt("count");
                }
                Analyze anal = new Analyze(order,answer,count);
                list.add(anal);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private int[] get_count(int sid){
        int res[]=new int[2];
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SurveyID",sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String js = sendJson2Server(jsonObject.toString(),"get_count");
        try {
            jsonObject=new JSONObject(js);
            res[0]=jsonObject.optInt("count");
            res[1]=jsonObject.optInt("except");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    private LinkedList<Chayi> get_chayi(int sid){
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("SurveyID",sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String js = sendJson2Server(jsonObject1.toString(),"get_chayi");
        LinkedList<Chayi> list= new LinkedList();
        String is_ok=new String();
        int max=0,min=0;
        try {
            JSONArray jsonArray =new JSONArray(js);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject =jsonArray.getJSONObject(i);
                int q_order = jsonObject.optInt("ques_order")+1;
                JSONObject jobj=jsonObject.optJSONObject("chayi");
                max = jobj.optInt("max");
                min = jobj.optInt("min");
                if(max>80||min<5)
                    is_ok="不合理";
                else is_ok="合理";
                Chayi cy = new Chayi(q_order,max,min,is_ok);
                list.add(cy);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case LONGIN:{
                String str[] = (String[]) message.obj;

                int id = login(str[0],str[1]);

                Message msg = new Message();
                msg.what=1;msg.arg1=id;
                ui_handler.sendMessage(msg);break;
            }
            case GET_SURVEYS:{
                Message message1 = new Message();
                message1.what = 2;
                if(message.arg1 != 0)
                    message1.obj = get_surveylist(message.arg1);
                else {
                    LinkedList<Survey> survey_list= new LinkedList();
                    message1.obj = survey_list;
                }
                message1.arg2=message.arg2;
                ui_handler.sendMessage(message1);
                break;
            }
            case SET_STATUS:{
                Message ms=new Message();
                ms.what=3;
                ms.arg1=message.arg1;
                ms.arg2=message.arg2;
                ms.obj=set_status(message.arg2,message.arg1);
                ui_handler.sendMessage(ms);
                break;
            }
            case REGISTE:{
                Message message1 =new  Message();
                message1.what=REGISTE;
                if(registe((String) message.obj)==1)
                    message1.arg1=1;
                else if(registe((String) message.obj)==2)
                    message1.arg1=2;
                else message1.arg1=0;
                ui_handler.sendMessage(message1);
                break;
            }
            case DELETE_SURVEY:{
                Message message1 = new Message();
                message1.what=DELETE_SURVEY;
                message1.arg1=delete_survey(message.arg1);
                message1.arg2=message.arg2;
                ui_handler.sendMessage(message1);
                break;
            }
            case ADD_SURVEY:{
                Message message1= new Message();
                message1.what=ADD_SURVEY;
                message1.arg1= add_survey((Survey)message.obj);
                ui_handler.sendMessage(message1);
                break;
            }
            case GET_QLIST:{
                Message message1 =new Message();
                message1.what=GET_QLIST;
                message1.obj=get_qlist(message.arg1);
                ui_handler.sendMessage(message1);
                break;
            }
            case CMIT_ANSWER:{
                Message msg=new Message();
                msg.what=CMIT_ANSWER;
                msg.arg1=cmit_answer(message.arg1,(LinkedList<Answer>) message.obj);
                ui_handler.sendMessage(msg);
                break;
            }
            case ANALYZE:{
                Message msg=new Message();
                msg.what=ANALYZE;
                msg.obj=analyze(message.arg1);
                ui_handler.sendMessage(msg);
                break;
            }
            case GET_BARCHART:{
                Message msg=new Message();
                msg.what=GET_BARCHART;
                msg.obj=get_barchart(message.arg1,message.arg2);
                ui_handler.sendMessage(msg);
                break;
            }
            case GET_PIECHART:{
                Message msg=new Message();
                msg.what=GET_PIECHART;
                msg.obj=get_piechart(message.arg1,message.arg2);
                ui_handler.sendMessage(msg);
                break;
            }
            case GET_CHAYI:{
                Message msg=new Message();
                msg.what=GET_CHAYI;
                msg.obj=get_chayi(message.arg1);
                ui_handler.sendMessage(msg);
                break;
            }

            case GET_RATE:{
                Message msg=new Message();
                msg.what=GET_RATE;
                msg.obj=get_count(message.arg1);
                ui_handler.sendMessage(msg);
            }
            default:;
        }

        return false;
    }
}
