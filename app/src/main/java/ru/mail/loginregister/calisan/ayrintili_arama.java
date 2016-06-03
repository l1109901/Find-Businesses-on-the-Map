package ru.mail.loginregister.calisan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import ru.mail.loginregister.R;
import ru.mail.loginregister.ayrintili_arama_sonuclari_calisan;

/**
 * Created by gafur on 5/21/2016.
 */
public class ayrintili_arama extends Activity {
    private Spinner il_Spinner;
    private Spinner ilce_Spinner;

    private ArrayAdapter<Il> ilArrayAdapter;
    private ArrayAdapter<Ilce> ilceArrayAdapter;

    private ArrayList<Il> iller;
    private ArrayList<Ilce> ilceler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ayrintili_arama_calisan);

        initializeUI();
    }

    private void initializeUI() {
        il_Spinner = (Spinner) findViewById(R.id.spinner_il);
        ilce_Spinner = (Spinner) findViewById(R.id.spinner_ilce);

        iller = new ArrayList<>();
        ilceler = new ArrayList<>();

        createLists();

        ilArrayAdapter = new ArrayAdapter<Il>(this, android.R.layout.simple_spinner_item, iller);
        il_Spinner.setAdapter(ilArrayAdapter);

        ilceArrayAdapter = new ArrayAdapter<Ilce>(this, android.R.layout.simple_spinner_item, ilceler);
        ilce_Spinner.setAdapter(ilceArrayAdapter);

        il_Spinner.setOnItemSelectedListener(il_listener);
        ilce_Spinner.setOnItemSelectedListener(ilce_listener);
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

                ilceArrayAdapter = new ArrayAdapter<Ilce>(ayrintili_arama.this, android.R.layout.simple_spinner_dropdown_item, tempIlceler);
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

    public void ara(View view){

        String il=il_Spinner.getSelectedItem().toString();
        String ilce=ilce_Spinner.getSelectedItem().toString();

        Intent i = new Intent(this, ayrintili_arama_sonuclari_calisan.class);
        i.putExtra("il",il);
        i.putExtra("ilce",ilce);
        startActivity(i);
    }
    public void iptal(View view){
        startActivity(new Intent(this,main_page_for_isci.class));
    }
}
