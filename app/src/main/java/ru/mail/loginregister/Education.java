package ru.mail.loginregister;

public class Education {

    private String okul_adi,bolum;
    private int mezuniyet_yili;

    public Education(String okul_adi,String bolum,int mezuniyet_yili){
        this.okul_adi=okul_adi;
        this.bolum=bolum;
        this.mezuniyet_yili=mezuniyet_yili;
    }

    public void setOkul_adi(String okul_adi){this.okul_adi=okul_adi;}
    public String getOkul_adi(){return okul_adi;}

    public void setBolum(String bolum){this.bolum=bolum;}
    public String getBolum(){return bolum;}

    public void setMezuniyet_yili(int mezuniyet_yili){this.mezuniyet_yili=mezuniyet_yili;}
    public int getMezuniyet_yili(){return mezuniyet_yili;}

}
