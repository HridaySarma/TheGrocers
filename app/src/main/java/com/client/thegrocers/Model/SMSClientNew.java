package com.client.thegrocers.Model;

import android.content.Context;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;


public class SMSClientNew {
    private OkHttpClient client = new OkHttpClient();
    private String url ="http://login.bulksmslaunch.com/rest/services/sendSMS/sendGroupSms";
    private String param1 ="?AUTH_KEY=";
    private String param2 ="&message=";
    private String param3= "&senderId=";
    private String param4 = "&routeId=";
    private String param5 = "&mobileNos=";
    private String param6 = "&smsContentType=";
    private Context context;
    private String getMainUrl;

    public SMSClientNew(Context context) {
        this.context = context;
    }

    public String getUrl(String AuthKey, String Message, String SenderId, String PhoneNumber, String RouteId, String SMSContentType){
        String SMSURL = "";
        if (SMSContentType.equals("english") || SMSContentType.equals("unicode")){
            try {
                SMSURL = "http://login.bulksmslaunch.com/rest/services/sendSMS/sendGroupSms"+param1+ URLEncoder.encode(AuthKey,"UTF-8")
                        + param2 + URLEncoder.encode(Message,"UTF-8") + param3 + URLEncoder.encode(SenderId,"UTF-8") + param4 +
                        URLEncoder.encode(RouteId,"UTF-8") + param5 + URLEncoder.encode(PhoneNumber,"UTF-8") + param6 + URLEncoder.encode(SMSContentType,"UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(context ,"SMSContentType should either be 'english' or 'unicode' ", Toast.LENGTH_SHORT).show();
        }

        return SMSURL;
    }
}
