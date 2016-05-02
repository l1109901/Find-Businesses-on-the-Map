package ru.mail.loginregister;

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

    public Firma(long tcno,String firmaAdi,String il,String ilce,String alan,double latitude,double longitude){
        this.tcno=tcno;
        this.firmaAdi=firmaAdi;
        this.il=il;
        this.ilce=ilce;
        this.alan=alan;
        this.latitude=latitude;
        this.longtitude=longitude;
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
