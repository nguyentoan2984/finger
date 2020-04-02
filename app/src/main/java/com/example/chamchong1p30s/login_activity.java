package com.example.chamchong1p30s;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chamchong1p30s.Retrofit.APIUtils;
import com.example.chamchong1p30s.Retrofit.Dataclient;
import com.example.chamchong1p30s.nhanvien.Nhanvien;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_activity extends AppCompatActivity {
    EditText edt_user, edt_pass;
    Button btn_dangnhap;
    CheckBox cb_remember;
    String m_deviceId;
    public static ProgressBar progressBar_loading;
    SharedPreferences sharedPreferences;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_PHONE_STATE"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Anhxa();
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        progressBar_loading.setVisibility(View.GONE);
        edt_user.setText(sharedPreferences.getString("username", ""));
        edt_pass.setText(sharedPreferences.getString("password", ""));
        cb_remember.setChecked(sharedPreferences.getBoolean("checked", false));
        int permission = ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，会弹出对话框 No permission, a dialog will pop up
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 2);

        }


        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getId();
                final String storename = edt_user.getText().toString().trim();
                final String password = edt_pass.getText().toString().trim();

                if (storename.isEmpty()) {
                    Toast.makeText(login_activity.this, "Vui lòng nhập tài khoản.", Toast.LENGTH_SHORT).show();
                }
                if (password.isEmpty()) {
                    Toast.makeText(login_activity.this, "Vui lòng nhập mật khẩu.", Toast.LENGTH_SHORT).show();
                }
                if (!password.isEmpty() && !storename.isEmpty()) {


                    Dataclient dataclient = APIUtils.getdata(login_activity.this);
//                    progressBar_loading.setVisibility(View.VISIBLE);
                    Call<ArrayList<Nhanvien>> call = dataclient.login(storename, password, m_deviceId);

                    call.enqueue(new Callback<ArrayList<Nhanvien>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Nhanvien>> call, Response<ArrayList<Nhanvien>> response) {

                            if (response.isSuccessful()) {
                                if (response.body().get(0).getControl().equals("success")) {
                                    progressBar_loading.setVisibility(View.GONE);

                                    if (cb_remember.isChecked()) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username", storename);
                                        editor.putString("password", password);
                                        editor.putBoolean("checked", true);
                                        editor.commit();

                                    } else {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove("username");
                                        editor.remove("password");
                                        editor.remove("checked");
                                        editor.commit();


                                    }

                                    Intent intent = new Intent(login_activity.this, MainActivity.class);
                                    intent.putExtra("macuahang", response.body().get(1).getMacuahang());
                                    startActivity(intent);
                                }
                                else if(response.body().get(0).getControl().equals("error")){

                                    Toast.makeText(login_activity.this,storename + " "+ response.body().get(1).getMacuahang() , Toast.LENGTH_SHORT).show();
                                    progressBar_loading.setVisibility(View.GONE);

                                }

                            }

//

                        }


                        @Override
                        public void onFailure(Call<ArrayList<Nhanvien>> call, Throwable t) {
                            Log.d("aaa", t.toString());
                            progressBar_loading.setVisibility(View.GONE);
                            Toast.makeText(login_activity.this, "Kết nối với serve bị lổi, kiểm tra lại mạng", Toast.LENGTH_SHORT).show();


                        }
                    });
//
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                Log.d("onItemSelected", "item: 1");

                break;
            default:
                Log.d("onItemSelected", "item: 2");

                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void Anhxa() {
        cb_remember = (CheckBox) findViewById(R.id.cb_remember);
        edt_user = (EditText) findViewById(R.id.edt_usename);
        edt_pass = (EditText) findViewById(R.id.edt_password);
        btn_dangnhap = (Button) findViewById(R.id.btn_login);
        progressBar_loading = (ProgressBar) findViewById(R.id.progressBar);

    }

    private void getId() {

        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 2);

            return;
        }
         m_deviceId = TelephonyMgr.getDeviceId();
         Log.d("aaa", m_deviceId);
    }


}
