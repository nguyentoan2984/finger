package com.example.chamchong1p30s.nhanvien;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Nhanvien {
    private String ten_nhanvien;
    private String ma_nhanvien;
    private int id_vantay;
    private String time_in;
    private String time_out;
    private ArrayList image_finger;
    private String control;


    public Nhanvien(String ten_nhanvien, String ma_nhanvien, int id_vantay, String time_in, String time_out, ArrayList image_finger,String control) {
        this.ten_nhanvien = ten_nhanvien;
        this.ma_nhanvien = ma_nhanvien;
        this.id_vantay = id_vantay;
        this.time_in = time_in;
        this.time_out = time_out;
        this.image_finger = image_finger;
    }

    public String getTen_nhanvien() {
        return ten_nhanvien;
    }

    public void setTen_nhanvien(String ten_nhanvien) {
        this.ten_nhanvien = ten_nhanvien;
    }

    public String getMa_nhanvien() {
        return ma_nhanvien;
    }

    public void setMa_nhanvien(String ma_nhanvien) {
        this.ma_nhanvien = ma_nhanvien;
    }

    public int getId_vantay() {
        return id_vantay;
    }

    public void setId_vantay(int id_vantay) {
        this.id_vantay = id_vantay;
    }

    public String getTime_in() {
        return time_in;
    }

    public void setTime_in(String time_in) {
        this.time_in = time_in;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }

    public ArrayList getImage_finger() {
        return image_finger;
    }

    public void setImage_finger(ArrayList image_finger) {
        this.image_finger = image_finger;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    @Override
    public String toString() {
        return "Nhanvien{" +
                "ten_nhanvien='" + ten_nhanvien + '\'' +
                ", ma_nhanvien='" + ma_nhanvien + '\'' +
                ", id_vantay=" + id_vantay +
                ", time_in='" + time_in + '\'' +
                ", time_out='" + time_out + '\'' +
                ", image_finger=" + image_finger +
                ", control='" + control + '\'' +
                '}';
    }
}
