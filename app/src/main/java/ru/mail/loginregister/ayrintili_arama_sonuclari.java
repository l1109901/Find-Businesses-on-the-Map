package ru.mail.loginregister;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by gafur on 5/10/2016.
 */
public class ayrintili_arama_sonuclari extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GMapV2Direction.DirecitonReceivedListener {

    private Button btnDirection;
    ToggleButton tbMode;

    private GoogleMap mMap;
    UserLocalStore userLocalStore;

    //Google ApiClient
    private GoogleApiClient googleApiClient;

    //To store longitude and latitude from map
    private double longitude;
    private double latitude;

    private String il,ilce,alan;
    List<Firma> isyerleri=new ArrayList<>();
    List<Firma> sortedFirma=new ArrayList<>();

    List<LatLng> points=new ArrayList<LatLng>();
    List<LatLng> sortedPoints=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayrintili_arama_sonuclari);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(ayrintili_arama_sonuclari.this);

        userLocalStore=new UserLocalStore(this);

        Bundle extras=getIntent().getExtras();
        il=extras.getString("il");
        ilce=extras.getString("ilce");
        alan=extras.getString("alan");

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

        getFirmData();
        btnDirection = (Button) findViewById(R.id.btnDirection);
        btnDirection.setOnClickListener(this);

        tbMode = (ToggleButton) findViewById(R.id.tbMode);

        tbMode.setChecked(true);
    }

    private void getFirmData(){

        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchLocationDataInBackground(new GetFirmCallBack() {
            @Override
            public void done(ArrayList<Firma> returnedFirma) {
                if (returnedFirma != null) {
                    for (int i = 0; i < returnedFirma.size(); i++) {

                        String rt_il = returnedFirma.get(i).getIl().toString();
                        String rt_ilce = returnedFirma.get(i).getIlce();
                        String rt_alan = returnedFirma.get(i).getAlan();

                        if (rt_il.equals(il) && rt_ilce.equals(ilce)) {//il ve ilce secilmis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        } else if (rt_il.equals(il)&&!rt_ilce.equals(ilce)) {//sadece il secilmis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        } else if (rt_ilce.equals(ilce)&&!rt_il.equals(il)) {//sadece ilce secilmis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        } else if(il.equals("Il seciniz")&&ilce.equals("Ilce Seciniz")){//hicbisey secilmemis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        }
                    }
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(0)));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 12));
                    sortByDistance();
                } else {
                    Toast.makeText(ayrintili_arama_sonuclari.this, "Internet Baglatinizi Kontrol Ediniz!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sortByDistance(){
        float minDistance,tmp;
        int counter=1,k;
        sortedFirma.add(isyerleri.get(0));
        sortedPoints.add(points.get(0));


        for(int i=0;i<points.size()-1;i++){
            minDistance=500000000;
            k=i+1;
            for(int j=i+1;j<points.size();j++){
                tmp=getDistance(points.get(i).latitude,points.get(i).longitude,points.get(j).latitude,points.get(j).longitude);
                if(tmp<minDistance){
                    minDistance=tmp;
                    k=j;
                }
            }
            sortedFirma.add(isyerleri.get(k));
            sortedPoints.add(points.get(k));
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
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();

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
        googleApiClient.disconnect();
        super.onStop();

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
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
            moveMap();
        }
    }

    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);


        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public void anasayfa(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void cikis(View view){
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);
        startActivity(new Intent(this, Login.class));
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onClick(View v) {

        if (v == btnDirection) {
            mMap.clear();

            if (tbMode.isChecked()) {
                for(int i=0;i<sortedPoints.size()-1;i++){
                    new GetRotueListTask(this, sortedPoints.get(i),
                            sortedPoints.get(i+1), GMapV2Direction.MODE_DRIVING, this)
                            .execute();
                }

            } else {
                for(int i=0;i<sortedPoints.size()-1;i++){
                    new GetRotueListTask(this, sortedPoints.get(i),
                            sortedPoints.get(i+1), GMapV2Direction.MODE_WALKING, this)
                            .execute();
                }
            }
        }
    }

    @Override
    public void OnDirectionListReceived(List<LatLng> mPointList) {

        for(int i=0;i<sortedFirma.size();i++){
            LatLng location = new LatLng(sortedFirma.get(i).getLatitude(), sortedFirma.get(i).getLongtitude());
            mMap.addMarker(new MarkerOptions().position(location).title(sortedFirma.get(i).getFirmaAdi()));
        }

        if (mPointList != null) {
            PolylineOptions rectLine = new PolylineOptions().width(10).color(
                    Color.RED);
            for (int i = 0; i < mPointList.size(); i++) {
                rectLine.add(mPointList.get(i));
            }
            mMap.addPolyline(rectLine);

            CameraPosition mCPFrom = new CameraPosition.Builder()
                    .target(sortedPoints.get(0)).zoom(15.5f).bearing(0).tilt(25)
                    .build();
            final CameraPosition mCPTo = new CameraPosition.Builder()
                    .target(sortedPoints.get(1)).zoom(15.5f).bearing(0)
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
                                                    .include(sortedPoints.get(0))
                                                    .include(
                                                            sortedPoints.get(1))
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
    public void onInfoWindowClick(Marker marker) {}
    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
    @Override
    public void onMapLongClick(LatLng latLng) {}
    @Override
    public void onMarkerDragStart(Marker marker) {}
    @Override
    public void onMarkerDrag(Marker marker) {}
    @Override
    public void onMarkerDragEnd(Marker marker) {}
}