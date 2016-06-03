package ru.mail.loginregister.calisan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import ru.mail.loginregister.GetEducationCallBack;
import ru.mail.loginregister.GetRandevularCallBack;
import ru.mail.loginregister.R;
import ru.mail.loginregister.ServerRequests;
import ru.mail.loginregister.siniflar.Education;
import ru.mail.loginregister.siniflar.Randevu;
import ru.mail.loginregister.siniflar.User;
import ru.mail.loginregister.siniflar.UserLocalStore;

/**
 * Created by gafur on 5/4/2016.
 */

public class egitim_bilgisi_ekleme extends Activity implements AdapterView.OnItemSelectedListener{

    private UserLocalStore userLocalStore;
    private Education education=null;
    private long tcno;
    Spinner okuladi,bolum,mezuniyet_yili;

    String selectedFromList;
    ArrayList<Education> mylist=null;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.egitim_bilgisi_ekle_calisan);
        userLocalStore=new UserLocalStore(this);

        lv = (ListView) findViewById(R.id.all_list);
        checkFromDB();

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
        Toast.makeText(egitim_bilgisi_ekleme.this, "Egitim Bilgisi Eklenmiştir.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(egitim_bilgisi_ekleme.this,main_page_for_isci.class));
    }

    private void education_kayit(Education education,long tcno){
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.storeEducationDataInBackground(education, tcno);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void geri(View view){
        startActivity(new Intent(this,main_page_for_isci.class));
    }

    public void checkFromDB(){

        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchEgitimBilgisiCalisanInBackground(userLocalStore.getLoggedInUser().getTc_no(), new GetEducationCallBack() {

            @Override
            public void done(ArrayList<Education> educations) {
                mylist = educations;

                List<String> liste = new ArrayList<>();
                List<String> bos = new ArrayList<>();
                int counter = 0;

                for (Education r : mylist) {

                    liste.add(r.getOkul_adi() + "\n" + r.getBolum() + "\nMezuniyet Yili: " + r.getMezuniyet_yili());
                    counter++;
                }

                if (counter != 0) {
                    ArrayAdapter adapter1 = new ArrayAdapter<String>(egitim_bilgisi_ekleme.this, R.layout.activity_listview, liste);//stringe cevirdikten sonra new ArrayAdapter<String> olacak

                    lv.setAdapter(adapter1);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            selectedFromList = lv.getItemAtPosition(position).toString();
                            lv.getChildAt(position).setBackgroundColor(Color.GRAY);
                        }
                    });
                } else {

                    ArrayAdapter adapter1 = new ArrayAdapter<String>(egitim_bilgisi_ekleme.this, R.layout.activity_listview, bos);//stringe cevirdikten sonra new ArrayAdapter<String> olacak

                    lv.setAdapter(adapter1);

                    Toast.makeText(egitim_bilgisi_ekleme.this, "Kayıtlı egitim bilgisi yok !", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}