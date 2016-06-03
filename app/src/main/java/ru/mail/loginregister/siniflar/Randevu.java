package ru.mail.loginregister.siniflar;

/**
 * Created by gafur on 5/20/2016.
 */
public class Randevu {
    int id;
    private long gonderen_tcno;
    private String firma_adi,isim,soyad,alan;
    private String tarih,saat;
    private int durum;

    public Randevu(int id, String firma_adi, long tcno, String isim, String soyad, String tarih, String saat, int durum){
        this.id=id;
        this.firma_adi=firma_adi;
        this.gonderen_tcno=tcno;
        this.isim=isim;
        this.soyad=soyad;
        this.tarih=tarih;
        this.saat=saat;
        this.durum=durum;
    }

    public int getId(){
        return id;
    }
    public String getFirma_adi(){
        return firma_adi;
    }
    public long getGonderen_tcno(){
        return gonderen_tcno;
    }
    public String getIsim(){
        return isim;
    }
    public String getSoyad(){
        return soyad;
    }
    public String getTarih(){
        return tarih;
    }
    public String getSaat(){
        return saat;
    }
    public int getDurum(){
        return durum;
    }
}
