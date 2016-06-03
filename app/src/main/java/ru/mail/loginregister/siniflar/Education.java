package ru.mail.loginregister.siniflar;

public class Education {

    private String okul_adi,bolum;
    private int mezuniyet_yili;

    public Education(String okul_adi,String bolum,int mezuniyet_yili){
        this.okul_adi=okul_adi;
        this.bolum=bolum;
        this.mezuniyet_yili=mezuniyet_yili;
    }

    public String getOkul_adi(){return okul_adi;}

    public String getBolum(){return bolum;}

    public int getMezuniyet_yili(){return mezuniyet_yili;}

}
