package com.example.chamchong1p30s.Retrofit;

import com.example.chamchong1p30s.nhanvien.Nhanvien;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface Dataclient {
    @Multipart
    @POST("upload/finger_user/{manhanvien}")
//    Call <RequestBody> uploadPhoto(@Part MultipartBody.Part photo );
    Call <JsonObject> uploadPhoto(@Part MultipartBody.Part photo, @Path("manhanvien") String manhanvien);

    @GET("user/{manhanvien}")
    Call<ArrayList<Nhanvien>> getUser_vantay(@Path("manhanvien") String manhanvien);

    @POST("user_chamcong/{manhanvien}")
    Call<ArrayList<Nhanvien>> send_chamcong_nhanvien(@Path("manhanvien") String manhanvien);

    @GET("all_users/{macuahang}")
    Call <ArrayList<Nhanvien>> getUsers(@Path("macuahang") String macuahang);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}
// route ko co dau /