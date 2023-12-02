package com.example.duan_appbanhang.utils;

import com.example.duan_appbanhang.mode.GioHang;
import com.example.duan_appbanhang.mode.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL = "http://10.0.2.2/banhang/";
    //  public static final String BASE_URL="http://192.168.1.162/banhang/";
    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang = new ArrayList<>();
    public static User user_current = new User();

    public static String statusOrder(int status) {
        String result = "";
        switch (status) {
            case 0:
                result = "Đơn hàng đang được xử lí";
                break;
            case 1:
                result = "Đơn hàng đã được xác nhận";
                break;
            case 2:
                result = "Đơn hàng đã được giao cho vận chuyển";
                break;
            case 3:
                result = "Đơn hàng đã được giao thành công";
                break;
            case 4:
                result = "Đơn hàng đã hủy";
                break;
            default:
                result ="...";
        }
        return result;
    }

}
