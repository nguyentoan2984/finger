package com.example.chamchong1p30s.nhanvien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chamchong1p30s.MainActivity;
import com.example.chamchong1p30s.R;
import com.example.chamchong1p30s.util.TimeUtils;

import java.util.List;

import okhttp3.MultipartBody;

public class NhanVienAdapter extends BaseAdapter {
     private MainActivity context;
     private int layout;
     private List<Nhanvien> nhanvienList;

    public NhanVienAdapter(MainActivity context, int layout, List<Nhanvien> nhanvienList) {
        this.context = context;
        this.layout = layout;
        this.nhanvienList = nhanvienList;
    }


    @Override
    public int getCount() {
        return nhanvienList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private  class ViewHolder{
        TextView txtHoten, txtManv,txtVanTay;
        ImageView imgAddVantay;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder holder ;

        if(view==null){
            holder=new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.txtHoten=(TextView)view.findViewById(R.id.txt_name_nhanvien_custom);
            holder.txtManv=(TextView)view.findViewById(R.id.txt_mnv_nhanvien_custom);
//            holder.txtVanTay=(TextView)view.findViewById(R.id.txt_vantay_nhanvien_custom);
            holder.imgAddVantay= (ImageView) view.findViewById(R.id.imgAdd_vantay_custom);
            view.setTag(holder);
        }else {
            holder=(ViewHolder) view.getTag();
        }

        final Nhanvien nhanvien = nhanvienList.get(i);
        holder.txtHoten.setText(nhanvien.getTen_nhanvien());
        holder.txtManv.setText("Mã Nv: " + nhanvien.getMa_nhanvien());
//        holder.txtVanTay.setText("Số Vt: "+ String.valueOf(nhanvien.getId_vantay()));

        holder.imgAddVantay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MainActivity.m_szHost.Run_CmdCancel();
                if (TimeUtils.isFastClick()) {
                    return;
                }

                if(MainActivity.m_szHost.m_bCmdDone==false) {
                    Toast.makeText(context, "Đang xử lý, bạn vui lòng chờ hoặc nhấn Cancel và thực hiện lại !", Toast.LENGTH_LONG).show();
                    return;
                }
                if(nhanvien.getImage_finger().size()>=3){
                    Toast.makeText(context, "Chỉ được lưu tối đa 3 dấu vân tay cho một nhân viên!\r\nNếu máy chưa có dữ liệu vân tay của bạn,vui lòng nhập mã nhân viên và nhấn tải vân tay về.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(MainActivity.check_database_sumId(nhanvien.getMa_nhanvien())>=3){
                    Toast.makeText(context, "Chỉ được lưu tối đa 3 dấu vân tay cho một nhân viên !", Toast.LENGTH_LONG).show();
                    return;
                }

                if(MainActivity.m_szHost.OpenDevice("/dev/ttyMT3",115200)==0){
                    MainActivity.m_szHost.Run_CmdGetEmptyID(nhanvien.getMa_nhanvien(),nhanvien.getTen_nhanvien(),"vantayLocal",null);
                }


            }
        });

        return view;
    }

}
