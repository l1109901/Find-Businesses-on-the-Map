package ru.mail.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends ActionBarActivity implements OnClickListener {

    private Button bEkle;
    Button bLogout,bisara,brandevular;
    TextView tvAd,tvSoyad,tvEmail;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_for_isci);
        userLocalStore=new UserLocalStore(this);

        //components from MainActivity.xml
        tvAd=(TextView)findViewById(R.id.tvAd);
        tvSoyad=(TextView)findViewById(R.id.tvSoyad);
        tvEmail=(TextView)findViewById(R.id.tvEmail);

        bLogout=(Button)findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);
        bisara=(Button)findViewById(R.id.b_isara);
        bisara.setOnClickListener(this);
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
            startActivity(new Intent(MainActivity.this,Login.class));
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
            case R.id.b_isara:
                startActivity(new Intent(this, Is_Arama.class));
                break;
            case R.id.bEgitimEkle:
                startActivity(new Intent(this, Egitim_bilgisi_ekle.class));
                break;
            case R.id.btn_randevular:
                startActivity(new Intent(this, Randevular.class));
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