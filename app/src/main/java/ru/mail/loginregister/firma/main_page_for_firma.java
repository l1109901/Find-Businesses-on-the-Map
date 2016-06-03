package ru.mail.loginregister.firma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.mail.loginregister.MainActivity;
import ru.mail.loginregister.R;
import ru.mail.loginregister.siniflar.UserLocalStore;
import ru.mail.loginregister.randevular_firma;
import ru.mail.loginregister.siniflar.User;

public class main_page_for_firma extends Activity implements View.OnClickListener {

    UserLocalStore userLocalStore;
    Button bLogout;
    Button bKonum;
    Button bRandevu;
    TextView tvAd,tvSoyad,tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_for_firma);
        userLocalStore=new UserLocalStore(this);

        //components from main_page_for_isci.xml
        tvAd=(TextView)findViewById(R.id.tvAd);
        tvSoyad=(TextView)findViewById(R.id.tvSoyad);
        tvEmail=(TextView)findViewById(R.id.tvEmail);

        bKonum=(Button)findViewById(R.id.bKonum);
        bKonum.setOnClickListener(this);
        bLogout=(Button)findViewById(R.id.bLogout2);
        bLogout.setOnClickListener(this);
        bRandevu=(Button)findViewById(R.id.btn_randevular);
        bRandevu.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(authenticate()){
            displayUserDetails();
        }else{
            startActivity(new Intent(this,MainActivity.class));
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
            case R.id.bLogout2:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.bKonum:
                startActivity(new Intent(this,maps_activity.class));
                break;
            case R.id.btn_randevular:
                startActivity(new Intent(this,randevular_firma.class));
                break;
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
