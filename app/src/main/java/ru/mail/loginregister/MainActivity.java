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


public class MainActivity extends ActionBarActivity implements OnClickListener{

    private Button bEkle;
    private TextView result1,result2,result3;

    Button bLogout;
    TextView tvAd,tvSoyad,tvEmail;
    UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAd=(TextView)findViewById(R.id.tvAd);
        tvSoyad=(TextView)findViewById(R.id.tvSoyad);
        tvEmail=(TextView)findViewById(R.id.tvEmail);

        bLogout=(Button)findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);
        userLocalStore=new UserLocalStore(this);

        // components from main.xml
        bEkle = (Button) findViewById(R.id.bEgitimEkle);
        result1 = (TextView) findViewById(R.id.result1);
        result2 = (TextView) findViewById(R.id.result2);
        result3 = (TextView) findViewById(R.id.result3);

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
        //etYil.setText(String.valueOf(user.getYil()));
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

        // get prompts.xml view
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
                        result1.setText(okulAdi.getText());
                        result2.setText(bolum.getText());
                        result3.setText(mezuniyetYili.getText());

                        String okuladi=result1.getText().toString();
                        String bolum=result2.getText().toString();

                        int mezuniyetyili=Integer.parseInt(result3.getText().toString());


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
}
