package ru.mail.loginregister.firma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

import ru.mail.loginregister.R;
import ru.mail.loginregister.ServerRequests;
import ru.mail.loginregister.siniflar.Firma;
import ru.mail.loginregister.siniflar.User;
import ru.mail.loginregister.siniflar.UserLocalStore;

/**
 * Created by gafur on 5/7/2016.
 */
public class firma_bilgileri_girme extends AppCompatActivity {

    private double lat,lon;
    private long tcno;
    UserLocalStore userLocalStore;
    User user;
    private EditText etfirmaadi;

    private Spinner il_Spinner;
    private Spinner ilce_Spinner;
    private Spinner alan_Spinner;

    private ArrayAdapter<Il> ilArrayAdapter;
    private ArrayAdapter<Ilce> ilceArrayAdapter;
    private ArrayAdapter<Alan> alanArrayAdapter;

    private ArrayList<Il> iller;
    private ArrayList<Ilce> ilceler;
    private ArrayList<Alan> alanlar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firma_bilgileri_firma);
        userLocalStore=new UserLocalStore(this);

        Bundle extras=getIntent().getExtras();
        lat=extras.getDouble("lat");
        lon=extras.getDouble("lon");

        etfirmaadi=(EditText)findViewById(R.id.etFirmaAdi);

        initializeUI();
    }

    private void initializeUI() {
        il_Spinner = (Spinner) findViewById(R.id.spinner_il);
        ilce_Spinner = (Spinner) findViewById(R.id.spinner_ilce);
        alan_Spinner = (Spinner) findViewById(R.id.spinner_alan);

        iller = new ArrayList<>();
        ilceler = new ArrayList<>();
        alanlar = new ArrayList<>();

        createLists();

        ilArrayAdapter = new ArrayAdapter<Il>(this, android.R.layout.simple_spinner_item, iller);
        il_Spinner.setAdapter(ilArrayAdapter);

        ilceArrayAdapter = new ArrayAdapter<Ilce>(this, android.R.layout.simple_spinner_item, ilceler);
        ilce_Spinner.setAdapter(ilceArrayAdapter);

        alanArrayAdapter = new ArrayAdapter<Alan>(this, android.R.layout.simple_spinner_item, alanlar);
        alan_Spinner.setAdapter(alanArrayAdapter);

        il_Spinner.setOnItemSelectedListener(il_listener);
        ilce_Spinner.setOnItemSelectedListener(ilce_listener);
        alan_Spinner.setOnItemSelectedListener(alan_listener);
    }

    private AdapterView.OnItemSelectedListener il_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                final Il il = (Il) il_Spinner.getItemAtPosition(position);
                Log.d("SpinnerIl", "onItemSelected: country: " + il.getIlID());
                ArrayList<Ilce> tempIlceler = new ArrayList<>();

                tempIlceler.add(new Ilce(0, new Il(0, "Il Seciniz"), "Ilce Seciniz"));

                for (Ilce singleIlce : ilceler) {
                    if (singleIlce.getIl().getIlID() == il.getIlID()) {
                        tempIlceler.add(singleIlce);
                    }
                }

                ilceArrayAdapter = new ArrayAdapter<Ilce>(firma_bilgileri_girme.this, android.R.layout.simple_spinner_dropdown_item, tempIlceler);
                ilce_Spinner.setAdapter(ilceArrayAdapter);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private AdapterView.OnItemSelectedListener ilce_listener = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private AdapterView.OnItemSelectedListener alan_listener = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private void createLists() {
        Il il0 = new Il(0, "Il seciniz");
        Il il1 = new Il(1, "Istanbul");
        Il il2 = new Il(2, "Ankara");

        iller.add(il0);
        iller.add(il1);
        iller.add(il2);

        Ilce state0 = new Ilce(0, il0, "Ilce Seciniz");
        Ilce state1 = new Ilce(1, il1, "Zeytinburnu");
        Ilce state2 = new Ilce(2, il1, "Mecdiyekoy");
        Ilce state3 = new Ilce(3, il2, "Kizilay");
        Ilce state4 = new Ilce(4, il2, "Kizilay2");

        ilceler.add(state0);
        ilceler.add(state1);
        ilceler.add(state2);
        ilceler.add(state3);
        ilceler.add(state4);

        alanlar.add(new Alan(0,  "Bolum Seciniz"));
        alanlar.add(new Alan(1,  "Bilgisayar Muhendisligi"));
        alanlar.add(new Alan(2,  "Elektrik Muhendisligi"));
        alanlar.add(new Alan(3,  "Insaat Muhendisligi"));
        alanlar.add(new Alan(4,  "Kimya Muhendisligi"));
        alanlar.add(new Alan(5,  "Matematik Muhendisligi"));
        alanlar.add(new Alan(6,  "Petrol ve Dogalgaz Muhendisligi"));
        alanlar.add(new Alan(7,  "Gemi Insaat Muhendisligi"));
        alanlar.add(new Alan(8,  "Makine Muhendisligi"));
    }

    private class Il implements Comparable<Il> {

        private int ilID;
        private String ilName;

        public Il(int ilID, String ilName) {
            this.ilID = ilID;
            this.ilName = ilName;
        }

        public int getIlID() {
            return ilID;
        }

        @Override
        public String toString() {
            return ilName;
        }

        @Override
        public int compareTo(Il another) {
            return this.getIlID() - another.getIlID();
        }
    }

    private class Ilce implements Comparable<Ilce> {

        private int ilceID;
        private Il il;
        private String ilceName;

        public Ilce(int ilceID, Il il, String ilceName) {
            this.ilceID = ilceID;
            this.il = il;
            this.ilceName = ilceName;
        }

        public int getIlceID() {
            return ilceID;
        }

        public Il getIl() {
            return il;
        }

        @Override
        public String toString() {
            return ilceName;
        }

        @Override
        public int compareTo(Ilce another) {
            return this.getIlceID() - another.getIlceID();
        }
    }

    private class Alan{

        private int alanID;
        private String alanName;

        public Alan(int alanID, String alanName) {
            this.alanID = alanID;
            this.alanName = alanName;
        }

        @Override
        public String toString() {
            return alanName;
        }
    }

    public void ekle(View view) {

        user=userLocalStore.getLoggedInUser();
        tcno=user.getTc_no();
        String firma_adi=etfirmaadi.getText().toString();
        String il=il_Spinner.getSelectedItem().toString();
        String ilce=ilce_Spinner.getSelectedItem().toString();
        String alan=alan_Spinner.getSelectedItem().toString();

        Firma firma=new Firma(tcno,firma_adi,il,ilce,alan,lat,lon);

        if(firma_adi.isEmpty()||il.equals("Il seciniz")||ilce.equals("Ilce Seciniz")||alan.equals("Bolum Seciniz")){
            Toast.makeText(this, "Secim Yapınız.", Toast.LENGTH_LONG).show();
        }
        else{
            ServerRequests serverRequests=new ServerRequests(this);
            serverRequests.storeFirmaDataInBackground(firma);
            Toast.makeText(this, "Firma Bilgisi Eklenmiştir.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, main_page_for_firma.class));
        }
    }

    public void iptal(View view){
        startActivity(new Intent(this, maps_activity.class));
    }
}