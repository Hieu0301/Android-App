package com.example.nhanviensqlite;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
public class NhanVienAdapter extends ArrayAdapter<NhanVien> {
    private Context context;
    private List<NhanVien> itemList;
    public NhanVienAdapter(Context context, List<NhanVien> itemList) {
        super(context, 0, itemList);
        this.context = context;
        this.itemList = itemList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        NhanVien nhanVien = itemList.get(position);
        ImageView imgAvatar = convertView.findViewById(R.id.imgView_avatar);
        TextView tvId = convertView.findViewById(R.id.tvId);
        TextView tvHoTen = convertView.findViewById(R.id.tvHoTen);
        TextView tvGioiTinh = convertView.findViewById(R.id.tvGioiTinh);
        TextView tvDonVi = convertView.findViewById(R.id.tvDonVi);

        tvId.setText("ID: " + Integer.toString(nhanVien.getNhanvienId()));
        tvHoTen.setText(nhanVien.getHoTen());
        tvGioiTinh.setText(nhanVien.getGioiTinh());
        tvDonVi.setText(nhanVien.getDonVi());
        //Nếu có dữ liệu là hình ảnh lưu dưới dạng byte thì sẽ chuyển đổi thành bitmap
        //còn ko thì trả về null
        if (nhanVien.getIdAvatar() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(nhanVien.getIdAvatar(), 0, nhanVien.getIdAvatar().length);
            imgAvatar.setImageBitmap(bitmap);
        } else {
            imgAvatar.setImageDrawable(null);
        }

        //  if (nhanVien.getGioiTinh().compareTo("Nam") == 0) {
        //     imgAvatar.setImageResource(R.drawable.boy);
        // }
        // else imgAvatar.setImageResource(R.drawable.girl);
        return convertView;
    }
}