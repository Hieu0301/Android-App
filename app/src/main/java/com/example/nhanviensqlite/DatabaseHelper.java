package com.example.nhanviensqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "BD.sqlite", null, 1);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE NhanVien("+
                "nhanvienId integer primary key, "+
                "hoten Text,"+
                "gioitinh Text, "+
                "donvi Text, "+
                "id_avatar Blob)");
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NhanVien");
        onCreate(sqLiteDatabase);
    }
public int insertNhanVien(NhanVien nhanvien) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("nhanvienId", nhanvien.getNhanvienId());
    contentValues.put("hoten", nhanvien.getHoTen());
    contentValues.put("gioitinh", nhanvien.getGioiTinh());
    contentValues.put("donvi", nhanvien.getDonVi());
    contentValues.put("id_avatar", nhanvien.getIdAvatar());
    int result = (int) db.insert("NhanVien", null, contentValues);
    db.close();
    return result;
}
    public ArrayList<NhanVien> getNhanVien() {
        ArrayList<NhanVien> listNhanVien = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from NhanVien", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            listNhanVien.add(new NhanVien(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getBlob(4)));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return listNhanVien;
    }
    public int deleteNhanVien(int nhanvienId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "nhanvienId = ?";
        String[] whereArgs = new String[]{String.valueOf(nhanvienId)};
        int result = db.delete("NhanVien", whereClause, whereArgs);
        close(db, null);
        return result;
    }

//    public int updateNhanVien(int nhanvienId, String hoTen, String gioiTinh, String donVi) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("hoten", hoTen);
//        values.put("gioitinh", gioiTinh);
//        values.put("donvi", donVi);
//        //values.put("id_avatar", idAvatar);
//        String whereClause = "nhanvienId = ?";
//        String[] whereArgs = new String[]{String.valueOf(nhanvienId)};
//        int result = db.update("NhanVien", values, whereClause, whereArgs);
//        close(db, null);
//        return result;
//    }
    private void close(SQLiteDatabase db, Cursor cursor){
        if(cursor != null){
            cursor.close();
        }
        if (db != null){
            db.close();
        }
    }
    // Phương thức để lấy thông tin nhân viên theo ID
    public NhanVien getNhanVienById(int nhanvienId) {
        SQLiteDatabase db = this.getReadableDatabase();
        NhanVien nhanVien = new NhanVien();
        Cursor cursor = db.query("NhanVien", // Tên của bảng
                new String[]{"nhanvienId", "hoten", "gioitinh", "donvi", "id_avatar"}, // Các cột trong bảng
                "nhanvienId = ?", new String[]{String.valueOf(nhanvienId)},
                null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) { // Di chuyển con trỏ tới hàng đầu tiên
                int nhanvienIdIndex = cursor.getColumnIndex("nhanvienId");
                int hoTenIndex = cursor.getColumnIndex("hoten");
                int gioiTinhIndex = cursor.getColumnIndex("gioitinh");
                int donViIndex = cursor.getColumnIndex("donvi");
                int idAvatarIndex = cursor.getColumnIndex("id_avatar");
                if (nhanvienIdIndex >= 0) {//nếu mà trường dữ liệu >= 0 thì lấy dữ liệu theo cột tương ứng
                    nhanVien.setNhanvienId(cursor.getInt(nhanvienIdIndex));
                }
                if (hoTenIndex >= 0) {
                    nhanVien.setHoTen(cursor.getString(hoTenIndex));
                }
                if (gioiTinhIndex >= 0) {
                    nhanVien.setGioiTinh(cursor.getString(gioiTinhIndex));
                }
                if (donViIndex >= 0) {
                    nhanVien.setDonVi(cursor.getString(donViIndex));
                }
                if (idAvatarIndex >= 0) {
                    byte[] imageBytes = cursor.getBlob(idAvatarIndex);
                    nhanVien.setIdAvatar(imageBytes);
                }
            }
            cursor.close();
        }
        db.close();
        return nhanVien;
    }
    public int updateNhanVien(NhanVien nhanVien) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hoten", nhanVien.getHoTen());
        values.put("gioitinh", nhanVien.getGioiTinh());
        values.put("donvi", nhanVien.getDonVi());
        values.put("id_avatar", nhanVien.getIdAvatar()); // Giả sử đây là cột lưu trữ hình ảnh
        return db.update("NhanVien", values, "nhanvienId = ?",
                new String[]{String.valueOf(nhanVien.getNhanvienId())});
    }
}
