package com.example.nhanviensqlite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<NhanVien> mNhanVienList;

    public ImageAdapter(Context context, ArrayList<NhanVien> nhanVienList) {
        mContext = context;
        mNhanVienList = nhanVienList;
    }

    @Override
    public int getCount() {
        return mNhanVienList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNhanVienList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250)); // Kích thước hình ảnh trong GridView
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        byte[] imageData = mNhanVienList.get(position).getIdAvatar();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        imageView.setImageBitmap(bitmap);
        return imageView;
    }
}

