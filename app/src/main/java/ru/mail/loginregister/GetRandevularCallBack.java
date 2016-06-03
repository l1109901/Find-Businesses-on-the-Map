package ru.mail.loginregister;

import java.util.ArrayList;

import ru.mail.loginregister.siniflar.Randevu;

/**
 * Created by gafur on 5/20/2016.
 */
public interface GetRandevularCallBack {
    public abstract void done(ArrayList<Randevu> randevular);
}
