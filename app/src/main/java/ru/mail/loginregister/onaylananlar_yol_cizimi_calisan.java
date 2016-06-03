package ru.mail.loginregister;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.mail.loginregister.calisan.main_page_for_isci;
import ru.mail.loginregister.siniflar.Firma;
import ru.mail.loginregister.siniflar.User;
import ru.mail.loginregister.siniflar.UserLocalStore;

public class onaylananlar_yol_cizimi_calisan extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        GMapV2Direction.DirecitonReceivedListener,LocationListener{

    private Button btnDirection;

    private GoogleMap mMap;
    UserLocalStore userLocalStore;

    //Google ApiClient
    private GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;

    private LatLng myLocation=null;

    List<Firma> isyerleri=new ArrayList<>();

    List<LatLng> points=new ArrayList<>();

    private Marker myMarker;

    private Calendar calendar;
    private String currentDate;
    private String currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onaylananlar_yol_cizimi_calisan);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(onaylananlar_yol_cizimi_calisan.this);

        userLocalStore=new UserLocalStore(this);

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

    private void getFirmData(){

        User user=userLocalStore.getLoggedInUser();

        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchOnaylananLocationDataInBackground(user.getTc_no(), new GetFirmCallBack() {
            @Override
            public void done(ArrayList<Firma> returnedFirma) {
                if (returnedFirma != null) {

                    calendar = Calendar.getInstance();
                    currentDate = calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);

                    Firma myfirm = new Firma("Ben", myLocation.latitude, myLocation.longitude);
                    points.add(myLocation);
                    isyerleri.add(myfirm);

                    String tarih, saat;


                    for (int i = 0; i < returnedFirma.size(); i++) {

                        tarih = returnedFirma.get(i).getTarih();
                        saat = returnedFirma.get(i).getSaat();
                        try {

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                            Date date2 = formatter.parse(currentDate);
                            Date date1 = formatter.parse(tarih);

                            if (date1.compareTo(date2) == 0)//randevu o gune ait ise isyerini ekle
                            {
                                isyerleri.add(returnedFirma.get(i));
                                LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                                points.add(location);
                            }

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(0)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 12));

                    myMarker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(myLocation)
                                    .title("Ben")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    );

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
                    Toast.makeText(onaylananlar_yol_cizimi_calisan.this, "Internet Baglatinizi Kontrol Ediniz!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sortByDistance(){
        float minDistance,tmp;
        int index=1;
        Firma tmpFirma=null;
        LatLng tmpPoint=null;

        for(int i=0;i<points.size()-1;i++){
            minDistance=getDistance(points.get(i).latitude,points.get(i).longitude,points.get(i+1).latitude,points.get(i+1).longitude);
            index=i+1;
            for(int j=i+2;j<points.size();j++){
                tmp=getDistance(points.get(i).latitude,points.get(i).longitude,points.get(j).latitude,points.get(j).longitude);
                if(tmp<=minDistance){
                    minDistance=tmp;
                    index=j;
                }
            }
            tmpFirma=isyerleri.get(i+1);
            tmpPoint=points.get(i+1);

            isyerleri.set(i+1,isyerleri.get(index));
            points.set(i+1,points.get(index));

            isyerleri.set(index, tmpFirma);
            points.set(index,tmpPoint);
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

    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        //Creating a location object
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            myLocation=new LatLng(location.getLatitude(),location.getLongitude());

            //Adding marker to map
            myMarker=mMap.addMarker(new MarkerOptions()
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
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation=new LatLng(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onClick(View v) {
        if (v == btnDirection) {

            //sortByDistance();
            sortByTime();

            for(int i=0;i<points.size()-1;i++){
                new GetRotueListTask(this, points.get(i),
                        points.get(i+1), GMapV2Direction.MODE_DRIVING, this)
                        .execute();
            }
        }
    }

    public void sortByTime(){
        String dest,tmp;
        int index=1;
        Firma tmpFirma=null;
        LatLng tmpPoint=null;

        Date time1=null,time2=null;
        int h1=0,h2=0,m1=0,m2=0,ekle=0;

        for(int i=0;i<points.size()-1;i++){
            dest=isyerleri.get(i+1).getSaat();
            int count=0;

            mySimpleDateFormat formatter1 = new mySimpleDateFormat(dest);
            h1=formatter1.get_hour();
            ekle=formatter1.get_ekle_cikar();
            h1=h1+ekle;//saat
            m1=formatter1.get_minute();//dakika

            for(int j=i+2;j<points.size();j++){
                tmp=isyerleri.get(j).getSaat();

                mySimpleDateFormat formatter2 = new mySimpleDateFormat(tmp);
                h2=formatter2.get_hour();
                ekle=formatter2.get_ekle_cikar();
                h2=h2+ekle;//saat
                m2=formatter2.get_minute();//dakika

                if((h2<h1)||((h2==h1)&&(m2<m1))){
                    h1=h2;
                    m1=m2;
                    index=j;
                    count++;
                }
            }
            if(count!=0){//noktalar bittiyse bisey yapma
                tmpFirma=isyerleri.get(i+1);
                tmpPoint=points.get(i+1);

                isyerleri.set(i+1,isyerleri.get(index));
                points.set(i+1,points.get(index));

                isyerleri.set(index, tmpFirma);
                points.set(index,tmpPoint);
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

    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback,boolean instant) {
        if (instant) {
            mMap.animateCamera(update, 1, callback);
        } else {
            mMap.animateCamera(update, 4000, callback);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    public void anasayfa(View view){
        isyerleri.clear();
        points.clear();
        mMap.clear();
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
}
