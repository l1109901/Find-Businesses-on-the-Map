package ru.mail.loginregister;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.mail.loginregister.firma.main_page_for_firma;
import ru.mail.loginregister.siniflar.Randevu;
import ru.mail.loginregister.siniflar.UserLocalStore;

/**
 * Created by gafur on 5/20/2016.
 */
public class randevular_firma extends Activity implements AdapterView.OnItemClickListener{

    UserLocalStore userLocalStore;
    String selectedFromList;
    ArrayList<Randevu> mylist=null;
    ListView lv;
    int buton=0;

    int mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randevular_firma);
        userLocalStore=new UserLocalStore(this);

        lv = (ListView) findViewById(R.id.randevular);
        lv.setOnItemClickListener(randevular_firma.this);

        checkFromDB();
    }

    public void geri(View view){
        startActivity(new Intent(this, main_page_for_firma.class));
    }

    public void cikis(View view){
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);
        startActivity(new Intent(this, MainActivity.class));
    }

    public void gelen(View view){


        List<String> liste=new ArrayList<>();
        List<String> bos=new ArrayList<>();
        int counter=0;

        for(Randevu r:mylist){
            if(r.getDurum()==0){//durum 0 ise onaylanmamis
                liste.add(r.getFirma_adi()+"\n"+r.getGonderen_tcno()+"\n"+r.getIsim()+"\n"+r.getSoyad()+"\n"+
                        r.getTarih()+"\n"+r.getSaat());
                counter++;
            }
        }

        if(counter!=0){
            ArrayAdapter adapter1 = new ArrayAdapter<String>(randevular_firma.this, R.layout.activity_listview, liste);//stringe cevirdikten sonra new ArrayAdapter<String> olacak

            lv.setAdapter(adapter1);
            buton=1;
            /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedFromList = lv.getItemAtPosition(position).toString();
                    lv.getChildAt(position).setBackgroundColor(Color.GRAY);
                    buton=1;
                }
            });*/
        }
        else{

            ArrayAdapter adapter1 = new ArrayAdapter<String>(randevular_firma.this, R.layout.activity_listview, bos);//stringe cevirdikten sonra new ArrayAdapter<String> olacak

            lv.setAdapter(adapter1);

            Toast.makeText(this, "Gelen kutusu bos!", Toast.LENGTH_LONG).show();
        }
    }

    public void onaylananlar(View view){

        List<String> liste=new ArrayList<>();
        List<String> bos=new ArrayList<>();
        int counter=0;

        for(Randevu r:mylist){
            if(r.getDurum()!=0){//durum 1 ise onaylanmis
                liste.add(r.getFirma_adi()+"\n"+r.getGonderen_tcno()+"\n"+r.getIsim()+"\n"+r.getSoyad()+"\n"+
                        r.getTarih()+"\n"+r.getSaat());
                counter++;
            }
        }

        if(counter!=0){

            ArrayAdapter adapter1 = new ArrayAdapter<String>(randevular_firma.this, R.layout.activity_listview, liste);//stringe cevirdikten sonra new ArrayAdapter<String> olacak

            lv.setAdapter(adapter1);
            buton=2;
            /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedFromList = lv.getItemAtPosition(position).toString();
                    lv.getChildAt(position).setBackgroundColor(Color.GRAY);
                    buton=2;
                }
            });*/
        }
        else{

            ArrayAdapter adapter1 = new ArrayAdapter<String>(randevular_firma.this, R.layout.activity_listview, bos);//stringe cevirdikten sonra new ArrayAdapter<String> olacak

            lv.setAdapter(adapter1);

            Toast.makeText(this, "Onaylanan kutusu bos!", Toast.LENGTH_LONG).show();
        }
    }

    public void checkFromDB(){

        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchRandevuInBackground(userLocalStore.getLoggedInUser().getTc_no(), new GetRandevularCallBack() {

            @Override
            public void done(ArrayList<Randevu> randevular) {
                mylist = randevular;
            }
        });
    }

    public void delete(View view){
        int id=0;
        String[] dizi=selectedFromList.split("\n");
        String firma_adi=dizi[0];
        long tcno=Long.parseLong(dizi[1]);
        String isim=dizi[2];
        String soyad=dizi[3];
        String tarih=dizi[4];
        String saat=dizi[5];

        for(Randevu r:mylist){
            if(r.getGonderen_tcno()==tcno&&
                    r.getFirma_adi().equals(firma_adi)&&
                    r.getIsim().equals(isim)&&
                    r.getSoyad().equals(soyad)&&
                    r.getTarih().equals(tarih)&&
                    r.getSaat().equals(saat)){
                id=r.getId();
            }
        }
        deleteFromDB(id);
    }

    public void deleteFromDB(int id){
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.deleteFromRandevuInBackground(id, new GetConnectionCallBack() {
            @Override
            public void done(String result) {
                if (result.equals("") || result == null) {
                    Toast.makeText(randevular_firma.this, "Server baglanti hatasi!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    checkFromDB();
                    Toast.makeText(randevular_firma.this, "Listeyi yenilemek icin (Gelen/Onaylananlar) butona tiklayin!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    public void onayla(View view){
        if(buton==1){
            int id=0;
            String[] dizi=selectedFromList.split("\n");
            String firma_adi=dizi[0];
            long tcno=Long.parseLong(dizi[1]);
            String isim=dizi[2];
            String soyad=dizi[3];
            String tarih=dizi[4];
            String saat=dizi[5];

            for(Randevu r:mylist){
                if(r.getGonderen_tcno()==tcno&&
                        r.getFirma_adi().equals(firma_adi)&&
                        r.getIsim().equals(isim)&&
                        r.getSoyad().equals(soyad)&&
                        r.getTarih().equals(tarih)&&
                        r.getSaat().equals(saat)){
                    id=r.getId();
                }
            }
            onaylaDB(id);
        }else {
            Toast.makeText(randevular_firma.this, "Onaylamak icin Gelen butona tekrar tiklayin!", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void onaylaDB(int id){
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.onaylaInBackground(id, new GetConnectionCallBack() {
            @Override
            public void done(String result) {
                if (result.equals("") || result == null) {
                    Toast.makeText(randevular_firma.this, "Server baglanti hatasi!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    checkFromDB();
                    Toast.makeText(randevular_firma.this, "Listeyi yenilemek icin (Gelen/Onaylananlar) butona tiklayin!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedFromList = lv.getItemAtPosition(position).toString();
        view.setSelected(true);
    }

}
