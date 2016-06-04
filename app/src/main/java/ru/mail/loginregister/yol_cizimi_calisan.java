package ru.mail.loginregister;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.mail.loginregister.calisan.main_page_for_isci;
import ru.mail.loginregister.siniflar.Firma;
import ru.mail.loginregister.siniflar.User;
import ru.mail.loginregister.siniflar.UserLocalStore;

/**
 * Created by gafur on 5/10/2016.
 */
public class yol_cizimi_calisan extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GMapV2Direction.DirecitonReceivedListener, LocationListener,
        GoogleMap.OnMarkerClickListener{

    private Button btnDirection;

    private GoogleMap mMap;
    UserLocalStore userLocalStore;

    //Google ApiClient
    private GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;

    private LatLng myLocation = null;

    private String il, ilce;
    List<Firma> isyerleri;

    List<LatLng> points;

    private Marker myMarker;

    private Date mDate;
    private String time="";
    private String date="";
    private String firma_adi="";
    private Double lat,lon;
    private long tcno,tcno_firma;
    private int year, month, day, hour, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yol_cizimii_calisan);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(yol_cizimi_calisan.this);

        isyerleri = new ArrayList<>();
        points = new ArrayList<>();

        userLocalStore = new UserLocalStore(this);

        Bundle extras = getIntent().getExtras();
        il = extras.getString("il");
        ilce = extras.getString("ilce");

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API)
                .build();

        btnDirection = (Button) findViewById(R.id.btnDirection);
        btnDirection.setOnClickListener(this);
    }

    private void getFirmData() {

        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchLocationDataInBackground(new GetFirmCallBack() {
            @Override
            public void done(ArrayList<Firma> returnedFirma) {
                if (returnedFirma != null) {

                    points.add(myLocation);
                    Firma iam = new Firma("Benim İsyerim", myLocation.latitude, myLocation.longitude);
                    isyerleri.add(iam);

                    for (int i = 0; i < returnedFirma.size(); i++) {

                        String rt_il = returnedFirma.get(i).getIl().toString();
                        String rt_ilce = returnedFirma.get(i).getIlce();

                        if (rt_il.equals(il) && rt_ilce.equals(ilce)) {//il ve ilce secilmis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                        } else if (rt_il.equals(il) && ilce.equals("Ilce Seciniz")) {//sadece il secilmis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                        } else if (rt_ilce.equals(ilce) && il.equals("Il seciniz")) {//sadece ilce secilmis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                        } else if (il.equals("Il seciniz") && ilce.equals("Ilce Seciniz")) {//hicbisey secilmemis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                        }
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(0)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 12));

                    for (int i = 1; i < isyerleri.size(); i++) {
                        LatLng location = new LatLng(isyerleri.get(i).getLatitude(), isyerleri.get(i).getLongtitude());

                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(location)
                                        .title(isyerleri.get(i).getFirmaAdi())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        );
                    }

                } else {
                    Toast.makeText(yol_cizimi_calisan.this, "Internet Baglatinizi Kontrol Ediniz!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sortByDistance() {
        float minDistance, tmp;
        int index = 1;
        Firma tmpFirma = null;
        LatLng tmpPoint = null;

        for (int i = 0; i < points.size() - 1; i++) {

            // Getting URL to the Google Directions API
            //DownloadTask downloadTask = new DownloadTask(points.get(i),points.get(i+1));
            //String url=downloadTask.getDirectionsUrl();
            // Start downloading json data from Google Directions API
            //downloadTask.execute(url);
            //float kms=downloadTask.getDistance();

            minDistance = getDistance(points.get(i).latitude, points.get(i).longitude, points.get(i + 1).latitude, points.get(i + 1).longitude);
            index = i + 1;
            for (int j = i + 2; j < points.size(); j++) {
                tmp = getDistance(points.get(i).latitude, points.get(i).longitude, points.get(j).latitude, points.get(j).longitude);
                if (tmp <= minDistance) {
                    minDistance = tmp;
                    index = j;
                }
            }
            tmpFirma = isyerleri.get(i + 1);
            tmpPoint = points.get(i + 1);

            isyerleri.set(i + 1, isyerleri.get(index));
            points.set(i + 1, points.get(index));

            isyerleri.set(index, tmpFirma);
            points.set(index, tmpPoint);
        }
    }

    public float getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1,
                lat2, lon2,
                results);
        return results[0];
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ru.mail.loginregister/http/host/path")
        );
        AppIndex.AppIndexApi.start(googleApiClient, viewAction);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                Uri.parse("http://host/path"),
                Uri.parse("android-app://ru.mail.loginregister/http/host/path")
        );
        AppIndex.AppIndexApi.end(googleApiClient, viewAction);
    }

    public void anasayfa(View view) {
        isyerleri.clear();
        points.clear();
        mMap.clear();
        startActivity(new Intent(this, main_page_for_isci.class));
    }

    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        //Creating a location object
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());

            //Adding marker to map
            myMarker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .position(myLocation) //setting position
                    .draggable(false) //Making the marker draggable
                    .title("Current Location")); //Adding a title
            getFirmData();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onClick(View v) {

        if (v == btnDirection) {

            sortByDistance();

            for (int i = 0; i < points.size() - 1; i++) {
                new GetRotueListTask(this, points.get(i),
                        points.get(i + 1), GMapV2Direction.MODE_DRIVING, this)
                        .execute();
            }
        }
    }

    @Override
    public void OnDirectionListReceived(List<LatLng> mPointList) {

        if (mPointList != null) {
            PolylineOptions rectLine = new PolylineOptions().width(10).color(
                    Color.RED);
            for (int i = 0; i < mPointList.size(); i++) {
                rectLine.add(mPointList.get(i));
            }
            mMap.addPolyline(rectLine);

            CameraPosition mCPFrom = new CameraPosition.Builder()
                    .target(points.get(0)).zoom(15.5f).bearing(0).tilt(25)
                    .build();
            final CameraPosition mCPTo = new CameraPosition.Builder()
                    .target(points.get(1)).zoom(15.5f).bearing(0)
                    .tilt(50).build();

            changeCamera(CameraUpdateFactory.newCameraPosition(mCPFrom),
                    new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            changeCamera(CameraUpdateFactory
                                            .newCameraPosition(mCPTo),
                                    new GoogleMap.CancelableCallback() {

                                        @Override
                                        public void onFinish() {

                                            LatLngBounds bounds = new LatLngBounds.Builder()
                                                    .include(points.get(0))
                                                    .include(
                                                            points.get(1))
                                                    .build();
                                            changeCamera(
                                                    CameraUpdateFactory
                                                            .newLatLngBounds(
                                                                    bounds, 50),
                                                    null, false);
                                        }

                                        @Override
                                        public void onCancel() {
                                        }
                                    }, false);
                        }

                        @Override
                        public void onCancel() {
                        }
                    }, true);
        }
    }

    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback, boolean instant) {
        if (instant) {
            mMap.animateCamera(update, 1, callback);
        } else {
            mMap.animateCamera(update, 4000, callback);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (!marker.equals(myMarker)) {
            firma_adi = marker.getTitle().toString();
            lat = marker.getPosition().latitude;//firma's latitude
            lon = marker.getPosition().longitude;//firma's longtitude

            getAlanAndTCNOfromDB();//tcno_firma almak icin

            //////////////////////////////////////////////////////////////////////////////////////
            final Dialog dialog = new Dialog(yol_cizimi_calisan.this);
            dialog.setContentView(R.layout.dialog_demo);
            dialog.setTitle("Randevu Gönderme");


            DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.date_picker);
            TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.time_picker);

            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);

            datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                public void onDateChanged(DatePicker view, int year, int month, int day) {
                    yol_cizimi_calisan.this.year = year;
                    yol_cizimi_calisan.this.month = month;
                    yol_cizimi_calisan.this.day = day;
                    updateDateTime();
                }
            });

            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(min);

            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                public void onTimeChanged(TimePicker view, int hour, int min) {
                    yol_cizimi_calisan.this.hour = hour;
                    yol_cizimi_calisan.this.min = min;
                    updateDateTime();
                }
            });

            Button btnGonder= (Button) dialog.findViewById(R.id.button2);
            Button btnIptal=(Button)dialog.findViewById(R.id.button1);

            dialog.show();

            btnGonder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Toast.makeText(yol_cizimi_calisan.this,"saat:"+hour,Toast.LENGTH_LONG).show();
                    //to dismiss the Dialog

                    sendMessage();
                    dialog.dismiss();
                }
            });

            btnIptal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //to dismiss the Dialog
                    dialog.dismiss();
                }
            });

            /*Intent i = new Intent(yol_cizimi_calisan.this, randevu_gonderme_islemleri_calisan.class);
            i.putExtra("firma_adi", firma_adi);
            i.putExtra("lat", lat);
            i.putExtra("lon", lon);
            startActivity(i);*/
        } else {
            Toast.makeText(this, "Bir İşyeri Seçiniz!.", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void updateDateTime() {
        mDate = new GregorianCalendar(year, month, day, hour, min).getTime();
    }

    public void sendMessage(){
        createTime();//time stringe saat'i atiyor
        createDate();//date stringe tarih'i atiyor
        User user=userLocalStore.getLoggedInUser();
        tcno=user.getTc_no();
        //firma_adi degiskeninde tutuluyor
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.storeRandevuDataInBackground(tcno,tcno_firma,firma_adi,date,time);
        Toast.makeText(this, "Randevu Gonderilmiştir.", Toast.LENGTH_LONG).show();
    }

    public void createTime() {

        String format = "";
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
        time=hour+" : "+min+" "+format;
    }

    private void createDate() {
        date=day+"/"+month+"/"+year;
    }

    public void getAlanAndTCNOfromDB(){

        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchAlanInBackground(lat, lon, new GetAlanCallBack() {

            @Override
            public void done(Firma firma) {
                tcno_firma=firma.getTcno();
            }
        });
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