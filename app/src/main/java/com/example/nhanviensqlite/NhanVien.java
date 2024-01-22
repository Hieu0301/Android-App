package com.example.nhanviensqlite;
public class NhanVien {

    //properties
    private int nhanvienId;
    private String hoTen;
    private String gioiTinh;
    private String donVi;
    private byte[] idAvatar;

    //constructors

    public NhanVien(int nhanvienId, String hoTen, String gioiTinh, String donVi, byte[] idAvatar) {
        this.nhanvienId = nhanvienId;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.donVi = donVi;
        this.idAvatar = idAvatar;
    }
    public NhanVien() {
        this.nhanvienId = nhanvienId;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.donVi = donVi;
        this.idAvatar = idAvatar;
    }

    public int getNhanvienId() {
        return nhanvienId;
    }

    public void setNhanvienId(int nhanvienId) {
        this.nhanvienId = nhanvienId;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public byte[] getIdAvatar() {
        return idAvatar;
    }

    public void setIdAvatar(byte[] idAvatar) {
        this.idAvatar = idAvatar;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "nhanvienId=" + nhanvienId +
                ", hoTen='" + hoTen + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", donVi='" + donVi + '\'' +
                ", idAvatar=" + idAvatar +
                '}';
    }
}
