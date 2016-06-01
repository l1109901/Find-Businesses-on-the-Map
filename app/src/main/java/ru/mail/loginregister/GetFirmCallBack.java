package ru.mail.loginregister;

import java.util.ArrayList;

import ru.mail.loginregister.siniflar.Firma;

/**
 * Created by gafur on 4/12/2016.
 */
public interface GetFirmCallBack {
    public abstract void done(ArrayList<Firma> firmas);
}
