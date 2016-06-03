package ru.mail.loginregister.siniflar;

import android.content.Context;
import android.content.SharedPreferences;
import ru.mail.loginregister.siniflar.User;

public class UserLocalStore {//store user data on the phone
    public static final String SP_NAME="userDetails";
    SharedPreferences userLocalDatabase;//data on the phone

    public UserLocalStore(Context context){
        userLocalDatabase=context.getSharedPreferences(SP_NAME,0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor=userLocalDatabase.edit();
        spEditor.putString("ad",user.getAd());
        spEditor.putString("soyad",user.getSoyad());
        spEditor.putLong("tc_no", user.getTc_no());
        spEditor.putString("email", user.getEmail());
        spEditor.putString("tel", user.getTel());
        spEditor.putInt("kim", user.getId());
        spEditor.putString("kullanici_adi", user.getKullaniciAdi());
        spEditor.putString("sifre",user.getParola1());
        spEditor.commit();
    }

    public User getLoggedInUser(){
        String ad=userLocalDatabase.getString("ad", "");
        String soyad=userLocalDatabase.getString("soyad", "");
        long tc_no = userLocalDatabase.getLong("tc_no", -1);
        String email = userLocalDatabase.getString("email", "");
        String tel=userLocalDatabase.getString("tel", "");
        int id=userLocalDatabase.getInt("id", -1);
        String username=userLocalDatabase.getString("kullanici_adi", "");
        String password=userLocalDatabase.getString("sifre","");

        return new User(ad,soyad,tc_no,email,tel,id,username,password);
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor=userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn",false)==true){
            return true;
        }else{
            return false;
        }
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor=userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}