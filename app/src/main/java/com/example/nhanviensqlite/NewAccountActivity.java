package com.example.nhanviensqlite;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewAccountActivity extends AppCompatActivity {

    private EditText edtDangKyTaiKhoan;
    private EditText edtDangKyMatKhau;
    private EditText edtNhapLaiMatKhau;
    private Button btnDangKy;
    private TextView txtViewReturnDangNhap;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        txtViewReturnDangNhap = (TextView) findViewById(R.id.txtView_returnDangNhap);
        txtViewReturnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewAccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        edtDangKyTaiKhoan = (EditText) findViewById(R.id.edt_dangKyTaiKhoan);
        edtDangKyMatKhau = (EditText) findViewById(R.id.edt_dangKyMatKhau);
        edtNhapLaiMatKhau = (EditText) findViewById(R.id.edt_nhapLaiMatKhau);

        btnDangKy = (Button) findViewById(R.id.btn_dangKy);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });
    }
    private void dangKy() {
        if (edtNhapLaiMatKhau.getText().toString().compareTo(edtDangKyMatKhau.getText().toString()) == 0)
        {
            sharedPreferences = getSharedPreferences("accounts", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(edtDangKyTaiKhoan.getText().toString(), edtDangKyMatKhau.getText().toString());
            editor.apply();//được sử dụng để áp dụng thay đổi lưu trữ vào SharedPreferences.
            Toast.makeText(getApplicationContext(), "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NewAccountActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo");
            builder.setMessage("Nhập lại mật khẩu không khớp");
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