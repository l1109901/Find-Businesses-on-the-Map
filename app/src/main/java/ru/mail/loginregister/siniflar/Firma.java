package ru.mail.loginregister.siniflar;

/**
 * Created by gafur on 3/28/2016.
 */
public class Firma {
    private long tcno;
    private String firmaAdi;
    private String il;
    private String ilce;
    private String alan;
    private double latitude,longtitude;
    private String tarih;
    private String saat;

    public Firma(long tcno,String firmaAdi,String il,String ilce,String alan,double latitude,double longitude){
        this.tcno=tcno;
        this.firmaAdi=firmaAdi;
        this.il=il;
        this.ilce=ilce;
        this.alan=alan;
        this.latitude=latitude;
        this.longtitude=longitude;
    }

    public Firma(long tcno,String firmaAdi,String il,String ilce,String alan,double latitude,double longitude,String tarih,String saat){
        this.tcno=tcno;
        this.firmaAdi=firmaAdi;
        this.il=il;
        this.ilce=ilce;
        this.alan=alan;
        this.latitude=latitude;
        this.longtitude=longitude;
        this.tarih=tarih;
        this.saat=saat;
    }

    public String getTarih(){
        return tarih;
    }

    public String getSaat(){
        return saat;
    }

    public Firma(long tcno,String alan){
        this.tcno=tcno;
        this.alan=alan;
    }

    public Firma(String isim,double latitude,double longitude){
        this.latitude=latitude;
        this.longtitude=longitude;
        this.firmaAdi=isim;
    }

    public long getTcno(){
        return tcno;
    }
    public String getFirmaAdi(){
        return firmaAdi;
    }
    public String getIl(){
        return il;
    }
    public String getIlce(){
        return ilce;
    }
    public String getAlan(){
        return alan;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongtitude(){
        return longtitude;
    }
}
