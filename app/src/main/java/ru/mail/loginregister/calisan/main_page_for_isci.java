package ru.mail.loginregister.calisan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

import ru.mail.loginregister.MainActivity;
import ru.mail.loginregister.R;
import ru.mail.loginregister.randevular_calisan;
import ru.mail.loginregister.siniflar.User;
import ru.mail.loginregister.siniflar.UserLocalStore;
import ru.mail.loginregister.yolcizimden_oncekisayfa_calisan;

public class main_page_for_isci extends ActionBarActivity implements OnClickListener {

    private Button bEkle;
    Button bLogout,bisara,bisara2,brandevular;
    TextView tvAd,tvSoyad,tvEmail;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_for_calisan);
        userLocalStore=new UserLocalStore(this);

        //components from main_page_for_isci.xml
        tvAd=(TextView)findViewById(R.id.tvAd);
        tvSoyad=(TextView)findViewById(R.id.tvSoyad);
        tvEmail=(TextView)findViewById(R.id.tvEmail);

        bLogout=(Button)findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);
        bisara=(Button)findViewById(R.id.b_isara);
        bisara.setOnClickListener(this);
        bisara2=(Button)findViewById(R.id.b_isara_ayrintili);
        bisara2.setOnClickListener(this);
        bEkle = (Button) findViewById(R.id.bEgitimEkle);
        bEkle.setOnClickListener(this);
        brandevular = (Button)findViewById(R.id.btn_randevular);
        brandevular.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(authenticate()){
            displayUserDetails();
        }else{
            startActivity(new Intent(main_page_for_isci.this,MainActivity.class));
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
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.b_isara:
                startActivity(new Intent(this, ayrintili_arama.class));
                break;
            case R.id.b_isara_ayrintili:
                startActivity(new Intent(this, yolcizimden_oncekisayfa_calisan.class));
                break;
            case R.id.bEgitimEkle:
                startActivity(new Intent(this, egitim_bilgisi_ekleme.class));
                break;
            case R.id.btn_randevular:
                startActivity(new Intent(this, randevular_calisan.class));
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