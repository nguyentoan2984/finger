package com.example.chamchong1p30s.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient (String baseurl, Context context){


        OkHttpClient builder = new OkHttpClient.Builder()
                                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                                    .writeTimeout(5000,TimeUnit.MILLISECONDS)
                                    .connectTimeout(10000,TimeUnit.MILLISECONDS)
                                    .retryOnConnectionFailure(true)
                                    .addInterceptor(new addcookies(context))
                                    .addInterceptor(new receivedcookies(context))
                                    .build();
        Gson gson= new GsonBuilder().setLenient().create();
        retrofit =new Retrofit.Builder()
                    .baseUrl(baseurl)
                    .client(builder)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        return  retrofit;
    }

    public static class ReceivedCookiesInterceptor implements Interceptor {

        private Context context;
        public ReceivedCookiesInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet("PREF_COOKIES", new HashSet<String>());

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }

                SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
                memes.putStringSet("PREF_COOKIES", cookies).apply();
                memes.commit();
            }

            return originalResponse;
        }
    }
    public static class AddCookiesInterceptor implements Interceptor {

        public static final String PREF_COOKIES = "PREF_COOKIES";
        private Context context;

        public AddCookiesInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();

            HashSet<String> preferences = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet(PREF_COOKIES, new HashSet<String>());

            Request original = chain.request();
            if(original.url().toString().contains("distributor")){
                for (String cookie : preferences) {
                    builder.addHeader("Cookie", cookie);
                }
            }

            return chain.proceed(builder.build());
        }
    }



}
