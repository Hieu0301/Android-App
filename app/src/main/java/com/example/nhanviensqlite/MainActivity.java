package com.example.nhanviensqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText edtTaiKhoan;
    private EditText edtMatKhau;
    private Button btnDangNhap;
    private TextView txtViewDangKy;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtTaiKhoan = (EditText) findViewById(R.id.edt_taiKhoan);
        edtMatKhau = (EditText) findViewById(R.id.edt_matKhau);

        txtViewDangKy = (TextView) findViewById(R.id.txtView_dangKy);
        txtViewDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewAccountActivity.class);
                startActivity(intent);
            }
        });

        btnDangNhap = (Button) findViewById(R.id.btn_dangNhap);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangNhap();
            }
        });


    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.mymenu, menu);
//        return true;
//    }
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == R.id.mnu_NhanVien) {
//            Intent intentAuthor = new Intent(MainActivity.this, NhanVienActivity.class);
//            startActivity(intentAuthor);
//            return true;
//        }
//        return false;
//    }
private void dangNhap() {
    sharedPreferences = getSharedPreferences("accounts", MODE_PRIVATE);//MODE_PRIVATE. SharedPreferences là một cơ chế lưu trữ dữ liệu nhẹ và không cần thiết lập cơ sở dữ liệu SQL.
    String enteredUsername = edtTaiKhoan.getText().toString();
    String enteredPassword = edtMatKhau.getText().toString();
    if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Thông báo");
        builder.setMessage("Chưa nhập tài khoản hoặc mật khẩu");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    else
    {
        Map<String, ?> accountsList = sharedPreferences.getAll();
        boolean loginStatus = false;
        for (Map.Entry<String, ?> entry : accountsList.entrySet()) {
            String savedUsername = entry.getKey();
            String savedPassword = entry.getValue().toString();

            if (enteredUsername.compareTo(savedUsername) == 0) {
                if (enteredPassword.compareTo(savedPassword) == 0) {
                    Intent intent = new Intent(MainActivity.this, NhanVienActivity.class);
                    startActivity(intent);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Sai mật khẩu");
                    builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Thông báo");
                builder.setMessage("Tài khoản không tồn tại");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        }

    }
}
}