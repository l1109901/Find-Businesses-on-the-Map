package ru.mail.loginregister;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

import ru.mail.loginregister.calisan.main_page_for_isci;
import ru.mail.loginregister.siniflar.Firma;
import ru.mail.loginregister.siniflar.User;
import ru.mail.loginregister.siniflar.UserLocalStore;

public class randevu_gonderme_islemleri_calisan extends ActionBarActivity {

    UserLocalStore userLocalStore;
    long tcno_firma;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    private TimePicker timePicker1;
    private TextView time;
    private String format = "";
    private int hour,min;

    private TextView firma_adi;
    private TextView alan;

    private String firma_adi_str,alan_str;
    private Double lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randevu_islemleri_calisan);

        userLocalStore=new UserLocalStore(this);
        firma_adi=(TextView)findViewById(R.id.tv_firma_adi);
        alan=(TextView)findViewById(R.id.tv_alan);

        Bundle extras=getIntent().getExtras();
        firma_adi_str=extras.getString("firma_adi");
        lat=extras.getDouble("lat");
        lon=extras.getDouble("lon");

        getAlanFromDB(lat, lon);

        firma_adi.setText(firma_adi_str);
        alan.setText(alan_str);

        calendar = Calendar.getInstance();

        dateView = (TextView) findViewById(R.id.tv_date);

        //get current date
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        time = (TextView) findViewById(R.id.tv_time);

        //get current time
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        showDate(year, month + 1, day);
        showTime(hour, min);

    }

    public void getAlanFromDB(Double lat,Double lon){

        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchAlanInBackground(lat, lon, new GetAlanCallBack() {

            @Override
            public void done(Firma firma) {
                alan.setText(firma.getAlan());
                tcno_firma=firma.getTcno();
            }
        });
    }

    //date islemleri---------------------------------------------------------------------------------------

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(randevu_gonderme_islemleri_calisan.this, myDateListener, year, month, day);//<-----date icin dialog
        }
        if (id == 998) {
            return new TimePickerDialog(randevu_gonderme_islemleri_calisan.this,myTimeListener,hour,min,true);//<----------time icin dialog
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public void setTime(View view) {
        showDialog(998);
    }

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
            showTime(arg1, arg2);
        }
    };

    public void showTime(int hour, int min) {
//        yol_cizimi_calisan.getToast();
        if (hour == 0) {
            hour += 12;
            format = "AM";
        }
        else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        time.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }

    public void iptal(View view){
        startActivity(new Intent(this, main_page_for_isci.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void sendMessage(View view){
        String tarih=dateView.getText().toString().trim();
        String saat=time.getText().toString().trim();
        User user=userLocalStore.getLoggedInUser();
        long tcno=user.getTc_no();

        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.storeRandevuDataInBackground(tcno,tcno_firma,firma_adi_str,tarih,saat);

        Toast.makeText(this, "Randevu Gonderilmiştir.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, main_page_for_isci.class));
    }
}