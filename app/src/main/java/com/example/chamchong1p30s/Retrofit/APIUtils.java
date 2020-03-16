package com.example.chamchong1p30s.Retrofit;

import com.example.chamchong1p30s.MainActivity;

public class APIUtils {

    public static final String Base_url = MainActivity.domain;

    public static Dataclient getdata (){
        return  RetrofitClient.getClient(Base_url).create(Dataclient.class);
    }
}
