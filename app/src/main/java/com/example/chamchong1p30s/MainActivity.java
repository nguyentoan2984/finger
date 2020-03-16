package com.example.chamchong1p30s;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chamchong1p30s.Retrofit.APIUtils;
import com.example.chamchong1p30s.Retrofit.Database;
import com.example.chamchong1p30s.Retrofit.Dataclient;
import com.example.chamchong1p30s.nhanvien.ChamCong_NhanVienAdapter;
import com.example.chamchong1p30s.nhanvien.NhanVienAdapter;
import com.example.chamchong1p30s.nhanvien.Nhanvien;
import com.example.chamchong1p30s.util.DevComm;
import com.example.chamchong1p30s.util.FingerLib;
import com.example.chamchong1p30s.util.PowerUtil;
import com.example.chamchong1p30s.util.TimeUtils;
import com.google.gson.JsonArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    public static FingerLib m_szHost;
    int m_nUserID;
    int m_nBaudrate;
    //////

    public  static  Database database;
    LinearLayout linearlayout_quanlynhanvien,linearlayout_chamcong,linearLayout_cham_xem_chamcong ;
    List<Nhanvien> nhanvienArrayList;
    NhanVienAdapter arrayAdapter;
    ListView listView_nhanvien;

    public static List<Nhanvien> chamcongArraylist_time_in_out;
    public static ChamCong_NhanVienAdapter chamCong_nhanVienAdapter;
    ListView listView_chamcong_nhanvien;
    ////////
    String m_strPost;
    String m_szDevice;
    public final static String domain="http://192.168.1.5:3000";
//    public final static String domain="http://colus.com.vn";


    // Controls
    Button m_btnOpenDevice, m_btnCloseDevice, m_btnEnroll, m_btnVerify, m_btnIdentify, m_btnIdentifyFree;
    Button m_btnCaptureImage, m_btnCancel, m_btnGetUserCount, m_btnGetEmptyID, m_btnDeleteID, m_btnDeleteAll, m_btnReadTemplate;
    Button m_btnWriteTemplate, m_btnGetFWVer, m_btnSetDevPass, m_btnVerifyPass, m_btnVerifyImage, m_btnIdentifyImage;
    Button m_btnGetFeature;
    EditText m_editUserID, m_editDevPassword, m_editManvVantay;
    TextView m_txtStatus;
    ImageView m_FpImageViewer;
    Button btn_cancel_add_vantay, btn_chamcong_vantay, btn_taive_vantay;
    Button btn_dialog_cancel_yes, btn_dialog_cancel_no;

    Spinner m_spBaudrate, m_spDevice;
    public static ProgressBar progressBar_loading;

    private ScrollView innerScrollView;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private String TAG = "MainActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_chamcong:
                    linearLayout_cham_xem_chamcong.setVisibility(View.VISIBLE);
                    linearlayout_quanlynhanvien.setVisibility(View.GONE);
                    btn_cancel_add_vantay.setVisibility(View.GONE);
                    btn_chamcong_vantay.setVisibility(View.VISIBLE);
                    OnCancelBtn();
                    return true;
                case R.id.navigation_qlnv:
                    linearLayout_cham_xem_chamcong.setVisibility(View.GONE);
                    linearlayout_quanlynhanvien.setVisibility(View.VISIBLE);
                    btn_cancel_add_vantay.setVisibility(View.VISIBLE);
                    btn_chamcong_vantay.setVisibility(View.GONE);
                    nhanvienArrayList.clear();
                    load_dsnhanvien("chphunhuan");
                    OnCancelBtn();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 检测权限 Check permission
        int permission = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，会弹出对话框 No permission, a dialog will pop up
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 2);
        }




        InitView();
        SetInitialState();

        innerScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                innerScrollView.getParent()
                        .getParent()
                        .getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

//        m_spBaudrate.setOnItemSelectedListener(this);
//        m_spDevice.setOnItemSelectedListener(this);
//        m_spBaudrate.setSelection(4);
//        m_spDevice.setSelection(9);


        linearlayout_chamcong = (LinearLayout)findViewById(R.id.linearlayout_chamcong) ;
        linearLayout_cham_xem_chamcong = (LinearLayout)findViewById(R.id.linearlayout_cham_xem_chamcong) ;
        linearlayout_quanlynhanvien  = (LinearLayout) findViewById(R.id.linearlayout_quanlynhanvien);
        listView_nhanvien= (ListView)findViewById(R.id.listview_nhanvien);
        listView_chamcong_nhanvien=(ListView)findViewById(R.id.listview_chamcong_time_in_out) ;
        linearLayout_cham_xem_chamcong.setVisibility(View.VISIBLE);
        linearlayout_quanlynhanvien.setVisibility(View.GONE);
        linearlayout_chamcong.setVisibility(View.GONE);
        btn_cancel_add_vantay.setVisibility(View.GONE);
        btn_chamcong_vantay.setVisibility(View.VISIBLE);
        progressBar_loading.setVisibility(View.GONE);


        creatDatabase();
        nhanvienArrayList=new ArrayList<>();
        arrayAdapter=new NhanVienAdapter(this,R.layout.dong_nhanven,nhanvienArrayList);
        listView_nhanvien.setAdapter(arrayAdapter);

        chamcongArraylist_time_in_out=new ArrayList<>();
        chamCong_nhanVienAdapter=new ChamCong_NhanVienAdapter(this,R.layout.dong_chamcong_nhanven,chamcongArraylist_time_in_out);
        listView_chamcong_nhanvien.setAdapter((chamCong_nhanVienAdapter));



    }

    public void creatDatabase() {
            database = new Database(this,"1p30s.sqlite",null,1);
            database.QueryData("CREATE TABLE IF NOT EXISTS NhanVien(Id INTEGER PRIMARY KEY AUTOINCREMENT,ten_nhanvien VARCHAR, ma_nhanvien VARCHAR(60),id_vantay INTEGER ) ");
    }


    public void InitView() {
        m_FpImageViewer = (ImageView) findViewById(R.id.ivImageViewer);
        m_btnOpenDevice = (Button) findViewById(R.id.btnOpenDevice);
        m_btnCloseDevice = (Button) findViewById(R.id.btnCloseDevice);
        m_btnEnroll = (Button) findViewById(R.id.btnEnroll);
        m_btnVerify = (Button) findViewById(R.id.btnVerify);
        m_btnIdentify = (Button) findViewById(R.id.btnIdentify);
        m_btnIdentifyFree = (Button) findViewById(R.id.btnIdentifyFree);
        m_btnCaptureImage = (Button) findViewById(R.id.btnCaptureImage);
        m_btnCancel = (Button) findViewById(R.id.btnCancel);
        m_btnGetUserCount = (Button) findViewById(R.id.btnGetEnrollCount);
        m_btnGetEmptyID = (Button) findViewById(R.id.btnGetEmptyID);
        m_btnDeleteID = (Button) findViewById(R.id.btnRemoveTemplate);
        m_btnDeleteAll = (Button) findViewById(R.id.btnRemoveAll);
        m_btnReadTemplate = (Button) findViewById(R.id.btnReadTemplate);
        m_btnWriteTemplate = (Button) findViewById(R.id.btnWriteTemplate);
        m_btnGetFWVer = (Button) findViewById(R.id.btnGetFWVer);
        m_btnSetDevPass = (Button) findViewById(R.id.btnSetDevPass);
        m_btnVerifyPass = (Button) findViewById(R.id.btnVerifyPass);
        m_btnVerifyImage = (Button) findViewById(R.id.btnVerifyImage);
        m_btnGetFeature = (Button) findViewById(R.id.btnGetFeature);
        m_btnIdentifyImage = (Button) findViewById(R.id.btnIdentifyImage);
        m_txtStatus = (TextView) findViewById(R.id.txtStatus);
        m_editUserID = (EditText) findViewById(R.id.editUserID);
        m_editDevPassword = (EditText) findViewById(R.id.editDevPassword);
        m_spBaudrate = (Spinner) findViewById(R.id.spnBaudrate);
        m_spDevice = (Spinner) findViewById(R.id.spnDevice);
        innerScrollView = (ScrollView) findViewById(R.id.innerScrollView);

        btn_cancel_add_vantay= (Button)findViewById(R.id.button_cancel_add_vantay);
        btn_cancel_add_vantay.setOnClickListener(this);
        btn_chamcong_vantay=(Button)findViewById(R.id.button_chamcong_vantay);
        btn_chamcong_vantay.setOnClickListener(this);
        btn_taive_vantay= (Button)findViewById(R.id.button_taive_vantay);
        btn_taive_vantay.setOnClickListener(this);
        m_editManvVantay= (EditText)findViewById(R.id.edt_nhanvien_tai_vantay) ;
        progressBar_loading=(ProgressBar)findViewById(R.id.progressBar);

        m_btnOpenDevice.setOnClickListener(this);
        m_btnCloseDevice.setOnClickListener(this);
        m_btnEnroll.setOnClickListener(this);
        m_btnVerify.setOnClickListener(this);
        m_btnIdentify.setOnClickListener(this);
        m_btnIdentifyFree.setOnClickListener(this);
        m_btnCaptureImage.setOnClickListener(this);
        m_btnCancel.setOnClickListener(this);
        m_btnGetUserCount.setOnClickListener(this);
        m_btnGetEmptyID.setOnClickListener(this);
        m_btnDeleteID.setOnClickListener(this);
        m_btnDeleteAll.setOnClickListener(this);
        m_btnReadTemplate.setOnClickListener(this);
        m_btnWriteTemplate.setOnClickListener(this);
        m_btnGetFWVer.setOnClickListener(this);
        m_btnSetDevPass.setOnClickListener(this);
        m_btnVerifyPass.setOnClickListener(this);
        m_btnVerifyImage.setOnClickListener(this);
        m_btnGetFeature.setOnClickListener(this);
        m_btnIdentifyImage.setOnClickListener(this);

        if (m_szHost == null) {
            m_szHost = new FingerLib(this, m_txtStatus, m_FpImageViewer, runEnableCtrl, m_spDevice);
        } else {
            m_szHost.SZOEMHost_Lib_Init(this, m_txtStatus ,m_FpImageViewer, runEnableCtrl, m_spDevice);
        }
    }


    public void onClick(View view) {
        if (view == m_btnOpenDevice) {
            // Multiple clicks within 2 seconds only respond once
            if (TimeUtils.isFastClick()) {
                return;
            }
            OnOpenDeviceBtn();
        } else if (view == m_btnCloseDevice) {
            OnCloseDeviceBtn();
        } else if (view == m_btnEnroll) {
            OnEnrollBtn();
        } else if (view == m_btnVerify) {
            OnVerifyBtn();
        } else if (view == m_btnIdentify) {
            OnIdentifyBtn();
        } else if (view == m_btnIdentifyFree) {
            OnIdentifyFreeBtn();
        } else if (view == m_btnCaptureImage) {
            OnUpImage();
        } else if (view == m_btnCancel) {
            OnCancelBtn();
        } else if (view == m_btnGetUserCount) {
            OnGetUserCount();
        } else if (view == m_btnGetEmptyID) {
            OnGetEmptyID();
        } else if (view == m_btnDeleteID) {
            OnDeleteIDBtn();
        } else if (view == m_btnDeleteAll) {
            OnDeleteAllBtn();
        } else if (view == m_btnReadTemplate) {
            OnReadTemplateBtn();
        } else if (view == m_btnWriteTemplate) {
            OnWriteTemplateBtn();
        } else if (view == m_btnGetFWVer) {
            OnGetFwVersion();
        } else if (view == m_btnSetDevPass) {
            OnSetDevPass();
        } else if (view == m_btnVerifyPass) {
            OnVerifyPassBtn();
        } else if (view == m_btnVerifyImage) {
            OnVerifyWithImage();
        } else if (view == m_btnIdentifyImage) {
            OnIdentifyWithImage();
        } else if (view == m_btnGetFeature) {
            OnGetFeature();
        } else if (view == btn_cancel_add_vantay) {
            progressBar_loading.setVisibility(View.GONE);
            OnCancelBtn();
        } else if (view == btn_chamcong_vantay) {
            if(MainActivity.m_szHost.m_bCmdDone==false) {
                Toast.makeText(this, "Đã trong chế độ chấm công !", Toast.LENGTH_LONG).show();
                return;
            }
            if(MainActivity.m_szHost.OpenDevice("/dev/ttyMT3",115200)==0){
//                    MainActivity.m_szHost.Run_CmdCancel();
//                MainActivity.m_szHost.Run_CmdGetEmptyID(nhanvien.getMa_nhanvien(),nhanvien.getTen_nhanvien(),"vantayLocal");
                EnableCtrl(true);
                m_btnOpenDevice.setEnabled(false);
                m_btnCloseDevice.setEnabled(true);
                OnIdentifyFreeBtn();
            }
//            OnIdentifyFreeBtn();


        } else if (view == btn_taive_vantay) {
            if (TimeUtils.isFastClick()) {
                return;
            }
            String ma_nhanvien_tai_vantay = m_editManvVantay.getText().toString().trim();
            if(ma_nhanvien_tai_vantay.equals("1234")){
                if(MainActivity.m_szHost.OpenDevice("/dev/ttyMT3",115200)==0) {
                    OnDeleteAllBtn();
                }

                return;
            }
            if(ma_nhanvien_tai_vantay.equals("4321")){
                m_editUserID.setText("1");
                if(MainActivity.m_szHost.OpenDevice("/dev/ttyMT3",115200)==0) {
                    OnWriteTemplateBtn();
                }
                return;
            }

           if(!ma_nhanvien_tai_vantay.isEmpty())
           {
               if(MainActivity.m_szHost.m_bCmdDone==false) {
                   Toast.makeText(this, "Đang xử lý, bạn vui lòng chờ hoặc nhấn Cancel và thực hiện lại !", Toast.LENGTH_LONG).show();
                   return;
               }

               if(check_database_manhanvien(ma_nhanvien_tai_vantay).equals(ma_nhanvien_tai_vantay)){
                   Toast.makeText(this, "Mã nhân viên đã tồn tại trong thiết bị !", Toast.LENGTH_LONG).show();
                   return;
               }

                   if(MainActivity.m_szHost.OpenDevice("/dev/ttyMT3",115200)==0) {
                       getArray_fingerUser(ma_nhanvien_tai_vantay);

                   }


           }else {
               Toast.makeText(this, "Vui lòng nhập mã nhân viên", Toast.LENGTH_SHORT).show();
           }

        }
    }
    public String check_database_manhanvien( String manhanvien) {
        String maNV_return="";
        Cursor cursor = MainActivity.database.GetData("SELECT NhanVien.ma_nhanvien FROM NhanVien WHERE ma_nhanvien='" + manhanvien + "' ");
        while (cursor.moveToNext()){
            maNV_return = cursor.getString(0);
        }
//        Toast.makeText(mContext, maNV_return, Toast.LENGTH_SHORT).show();
        Log.d("Add Finger","add finger insert database No oK id"+String.valueOf(maNV_return)+ String.valueOf(maNV_return));

        return  maNV_return;

    }
    public static int check_database_sumId( String manhanvien) {
        int sumId=0;
        ArrayList arrayListSumid= new ArrayList();
        Cursor cursor = MainActivity.database.GetData("SELECT NhanVien.id_vantay FROM NhanVien WHERE ma_nhanvien='" + manhanvien + "' ");
        while (cursor.moveToNext()){
            arrayListSumid .add( cursor.getString(0));
        }
//        Toast.makeText(mContext, maNV_return, Toast.LENGTH_SHORT).show();
        sumId=arrayListSumid.size();
        Log.d("Add Finger","add finger check sumId database"+String.valueOf(sumId));

        return  sumId;

    }


    public  void load_dsnhanvien(String macuahang){
        progressBar_loading.setVisibility(View.VISIBLE);

        Dataclient dataclient = APIUtils.getdata();

        Call<ArrayList<Nhanvien>> call = dataclient.getUsers(macuahang);

        call.enqueue(new Callback<ArrayList<Nhanvien>>() {
            @Override
            public void onResponse(Call<ArrayList<Nhanvien>> call, Response<ArrayList<Nhanvien>> response) {
                if(response.isSuccessful()){
                    nhanvienArrayList.clear();
                    for (int i = 0; i < response.body().size(); i++) {
                        nhanvienArrayList.add(response.body().get(i));
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
                progressBar_loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ArrayList<Nhanvien>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ket noi toi Server bi loi !!", Toast.LENGTH_SHORT).show();
                Log.e("Error", "Load danh sach nhan vien Error");
                progressBar_loading.setVisibility(View.GONE);

            }
        });
    }

    public  void getArray_fingerUser(String ma_nhanvien_tai_vantay){
//        Toast.makeText(this, ma_nhanvien_tai_vantay, Toast.LENGTH_SHORT).show();
        Dataclient dataclient = APIUtils.getdata();

        Call <ArrayList<Nhanvien>> call =dataclient.getUser_vantay( ma_nhanvien_tai_vantay);

        call.enqueue(new Callback<ArrayList<Nhanvien>>() {
            @Override
            public void onResponse(Call<ArrayList<Nhanvien>> call, Response<ArrayList<Nhanvien>> response) {
//                Toast.makeText(MainActivity.this, response.body().get(0).toString(), Toast.LENGTH_SHORT).show();
                    if(response.isSuccessful()){
                        m_szHost.getImage_fingerUser(response.body());
                    }
            }

            @Override
            public void onFailure(Call<ArrayList<Nhanvien>> call, Throwable t) {
                Log.e("aaa", "error");
                Toast.makeText(MainActivity.this, "Mã nhân viên không tồn tại.\r\nHoặc đường truyền mạng có vấn đề.\r\nVui lòng thử lại.", Toast.LENGTH_SHORT).show();

            }
        });
    }



    public  void EnableCtrl(boolean bEnable) {
        m_btnEnroll.setEnabled(bEnable);
        m_btnVerify.setEnabled(bEnable);
        m_btnIdentify.setEnabled(bEnable);
        m_btnIdentifyFree.setEnabled(bEnable);
        //        m_btnCancel.setEnabled(bEnable);
        m_btnGetUserCount.setEnabled(bEnable);
        m_btnGetEmptyID.setEnabled(bEnable);
        m_btnDeleteID.setEnabled(bEnable);
        m_btnDeleteAll.setEnabled(bEnable);
        m_btnReadTemplate.setEnabled(bEnable);
        m_btnWriteTemplate.setEnabled(bEnable);
        m_btnCaptureImage.setEnabled(bEnable);
        m_btnGetFWVer.setEnabled(bEnable);
        m_btnSetDevPass.setEnabled(bEnable);
        m_btnVerifyPass.setEnabled(bEnable);
        m_btnVerifyImage.setEnabled(bEnable);
        m_btnIdentifyImage.setEnabled(bEnable);
        m_btnGetFeature.setEnabled(bEnable);

        m_editUserID.setEnabled(bEnable);
        m_editDevPassword.setEnabled(bEnable);
    }

    public void OnGetFeature() {
        EnableCtrl(false);
        m_btnCloseDevice.setEnabled(false);
        m_btnCancel.setEnabled(true);

        m_szHost.Run_CmdGetFeatureOfCapturedFP();
    }

    public void SetInitialState() {
//        m_txtStatus.setText("Please open device!");
        m_txtStatus.setText("Wellcome !");

        m_btnOpenDevice.setEnabled(true);
        m_btnCloseDevice.setEnabled(false);
        EnableCtrl(false);
        m_btnCancel.setEnabled(false);

    }

    public  void OnOpenDeviceBtn() {
        if (m_szHost.OpenDevice(m_szDevice, m_nBaudrate) == 0) {
            EnableCtrl(true);
            m_btnOpenDevice.setEnabled(false);
            m_btnCloseDevice.setEnabled(true);
        }
    }

    public void OnCloseDeviceBtn() {
        m_szHost.CloseDevice();

        SetInitialState();
    }


    public void OnEnrollBtn() {
        int w_nTemplateNo;
        w_nTemplateNo = GetInputTemplateNo();
        if (w_nTemplateNo < 0) {
            return;
        }

        EnableCtrl(false);
        m_btnCloseDevice.setEnabled(false);
        m_btnCancel.setEnabled(true);

        m_szHost.Run_CmdEnroll(w_nTemplateNo);
    }



    public  void OnIdentifyBtn() {
        EnableCtrl(false);
        m_btnCloseDevice.setEnabled(false);
        m_btnCancel.setEnabled(true);

        m_szHost.Run_CmdIdentify();
    }

    public void OnIdentifyFreeBtn() {
        EnableCtrl(false);
        m_btnCloseDevice.setEnabled(false);
        m_btnCancel.setEnabled(true);

        m_szHost.Run_CmdIdentifyFree();
    }

    public void OnVerifyBtn() {
        int w_nTemplateNo;

        w_nTemplateNo = GetInputTemplateNo();
        if (w_nTemplateNo < 0) {
            return;
        }

        EnableCtrl(false);
        m_btnCloseDevice.setEnabled(false);
        m_btnCancel.setEnabled(true);

        m_szHost.Run_CmdVerify(w_nTemplateNo);
    }

    //    public void OnEnrollOneTime(){
    //        int w_nTemplateNo;
    //
    //        w_nTemplateNo = GetInputTemplateNo();
    //        if (w_nTemplateNo < 0)
    //            return;
    //
    //        EnableCtrl(false);
    //        m_btnCloseDevice.setEnabled(false);
    //        m_btnCancel.setEnabled(true);
    //
    //        m_szHost.Run_CmdEnrollOneTime(w_nTemplateNo);
    //    }

    //    public void OnChangeTemplate(){
    //        int w_nTemplateNo;
    //
    //        w_nTemplateNo = GetInputTemplateNo();
    //        if (w_nTemplateNo < 0)
    //            return;
    //
    //        EnableCtrl(false);
    //        m_btnCloseDevice.setEnabled(false);
    //        m_btnCancel.setEnabled(true);
    //
    //        m_szHost.Run_CmdChangeTemplate(w_nTemplateNo);
    //    }

    public void OnDeleteIDBtn() {
        int w_nTemplateNo;

        w_nTemplateNo = GetInputTemplateNo();
        if (w_nTemplateNo < 0) {
            return;
        }

        m_szHost.Run_CmdDeleteID(w_nTemplateNo);
    }

    public void OnDeleteAllBtn() {
        m_szHost.Run_CmdDeleteAll();
    }

    public void OnReadTemplateBtn() {
        int w_nTemplateNo;

        w_nTemplateNo = GetInputTemplateNo();
        if (w_nTemplateNo < 0) {
            return;
        }

        if (m_szHost.Run_CmdReadTemplate(w_nTemplateNo) == 2) {
            OnCloseDeviceBtn();
        }
    }

    public void OnWriteTemplateBtn() {
        int w_nTemplateNo;

        w_nTemplateNo = GetInputTemplateNo();
        if (w_nTemplateNo < 0) {
            return;
        }

        if (m_szHost.Run_CmdWriteTemplate(w_nTemplateNo) == 2) {
            OnCloseDeviceBtn();
        }
    }

    public   void OnGetEmptyID() {
//        m_szHost.Run_CmdGetEmptyID();
        Toast.makeText(this, " Da thay the ko su dung", Toast.LENGTH_SHORT).show();
    }

    public void OnGetUserCount() {
        m_szHost.Run_CmdGetUserCount();
    }

    //    public void OnGetBrokenTemplate(){
    //        m_szHost.Run_CmdGetBrokenTemplate();
    //    }

    //    public void OnReadTemplate()
    //    {
    //        int w_nTemplateNo;
    //
    //        w_nTemplateNo = GetInputTemplateNo();
    //        if (w_nTemplateNo < 0)
    //            return;
    //
    //        m_szHost.Run_CmdReadTemplate(w_nTemplateNo);
    //    }

    //    public void OnWriteTemplate()
    //    {
    //        int w_nTemplateNo;
    //
    //        w_nTemplateNo = GetInputTemplateNo();
    //        if (w_nTemplateNo < 0)
    //            return;
    //
    //        m_szHost.Run_CmdWriteTemplate(w_nTemplateNo);
    //    }

    //    public void OnSetParameter()
    //    {
    //        m_szHost.Run_CmdSetParameter();
    //    }

    public void OnGetFwVersion() {
        m_szHost.Run_CmdGetFwVersion();
    }

    //    public void OnDetectFingerBtn(){
    //        EnableCtrl(false);
    //        m_btnCloseDevice.setEnabled(false);
    //        m_btnCancel.setEnabled(true);
    //
    //        m_szHost.Run_CmdDetectFinger();
    //    }

    public void OnSetDevPass() {
        if (m_editDevPassword.length() != 0 && m_editDevPassword.length() != 14) {
            m_strPost = getString(R.string.input_device_password);
            m_txtStatus.setText(m_strPost);
            EnableCtrl(true);
            return;
        }

        m_szHost.Run_CmdSetDevPass(m_editDevPassword.getText().toString());
    }

    public void OnVerifyPassBtn() {
        if (m_editDevPassword.length() != 14) {
            m_strPost = getString(R.string.input_device_password);
            m_txtStatus.setText(m_strPost);
            EnableCtrl(true);
            return;
        }

        m_szHost.Run_CmdVerifyPass(m_editDevPassword.getText().toString());
    }

    //    public void OnExitDevPass()
    //    {
    //        m_szHost.Run_CmdExitDevPass();
    //    }

    //    public void OnAdjustSensor()
    //    {
    //        m_szHost.Run_CmdAdjustSensor();
    //    }

    //    public void OnEnterStandByMode()
    //    {
    //        m_szHost.Run_CmdEnterStandByMode();
    //    }

    public void OnCancelBtn() {
        m_btnCloseDevice.setEnabled(false);

        m_szHost.Run_CmdCancel();
    }

    //    public void OnGetFeatureOfCapturedFP()
    //    {
    //        m_szHost.Run_CmdGetFeatureOfCapturedFP();
    //    }

    //    public void OnIdentifyWithTemplate2()
    //    {
    //        m_szHost.Run_CmdIdentifyWithTemplate2();
    //    }

    public void OnUpImage() {
        EnableCtrl(false);
        m_btnCloseDevice.setEnabled(false);
        m_btnCancel.setEnabled(true);

        m_szHost.Run_CmdUpImage();
    }

    public void OnIdentifyWithImage() {
        m_szHost.Run_CmdIdentifyWithImage();
    }

    public void OnVerifyWithImage() {
        int w_nTemplateNo;

        w_nTemplateNo = GetInputTemplateNo();
        if (w_nTemplateNo < 0) {
            return;
        }

        m_szHost.Run_CmdVerifyWithImage(w_nTemplateNo);
    }

    //    public void OnVerifyWithDownTmpl()
    //    {
    //        int w_nTemplateNo;
    //
    //        w_nTemplateNo = GetInputTemplateNo();
    //        if (w_nTemplateNo < 0)
    //            return;
    //
    //        m_szHost.Run_CmdVerifyWithDownTmpl(w_nTemplateNo);
    //    }

    //    public void OnIdentifyWithDownTmpl()
    //    {
    //        m_szHost.Run_CmdIdentifyWithDownTmpl();
    //    }

    //    public void OnEnterISPMode()
    //    {
    //        m_szHost.Run_CmdEnterISPMode();
    //    }

    public int GetInputTemplateNo() {
        String str;

        str = m_editUserID.getText().toString();

        if (str.isEmpty()) {
            m_txtStatus.setText(getString(R.string.input_userid));
            return -1;
        }

        try {
            m_nUserID = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            m_txtStatus.setText(String.format(getString(R.string.input_userid_rangle),
                    (short) DevComm.GD_MAX_RECORD_COUNT));
            return -1;
        }

        return m_nUserID;
    }

    Runnable runEnableCtrl = new Runnable() {
        public void run() {
            EnableCtrl(true);
            m_btnOpenDevice.setEnabled(false);
            m_btnCloseDevice.setEnabled(true);
            m_btnCancel.setEnabled(false);
        }
    };
    public void openDialog() {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_cancel);
        dialog.show();
        btn_dialog_cancel_yes=(Button)dialog.findViewById(R.id.dialog_ok);
        btn_dialog_cancel_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Thoat chuong trinh", Toast.LENGTH_SHORT).show();
                finish();
                System.exit(0);
            }
        });
        btn_dialog_cancel_no=(Button)dialog.findViewById(R.id.dialog_cancel);
        btn_dialog_cancel_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
        });


    }


    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event) {
//        Toast.makeText(this,String.valueOf(m_btnCancel.isEnabled())+String.valueOf(m_btnCloseDevice.isEnabled()), Toast.LENGTH_SHORT).show();
        switch (KeyCode) {
            case KeyEvent.KEYCODE_BACK:
//                if (event.getRepeatCount() == 0) {
//                    if (m_btnCancel.isEnabled()) {
//                        Toast.makeText(this, "Bạn đã thoát ứng dụng 1!", Toast.LENGTH_SHORT).show();
////                        Toast.makeText(this, getString(R.string.cancle_command), Toast.LENGTH_LONG);
//                        OnCancelBtn();
////                        finish();
////                        System.exit(0);
////                        return true;
//                    }
//                    if (m_btnCloseDevice.isEnabled()) {
//                        Toast.makeText(this, "Bạn đã thoát ứng dụng 2!", Toast.LENGTH_SHORT).show();
//                         OnCloseDeviceBtn();
////                        finish();
////                        System.exit(0);
//                    }
//                }
                openDialog();
//                finish();
//                System.exit(0);
                break;
        }

        return super.onKeyDown(KeyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        PowerUtil.power("1");


    }

    @Override
    public void onPause() {
        super.onPause();
        PowerUtil.power("0");

    }



    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnDevice:
                m_szDevice = m_spDevice.getItemAtPosition(position).toString();
                break;
            case R.id.spnBaudrate:
                Log.d("onItemSelected", "item: " + parent.getSelectedItem() + "");
                m_nBaudrate = Integer.parseInt(parent.getSelectedItem().toString());
                break;
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
