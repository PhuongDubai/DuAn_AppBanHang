package com.example.duan_appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duan_appbanhang.R;
import com.example.duan_appbanhang.retrfit.ApiBanHang;
import com.example.duan_appbanhang.retrfit.RetrofitClient;
import com.example.duan_appbanhang.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    EditText email, pass, repass, mobile, username;
    Button btndangki;
    ApiBanHang apiBanHang;
    FirebaseAuth firebaseAuth;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        initView();
        initControll();
    }

    private void initControll() {
        btndangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKi();
            }
        });

    }

    private void dangKi() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        String str_username = username.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_username)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_pass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập pass", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_repass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập re-pass", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_mobile)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập phone number", Toast.LENGTH_SHORT).show();
        } else {
            if (str_pass.equals(str_repass)) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(str_email, str_pass)
                        .addOnCompleteListener(DangKiActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        postData(str_email, str_pass, str_username, str_mobile, user.getUid());
                                    }
                                } else {
                                    Toast.makeText(DangKiActivity.this, "Email đã tồn tại hoặc không thành công", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            } else {
                Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void postData(String str_email, String str_pass, String str_username, String str_mobile, String uid) {
        compositeDisposable.add(apiBanHang.dangKi(str_email, str_pass, str_username, str_mobile, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                Utils.user_current.setEmail(str_email);
                                Utils.user_current.setPass(str_pass);
                                Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                )
        );
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        btndangki = findViewById(R.id.btnDK);
        mobile = findViewById(R.id.mobile);
        username = findViewById(R.id.username);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}