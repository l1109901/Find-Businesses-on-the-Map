package ru.mail.loginregister;

/**
 * Created by gafur on 3/28/2016.
 */
public class Firma {
    private long tcno;
    private String firmaAdi;
    private double latitude,longtitude;

    public Firma(long tcno,String firmaAdi,double latitude,double longitude){
        this.tcno=tcno;
        this.firmaAdi=firmaAdi;
        this.latitude=latitude;
        this.longtitude=longitude;
    }

    public String getFirmaAdi(){
        return firmaAdi;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongtitude(){
        return longtitude;
    }

    public long getTcno(){
        return tcno;
    }
}
