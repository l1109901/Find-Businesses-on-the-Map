package ru.mail.loginregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gafur on 5/4/2016.
 */

public class Egitim_bilgisi_ekle extends Activity implements AdapterView.OnItemSelectedListener{

    private UserLocalStore userLocalStore;
    private Education education=null;
    private long tcno;
    Spinner okuladi,bolum,mezuniyet_yili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.egitim_bilgisi_ekle);
        userLocalStore=new UserLocalStore(this);

        okuladi=(Spinner)findViewById(R.id.spinner_okuladi);
        bolum=(Spinner)findViewById(R.id.spinner_bolum);
        mezuniyet_yili=(Spinner)findViewById(R.id.spinner_yil);

        okuladi.setOnItemSelectedListener(this);
        bolum.setOnItemSelectedListener(this);
        mezuniyet_yili.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> okullar = new ArrayList<String>();
        okullar.add("YTU");
        okullar.add("ODTU");
        okullar.add("Istanbul Universitesi");
        okullar.add("Sakarya Universitesi");
        okullar.add("Bogazici Universitesi");
        okullar.add("Gazi Universitesi");
        okullar.add("Hacettepe Universitesi");
        okullar.add("Galatasaray Universitesi");
        okullar.add("Ankara Universitesi");

        // Spinner Drop down elements
        List<String> bolumler = new ArrayList<String>();
        bolumler.add("Bilgisayar Muhendisligi");
        bolumler.add("Elektrik Muhendisligi");
        bolumler.add("Insaat Muhendisligi");
        bolumler.add("Kimya Muhendisligi");
        bolumler.add("Matematik Muhendisligi");
        bolumler.add("Petrol ve Dogalgaz Muhendisligi");
        bolumler.add("Gemi Insaat Muhendisligi");
        bolumler.add("Makine Muhendisligi");

        // Spinner Drop down elements
        List<String> yillar = new ArrayList<String>();
        yillar.add("2000");
        yillar.add("2001");
        yillar.add("2002");
        yillar.add("2003");
        yillar.add("2004");
        yillar.add("2005");
        yillar.add("2006");
        yillar.add("2007");
        yillar.add("2008");
        yillar.add("2009");
        yillar.add("2010");
        yillar.add("2011");
        yillar.add("2012");
        yillar.add("2013");
        yillar.add("2014");
        yillar.add("2015");
        yillar.add("2016");

        // Creating adapter for spinner
        ArrayAdapter<String> okulAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, okullar);
        ArrayAdapter<String> bolumAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bolumler);
        ArrayAdapter<String> yilAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yillar);

        okuladi.setAdapter(okulAdapter);
        bolum.setAdapter(bolumAdapter);
        mezuniyet_yili.setAdapter(yilAdapter);
    }

    public void ekle(View view){

        String okuladi_str=okuladi.getSelectedItem().toString();
        String bolum_str=bolum.getSelectedItem().toString();
        String yil_str=mezuniyet_yili.getSelectedItem().toString();

        int mezuniyetyili=Integer.parseInt(yil_str);

        User user=userLocalStore.getLoggedInUser();
        tcno=user.getTc_no();

        education=new Education(okuladi_str,bolum_str,mezuniyetyili);

        education_kayit(education, tcno);
        Toast.makeText(Egitim_bilgisi_ekle.this, "Egitim Bilgisi Eklenmiştir.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(Egitim_bilgisi_ekle.this,MainActivity.class));
    }

    private void education_kayit(Education education,long tcno){
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.storeEducationDataInBackground(education,tcno);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void geri(View view){
        startActivity(new Intent(this,MainActivity.class));
    }
}