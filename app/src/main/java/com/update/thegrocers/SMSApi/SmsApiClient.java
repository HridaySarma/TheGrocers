package com.update.thegrocers.SMSApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SmsApiClient {
    private static Retrofit retrofit = null;
    private static String url;

    public static Retrofit getClient(String phoneNumber,String randomNumber){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        url = "https://www.smsgatewayhub.com/api/mt/SendSMS?APIKey=ubD43nCqMUOXfeLKYV4oLQ&senderid=EDEZIP&channel=2&DCS=0&flashsms=0&number=91"+phoneNumber+"&text=Hello,%20Hope%20You%20are%20doing%20well%20Your%20OTP%20is%20"+randomNumber+"%20Ezip%20Info%20Tech%20Pvt%20Ltd&route=1\n";

        retrofit = new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return  retrofit;
    }

}
