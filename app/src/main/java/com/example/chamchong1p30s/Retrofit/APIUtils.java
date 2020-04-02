package com.example.chamchong1p30s.Retrofit;

import android.content.Context;

import com.example.chamchong1p30s.MainActivity;

public class APIUtils {

    public static final String Base_url = MainActivity.domain;

    public static Dataclient getdata (Context context){
        return  RetrofitClient.getClient(Base_url,context).create(Dataclient.class);
    }
}
