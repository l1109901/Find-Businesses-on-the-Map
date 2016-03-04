package ru.mail.loginregister;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gafur on 06.02.2016.
 */
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
        spEditor.putInt("dogum_yil", user.getYil());
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
        int yil = userLocalDatabase.getInt("dogum_yil", -1);
        String email = userLocalDatabase.getString("email", "");
        String tel=userLocalDatabase.getString("tel", "");
        int id=userLocalDatabase.getInt("id", -1);
        String username=userLocalDatabase.getString("kullanici_adi", "");
        String password=userLocalDatabase.getString("sifre","");

        //User storedUser=new User(ad,soyad,yil,email,tel,id,username,password);

        //return storedUser;
        return new User(ad,soyad,yil,email,tel,id,username,password);
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
