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

public class ChamCong_NhanVienAdapter extends BaseAdapter {
     private MainActivity context;
     private int layout;
     private List<Nhanvien> nhanvienList;

    public ChamCong_NhanVienAdapter(MainActivity context, int layout, List<Nhanvien> nhanvienList) {
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
        TextView txtHoten, txtManv,txtTimein,txtTimeout;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder holder ;

        if(view==null){
            holder=new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.txtHoten=(TextView)view.findViewById(R.id.txt_name_nhanvien_chamcong_custom);
            holder.txtManv=(TextView)view.findViewById(R.id.txt_mnv_nhanvien_chamcong_custom);
            holder.txtTimein=(TextView)view.findViewById(R.id.txt_time_in_chamcong_custom);
            holder.txtTimeout=(TextView)view.findViewById(R.id.txt_time_out_chamcong_custom);
            view.setTag(holder);
        }else {
            holder=(ViewHolder) view.getTag();
        }

        final Nhanvien nhanvien = nhanvienList.get(i);
        holder.txtHoten.setText(nhanvien.getTen_nhanvien());
        holder.txtManv.setText("Mã Nv: " + nhanvien.getMa_nhanvien());
        holder.txtTimein.setText( nhanvien.getTime_in());
        holder.txtTimeout.setText( nhanvien.getTime_out());

        return view;
    }

}
