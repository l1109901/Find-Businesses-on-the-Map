package ru.mail.loginregister;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import ru.mail.loginregister.calisan.ayrintili_arama_ve_yol;
import ru.mail.loginregister.calisan.main_page_for_isci;

public class yolcizimden_oncekisayfa_calisan extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yolcizimden_oncekisayfa_calisan);
    }

    public void anasayfa(View view){
        startActivity(new Intent(this, main_page_for_isci.class));
    }

    public void yol_ciz(View view){
        startActivity(new Intent(this,ayrintili_arama_ve_yol.class));
    }

    public void onaylanan_yol(View view){
        startActivity(new Intent(this,onaylananlar_yol_cizimi_calisan.class));
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
