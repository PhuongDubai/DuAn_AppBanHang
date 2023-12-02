package com.example.duan_appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan_appbanhang.R;
import com.example.duan_appbanhang.mode.GioHang;
import com.example.duan_appbanhang.mode.NotiSendData;
import com.example.duan_appbanhang.retrfit.ApiBanHang;
import com.example.duan_appbanhang.retrfit.ApiPushNotification;
import com.example.duan_appbanhang.retrfit.RetrofitClient;
import com.example.duan_appbanhang.retrfit.RetrofitClientNoti;
import com.example.duan_appbanhang.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsodt, txtemail;
    EditText edtDiachi;
    Button btnDathang , btnmomo;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();

    }

    private void countItem() {
        totalItem = 0;
        for (int i = 0; i < Utils.mangmuahang.size(); i++) {
            totalItem = totalItem + Utils.mangmuahang.get(i).getSoluong();
        }
    }



    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien", 0);
        txttongtien.setText(decimalFormat.format(tongtien));
        txtemail.setText(Utils.user_current.getEmail());
        txtsodt.setText(Utils.user_current.getMobile());
        btnDathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = edtDiachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)) {
                    Toast.makeText(ThanhToanActivity.this, "Bạn chưa nhập địa chỉ nhận hàng", Toast.LENGTH_SHORT).show();
                } else {
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.createOder(str_email, str_sdt, String.valueOf(tongtien), id, str_diachi, totalItem, new Gson().toJson(Utils.mangmuahang)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(userModel -> {
                        pushNotiToUser();
                        Toast.makeText(ThanhToanActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();

                        //clear mang gio hang
                        for (int i = 0 ; i <Utils.mangmuahang.size(); i ++){
                            GioHang gioHang = Utils.mangmuahang.get(i);
                            if (Utils.manggiohang.contains(gioHang)){
                                Utils.manggiohang.remove(gioHang);
                            }
                        }
                        Utils.mangmuahang.clear();
                        Paper.book().write("giohang", Utils.manggiohang);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }, throwable -> {
                        Toast.makeText(ThanhToanActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }));
                }
            }
        });

    }

    private void pushNotiToUser() {
        //gettoken
        compositeDisposable.add(apiBanHang.gettoken(1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(userModel -> {
            if (userModel.isSuccess()) {
                for (int i =0; i < userModel.getResult().size();i++){
                    Map<String, String> data = new HashMap<>();
                    data.put("title", "Thông báo");
                    data.put("body", "Bạn có đơn hàng mới");
                    NotiSendData notiSendData = new NotiSendData(userModel.getResult().get(i).getToken(), data);
                    ApiPushNotification apiPushNotification = RetrofitClientNoti.getInstance().create(ApiPushNotification.class);
                    compositeDisposable.add(apiPushNotification.sendNotification(notiSendData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(notiResponse -> {

                    }, throwable -> {
                        Log.d("logg", throwable.getMessage());
                    }));
                }

            }

        }, throwable -> {
            Log.d("loggg", throwable.getMessage());
        }));


    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toobbarthanhtoan);
        txttongtien = findViewById(R.id.txtTongTien_thanhtoan);
        txtsodt = findViewById(R.id.txtsodienthoai);
        txtemail = findViewById(R.id.txtemail);
        edtDiachi = findViewById(R.id.edtdiachi);
        btnDathang = findViewById(R.id.btnDatHang);
        btnmomo = findViewById(R.id.btnmomo);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}