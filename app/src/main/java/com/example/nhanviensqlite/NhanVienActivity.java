package com.example.nhanviensqlite;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import android.util.Base64;
public class NhanVienActivity extends AppCompatActivity {
    Button btnChonAnh, btnSelect, btnSave, btnDelete, btnUpdate, btnExit;
    EditText edtNhanVienId, edtHoTen;
    RadioButton radNam, radNu;
    Spinner spinnerDonVi;
    private String donVi = "";
    GridView gvNhanVien;
    ListView lvnhanVien;
    DatabaseHelper dbHelper;
    private NhanVien nhanVien = new NhanVien();
    private ArrayList<NhanVien> listNhanVien = new ArrayList<NhanVien>();
    private ImageView imgAnhNhanVien;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    int pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_vien);
        // Trong phương thức onCreate hoặc khi nhận dữ liệu từ cơ sở dữ liệu
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NhanVienActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        edtNhanVienId = (EditText) findViewById(R.id.edt_idNhanvien);
        edtHoTen = (EditText) findViewById(R.id.edt_hoTen);
        radNam = (RadioButton) findViewById(R.id.rad_nam);
        radNu = (RadioButton) findViewById(R.id.rad_nu);
        dbHelper = new DatabaseHelper(this);
        lvnhanVien= findViewById(R.id.lv_nhanVien);
        spinnerDonVi = findViewById(R.id.spinner_donVi);
        String[] lisItem;
        lisItem = getResources().getStringArray(R.array.array_donVi);
        ArrayAdapter<String> adapterDonVi = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lisItem);
        spinnerDonVi.setAdapter(adapterDonVi);
        spinnerDonVi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = lisItem[i];
                donVi = value;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // button chon ảnh
        imgAnhNhanVien = findViewById(R.id.imageView);
        Button btnChonAnh = findViewById(R.id.btn_ChonAnh);
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChonAnh();
            }
        });
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NhanVien nhanVien = new NhanVien();
                nhanVien.setNhanvienId(Integer.parseInt(edtNhanVienId.getText().toString()));
                nhanVien.setHoTen(edtHoTen.getText().toString());
                if (radNam.isChecked()) {
                    nhanVien.setGioiTinh("nam");
                } else {
                    nhanVien.setGioiTinh("nu");
                }
                nhanVien.setDonVi(donVi);
                // chuyển hình ảnh thành dạng byte
                byte[] imageBytes = imageToByte(imgAnhNhanVien);
                // Set the byte array to your NhanVien object
                nhanVien.setIdAvatar(imageBytes);
                if (dbHelper.insertNhanVien(nhanVien) > 0) {
                    Toast.makeText(getApplicationContext(), "Đã lưu", Toast.LENGTH_LONG).show();
                    loaddata();
                    listNhanVien = (ArrayList<NhanVien>) dbHelper.getNhanVien();
                    NhanVienAdapter adapterNhanVien = new NhanVienAdapter(NhanVienActivity.this, listNhanVien);
                    lvnhanVien.setAdapter(adapterNhanVien);
                    //xoaTrang();
                } else {
                    Toast.makeText(getApplicationContext(), "Chưa lưu", Toast.LENGTH_LONG).show();
                }
                // Xóa trắng
                edtNhanVienId.setText("");
                edtHoTen.setText("");
                radNam.setChecked(false);
                radNu.setChecked(false);
            }
        });
//        btnSelect = (Button) findViewById(R.id.btn_select);
//        btnSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loaddata();
//            }
//        });

        btnSelect = (Button) findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNhanVienId.getText().toString().isEmpty()) {
                    loaddata();
                }
                else
                {
                    int nhanVienId = Integer.parseInt(edtNhanVienId.getText().toString());
                    ArrayList<NhanVien> tempNhanVienList = new ArrayList<>();
                    NhanVien tempNhanVien = dbHelper.getNhanVienById(nhanVienId);
                    tempNhanVienList.add(tempNhanVien);
                    NhanVienAdapter adapterNhanVien = new NhanVienAdapter(NhanVienActivity.this, tempNhanVienList);
                    lvnhanVien.setAdapter(adapterNhanVien);
                }
            }
        });
        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nhanvienIdText = ((EditText) findViewById(R.id.edt_idNhanvien)).getText().toString();
                if (!nhanvienIdText.isEmpty()) {
                    int nhanvienId = Integer.parseInt(nhanvienIdText);
                    int result = dbHelper.deleteNhanVien(nhanvienId);
                    if (result > 0) {
                        Toast.makeText(getApplicationContext(), "Đã xóa nhân viên này", Toast.LENGTH_SHORT).show();
                        loaddata();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Xóa nhân viên không thành công", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    // Hiển thị thông báo cho người dùng rằng họ cần nhập authorId.
                    Toast.makeText(getApplicationContext(), "Chưa nhập nhanvienId", Toast.LENGTH_SHORT).show();
                }
                // Xóa trắng
                edtNhanVienId.setText("");
                edtHoTen.setText("");
                radNam.setChecked(false);
                radNu.setChecked(false);
            }
        });
//        btnUpdate = findViewById(R.id.btn_update);
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String nhanvienIdText = ((EditText) findViewById(R.id.edt_idNhanvien)).getText().toString();
//                String hoTen = ((EditText) findViewById(R.id.edt_hoTen)).getText().toString();
//                String gioiTinh = radNam.isChecked() ? "nam" : "nu";
//                Spinner spinnerDonVi = findViewById(R.id.spinner_donVi);
//                String donVi = spinnerDonVi.getSelectedItem().toString();
//
//                if (!nhanvienIdText.isEmpty() && !hoTen.isEmpty()) {
//                    try {
//                        int nhanvienId = Integer.parseInt(nhanvienIdText);
//                        // Lấy thông tin nhân viên từ cơ sở dữ liệu
//                        NhanVien nhanVien = dbHelper.getNhanVienById(nhanvienId);//sư dụng id để lấy thông tin từ dữ liệu
//                        // Cập nhật thông tin nhân viên
//                        nhanVien.setHoTen(hoTen);
//                        nhanVien.setGioiTinh(gioiTinh);
//                        nhanVien.setDonVi(donVi);
//                        // Chuyển đổi hình ảnh (imgAnhNhanVien) thành mảng byte để cập nhật hình ảnh mới cho nhân viên. Nếu có hình ảnh mới, nó sẽ được gán vào nhanVien
//                        byte[] imageBytes = imageToByte(imgAnhNhanVien);
//                        // Nếu có hình ảnh mới, cập nhật lại hình ảnh
//                        if (imageBytes != null) {
//                            nhanVien.setIdAvatar(imageBytes);
//                        }
//                        // Gọi phương thức cập nhật trong cơ sở dữ liệu
//                        int result = dbHelper.updateNhanVien(nhanVien);
//                        if (result > 0) {
//                            // Cập nhật thành công, có thể cập nhật giao diện hoặc thông báo.
//                            Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
//                            loaddata();
//                        } else {
//                            // Cập nhật không thành công, có thể cập nhật giao diện hoặc thông báo lỗi.
//                            Toast.makeText(getApplicationContext(), "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (NumberFormatException e) {
//                        // Xử lý khi không thể chuyển đổi nhanvienIdText sang kiểu int
//                        e.printStackTrace();
//                    }
//                } else {
//                    // Hiển thị thông báo cho người dùng rằng họ cần nhập đầy đủ thông tin.
//                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin và chọn hình ảnh", Toast.LENGTH_SHORT).show();
//                }
//                // Xóa trắng
//                edtNhanVienId.setText("");
//                edtHoTen.setText("");
//                radNam.setChecked(false);
//                radNu.setChecked(false);
//            }
//        });

        btnUpdate= (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NhanVien nhanVien = new NhanVien();
                nhanVien.setNhanvienId(Integer.parseInt(edtNhanVienId.getText().toString()));
                nhanVien.setHoTen(edtHoTen.getText().toString());
                if (radNam.isChecked()) {
                    nhanVien.setGioiTinh("nam");
                } else {
                    nhanVien.setGioiTinh("nu");
                }
                nhanVien.setDonVi(spinnerDonVi.getSelectedItem().toString());

                byte[] img = imageToByte(imgAnhNhanVien);
                nhanVien.setIdAvatar(img);
                int result = dbHelper.updateNhanVien(nhanVien);

                if (result > 0) {
                    Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    NhanVienAdapter adapterNhanVien = new NhanVienAdapter(NhanVienActivity.this, listNhanVien);
                    lvnhanVien.setAdapter(adapterNhanVien);
                } else {
                    Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Chọn nhân viên
        lvnhanVien = (ListView) findViewById(R.id.lv_nhanVien);
        lvnhanVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                NhanVien tempNhanVien = listNhanVien.get(i);
                pos = i;
                edtNhanVienId.setText(Integer.toString(tempNhanVien.getNhanvienId()));
                edtHoTen.setText(tempNhanVien.getHoTen());
                // Đặt giới tính dựa trên dữ liệu của nhanVien
                if (tempNhanVien.getGioiTinh().equalsIgnoreCase("Nam")) {
                    radNam.setChecked(true);
                    radNu.setChecked(false);
                } else if (tempNhanVien.getGioiTinh().equalsIgnoreCase("Nu")) {
                    radNam.setChecked(false);
                    radNu.setChecked(true);
                } else {
                    radNam.setChecked(false);
                    radNu.setChecked(false);
                }
                String donVi = tempNhanVien.getDonVi();
                // Tìm vị trí của donVi trong mảng donViArray
                String[] donViArray = getResources().getStringArray(R.array.array_donVi);
                // Kiểm tra xem donVi có trong mảng không trước khi đặt giá trị cho Spinner
                int posDonVi = Arrays.asList(donViArray).indexOf(donVi); //Cầm donVi đi so với từng item trong array_donVi
                if (posDonVi != -1) {
                    //Đặt giá   trị cho spinner
                    spinnerDonVi.setSelection(posDonVi);
                }
                Bitmap bitmap = getBitmapFromImagePath(tempNhanVien.getIdAvatar());
                if (bitmap != null) {
                    imgAnhNhanVien.setImageBitmap(bitmap);
                } else {
                    // Nếu không có ảnh, hiển thị ảnh mặc định
                    imgAnhNhanVien.setImageResource(R.drawable.avatar);
                }
            }
        });
    }
    private void loaddata() {
        listNhanVien = (ArrayList<NhanVien>) dbHelper.getNhanVien();
        NhanVienAdapter adapterNhanVien = new NhanVienAdapter(NhanVienActivity.this, listNhanVien);
        lvnhanVien.setAdapter(adapterNhanVien);
    }

    private void showDialogChonAnh () {
        final CharSequence[] items = {"Chụp ảnh", "Chọn từ thư viện"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CODE_IMAGE_CAPTURE);
                        break;
                    case 1:
                        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_CODE_GALLERY);
                        break;
                }
            }
        });
        builder.show();
    }
    // đưa ảnh từ thư viện máy lên
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Hiển thị bức ảnh trong một ImageView.
            ImageView imageView = findViewById(R.id.imageView);
            if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
                // Lấy ảnh từ Intent trực tiếp nếu là ảnh chụp
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
            } else if (requestCode == REQUEST_CODE_GALLERY) {
                // Lấy đường dẫn đến ảnh từ thư viện
                Uri imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }

    //chuyển đổi image thành byte
    private byte[] imageToByte(ImageView imgAnhNhanVien) {
        BitmapDrawable drawable = (BitmapDrawable) imgAnhNhanVien.getDrawable();
        Bitmap bitmap = drawable.getBitmap();//chuyển đổi Drawable thành Bitmap bằng cách gọi drawable.getBitmap()
        ByteArrayOutputStream stream = new ByteArrayOutputStream();//Hình ảnh dưới dạng bitmap được nén và ghi vào ByteArrayOutputStream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();//mảng byte được trả về từ stream.toByteArray(), chứa dữ liệu của hình ảnh đã được chuyển đổi từ bitmap sang dạng byte.
        return bytes;
    }

    //chuyển đổi mảng byte thành Bitmap
    private Bitmap getBitmapFromImagePath(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {//kiểm tra xem image có null hay không
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);//Sử dụng BitmapFactory.decodeByteArray() để chuyển đổi mảng byte thành một đối tượng Bitmap
        } else {
            return null;
        }
    }

}