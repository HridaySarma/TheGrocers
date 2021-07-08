package com.client.thegrocers.Model;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EmailClientNew {

    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create(mediaType, "{ \"routeId\":17, \"contentType\":\"html\", \"mailContent\":\"Test Attachment \n\n\", \"subject\":\"TestAttachment\", \"fromEmail\":\"test@xyz.com\", \"attachmentType\":\"1\", \"fromName\":\"jitendra\", \"toEmailSet\":[ { \"email\":\"toEmail1@xyz.com\", \"personName\":\"toEmail1\" },{ \"email\":\"toEmail2@xyz.com\", \"personName\":\"toEmail2\" } ], \"ccEmailSet\":[ { \"email\":\"ccEmail1@xyz.co.in\", \"personName\":\"ccEmail1\" }, { \"email\":\"ccEmail2@xyz.co.in\", \"personName\":\"ccEmail2@xyz.co.in\" } ], \"bccEmailSet\":[ { \"email\":\"bccEmail1@xyz.co.in\", \"personName\":\"bccEmail1\" }, { \"email\":\"bccEmail2@xyz.co.in\", \"personName\":\"bccEmail2@xyz.co.in\" } ], \"attachments\":[ { \"fileType\":\"text/plain\", \"fileName\":\"TestFileone.txt\", \"fileData\":\"dGV4dCBmaWxl\" }, { \"fileType\":\"text/plain\", \"fileName\":\"testfiletwo.txt\", \"fileData\":\"dGV4dCBmaWxl\" } ] }");
    Request request = new Request.Builder()
            .url("http://login.bulksmslaunch.com/rest/services/sendEmail/email?AUTH_KEY=authKey")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Cache-Control", "no-cache")
            .build();

    Response response;

    {
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
