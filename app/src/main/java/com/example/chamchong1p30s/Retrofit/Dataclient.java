package com.example.chamchong1p30s.Retrofit;

import com.example.chamchong1p30s.nhanvien.Nhanvien;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface Dataclient {
//    @Headers({"Content-Type: multipart/form-data"})
    @Multipart
    @POST("upload/finger_user/{manhanvien}")
    Call <JsonObject> uploadPhoto(@Part MultipartBody.Part photo, @Path("manhanvien") String manhanvien);

//    @Headers("Content-Type: application/json")
    @GET("user/{manhanvien}")
    Call<ArrayList<Nhanvien>> getUser_vantay(@Path("manhanvien") String manhanvien);

//    @Headers("Content-Type: application/json")
    @POST("user_chamcong/{manhanvien}/{macuahang}")
    Call<ArrayList<Nhanvien>> send_chamcong_nhanvien(@Path("manhanvien") String manhanvien, @Path("macuahang") String macuahang);

//    @Headers("Content-Type: application/json")
    @POST("danhnhap_android/{storename}/{password}/{serialThietbi}")
    Call<ArrayList<Nhanvien>> login(@Path("storename") String storename, @Path("password") String password, @Path("serialThietbi") String serialThietbi);

//    @Headers("Content-Type: application/json")
    @GET("user_loadchamcong/{ngaychamcong}/{macuahang}")
    Call<ArrayList<Nhanvien>> load_chamcong_nhanvien(@Path("ngaychamcong") String ngaychamcong, @Path("macuahang") String macuahang);

//    @Headers("Content-Type: application/json")
    @GET("all_users/{macuahang}")
    Call <ArrayList<Nhanvien>> getUsers(  @Path("macuahang") String macuahang);

//    @Headers("Content-Type: application/json")
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}
// route ko co dau /