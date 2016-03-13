package ru.mail.loginregister;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainForCompany extends ActionBarActivity implements View.OnClickListener {

    UserLocalStore userLocalStore;
    Button bLogout;
    Button bKonum;
    TextView tvAd,tvSoyad,tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_for_company);
        userLocalStore=new UserLocalStore(this);

        //components from MainActivity.xml
        tvAd=(TextView)findViewById(R.id.tvAd);
        tvSoyad=(TextView)findViewById(R.id.tvSoyad);
        tvEmail=(TextView)findViewById(R.id.tvEmail);

        bKonum=(Button)findViewById(R.id.bKonum);
        bKonum.setOnClickListener(this);
        bLogout=(Button)findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(authenticate()){
            displayUserDetails();
        }else{
            startActivity(new Intent(this,Login.class));
        }
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){
        User user=userLocalStore.getLoggedInUser();
        tvAd.setText(user.getAd());
        tvSoyad.setText(user.getSoyad());
        tvEmail.setText(user.getEmail());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.bKonum:
                startActivity(new Intent(this,MapsActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
