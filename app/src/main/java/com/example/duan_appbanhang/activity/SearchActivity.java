package com.example.duan_appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duan_appbanhang.R;
import com.example.duan_appbanhang.adapter.DienThoaiAdapter;
import com.example.duan_appbanhang.mode.SanPhamMoi;
import com.example.duan_appbanhang.retrfit.ApiBanHang;
import com.example.duan_appbanhang.retrfit.RetrofitClient;
import com.example.duan_appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edtsearch;

    DienThoaiAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        initView();
        ActionToolBar();


    }

    private void initView() {
        sanPhamMoiList = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toobbartimkiem);
        edtsearch = findViewById(R.id.edtsearch);
        recyclerView = findViewById(R.id.recycleview_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0 ){
                    sanPhamMoiList.clear();
                    adapterDt = new DienThoaiAdapter(getApplicationContext(),sanPhamMoiList);
                    recyclerView.setAdapter(adapterDt);

                }else {
                    getDataSearch(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

    }

    private void getDataSearch(String s) {
        sanPhamMoiList.clear();

        compositeDisposable.add(apiBanHang.search(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                sanPhamMoiList = sanPhamMoiModel.getResult();
                                adapterDt = new DienThoaiAdapter(getApplicationContext(),sanPhamMoiList);
                                recyclerView.setAdapter(adapterDt);
                            }else {
                                Toast.makeText(this, sanPhamMoiModel.getMessage(), Toast.LENGTH_SHORT).show();
                                sanPhamMoiList.clear();
                                adapterDt.notifyDataSetChanged();
                            }

                        },throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}