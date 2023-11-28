package com.example.duan_appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangki,txtForgot;
    EditText email, pass;
    Button btnDangnhap;
    ApiBanHang apiBanHang;
    ImageView passwordIcon;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean passwordShowing = false;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControll();

    }

    private void initControll() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DangKiActivity.class);
                startActivity(intent);
            }
        });
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập pass", Toast.LENGTH_SHORT).show();
                } else {
                    //lưu dang nhap
                    Paper.book().write("email", str_email);
                    Paper.book().write("pass", str_pass);
                    if (user != null) {
                        // user da co dang nhap
                        dangnhap(str_email, str_pass);
                    } else {
                        //user da dang xuat
                        firebaseAuth.signInWithEmailAndPassword(str_email, str_pass)
                                .addOnCompleteListener(DangNhapActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            dangnhap(str_email, str_pass);
                                        }
                                    }
                                });

                    }


                }

            }
        });
    }
    private void dangnhap(String str_email, String str_pass) {
        compositeDisposable.add(apiBanHang.dangNhap(str_email, str_pass).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(userModel -> {
            if (userModel.isSuccess()) {

                Utils.user_current = userModel.getResult().get(0);
                Paper.book().write("user",userModel.getResult().get(0));
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, throwable -> {
            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

        }));

    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangki = findViewById(R.id.txtdangki);
        pass = findViewById(R.id.edtPassword);
        email = findViewById(R.id.edtEmail);
        btnDangnhap = findViewById(R.id.btnDangNhap);
        passwordIcon = findViewById(R.id.passwordIcon);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //icon show pass
        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing) {

                    passwordShowing = false;
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hide);
                } else {
                    passwordShowing = true;

                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show);
                }
                pass.setSelection(pass.length());
            }
        });

//read data
        if (Paper.book().read("email") != null && Paper.book().read("pass") != null) {
            email.setText(Paper.book().read("email"));
            pass.setText(Paper.book().read("pass"));

        }

    }




//read dât




    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getPass() != null) {
            email.setText(Utils.user_current.getEmail());
            pass.setText(Utils.user_current.getPass());

        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}