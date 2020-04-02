package com.example.chamchong1p30s.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

public class receivedcookies implements Interceptor {
    private Context context;
    public receivedcookies(Context context) {
        this.context = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        // Log.e("namCallcookieRespon", originalResponse.toString());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet("PREF_COOKIES", new HashSet<String>());
            cookies.clear();
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
                //  Log.e("namcookie",header);
            }

            SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
            memes.putStringSet("PREF_COOKIES", cookies).apply();
            memes.commit();
            Log.e("namcookie1",cookies.toString());
        }

        return originalResponse;
    }

}
