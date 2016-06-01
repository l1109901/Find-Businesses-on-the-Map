package ru.mail.loginregister;

import ru.mail.loginregister.siniflar.User;

interface GetUserCallback {
    public abstract void done(User returnedUser);
}
