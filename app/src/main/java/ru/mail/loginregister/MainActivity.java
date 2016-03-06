package ru.mail.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements OnClickListener{

    //variables from egitim_bilgisi_ekle
    private Button bEkle;
    private TextView result1,result2,result3;

    Button bLogout;
    TextView tvAd,tvSoyad,tvEmail;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLocalStore=new UserLocalStore(this);

        //components from MainActivity.xml
        tvAd=(TextView)findViewById(R.id.tvAd);
        tvSoyad=(TextView)findViewById(R.id.tvSoyad);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        bLogout=(Button)findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);

        // components from egitim_bilgisi_ekle.xml
        result1 = (TextView) findViewById(R.id.result1);
        result2 = (TextView) findViewById(R.id.result2);
        result3 = (TextView) findViewById(R.id.result3);
        bEkle = (Button) findViewById(R.id.bEgitimEkle);
        bEkle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
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

   protected void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.egitim_bilgisi_ekle, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText okulAdi = (EditText) promptView.findViewById(R.id.etOkul);
        final EditText bolum = (EditText) promptView.findViewById(R.id.etBolum);
        final EditText mezuniyetYili = (EditText) promptView.findViewById(R.id.etMezunyet);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String okuladi=okulAdi.getText().toString();
                        String bol=bolum.getText().toString();

                        int mezuniyetyili=Integer.parseInt(mezuniyetYili.getText().toString());

                        User user=userLocalStore.getLoggedInUser();
                        long tcno=user.getTc_no();

                        Education education=new Education(okuladi,bol,mezuniyetyili);
                        education_kayit(education,tcno);
                    }
                })
                .setNegativeButton("Iptal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void education_kayit(Education education,long tcno){
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.storeEducationDataInBackground(education,tcno);
    }
}
