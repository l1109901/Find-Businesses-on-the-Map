package ru.mail.loginregister;

import java.util.ArrayList;

import ru.mail.loginregister.siniflar.Education;
import ru.mail.loginregister.siniflar.Randevu;

/**
 * Created by gafur on 5/31/2016.
 */
public interface GetEducationCallBack {
    public abstract void done(ArrayList<Education> educations);
}
