package ru.mail.loginregister;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends ActionBarActivity implements View.OnClickListener {

    Button bRegister;
    EditText etAd, etSoyad, etYil, etEmail,etTel,etKullanici_adi,etParola1,etParola2;
    CheckBox ch1,ch2;
    //int id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etAd = (EditText) findViewById(R.id.etAd);
        etSoyad = (EditText) findViewById(R.id.etSoyad);
        etYil = (EditText) findViewById(R.id.etYil);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etTel = (EditText) findViewById(R.id.etTel);
        etKullanici_adi = (EditText) findViewById(R.id.etKullanici_adi);
        etParola1 = (EditText) findViewById(R.id.etParola1);
        etParola2 = (EditText) findViewById(R.id.etParola2);
        ch1 = (CheckBox) findViewById(R.id.ch1);
        ch2 = (CheckBox) findViewById(R.id.ch2);
        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bRegister:
                String ad = etAd.getText().toString().trim();
                String soyad = etSoyad.getText().toString().trim();
                String syil = etYil.getText().toString();
                String stel = etTel.getText().toString();
                String email = etEmail.getText().toString().trim();
                String kullanici_adi = etKullanici_adi.getText().toString().trim();
                String parola1 = etParola1.getText().toString().trim();
                String parola2 = etParola2.getText().toString().trim();

                int id2 = 0;

                if(ch1.isChecked()){
                    id2=1;
                }else if(ch2.isChecked()){
                    id2=2;
                }else{
                    Toast.makeText(this, "Seçiminizi yapınız !", Toast.LENGTH_LONG).show();
                }
                if ( syil.isEmpty() || stel.isEmpty() || ad.isEmpty() || soyad.isEmpty() || email.isEmpty() || kullanici_adi.isEmpty() || parola1.isEmpty() || parola2.isEmpty()) {
                    Toast.makeText(this, "Tüm alanları doldurmanız gereklidir !", Toast.LENGTH_LONG).show();
                }else {

                    if (parola1.equals(parola2)) {
                        int yil = Integer.parseInt(etYil.getText().toString());

                        User user = new User(ad, soyad, yil, email,stel,id2,kullanici_adi,parola1);
                        registerUser(user);
                    } else {
                        final AlertDialog alertDialog = new AlertDialog.Builder(Register.this).create();
                        alertDialog.setMessage("Lütfen tekrar giriniz !");
                        alertDialog.setTitle("Hata!!!");
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                        alertDialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
                break;
        }
    }

    private void registerUser(User user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }


}
