package ru.mail.loginregister;

/**
 * Created by gafur on 06.02.2016. Şubat
 * Dont worry !
 */
public class User {
    private String ad, soyad, email,kullaniciAdi,parola1,tel;
    private int yil,id;
    
    public User(String ad,String soyad,int yil,String email, String tel,int id, String kullanici_adi, String parola1){
        this.ad=ad;
        this.soyad=soyad;
        this.yil=yil;
        this.email=email;
        this.tel=tel;
        this.id=id;
        this.kullaniciAdi=kullanici_adi;
        this.parola1=parola1;
    }
    public User(String username,String password){
        this.kullaniciAdi=username;
        this.parola1=password;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.ad = soyad;
    }

    public int getYil() {
        return yil;
    }

    public void setYil(int yil) {
        this.yil = yil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setName(String tel) {
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getParola1() {
        return parola1;
    }

    public void setParola1(String parola1) {
        this.parola1 = parola1;
    }
}
