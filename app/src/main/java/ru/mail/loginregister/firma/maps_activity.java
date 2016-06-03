package ru.mail.loginregister.firma;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ru.mail.loginregister.GetFirmCallBack;
import ru.mail.loginregister.MainActivity;
import ru.mail.loginregister.R;
import ru.mail.loginregister.ServerRequests;
import ru.mail.loginregister.randevu_gonderme_islemleri_calisan;
import ru.mail.loginregister.siniflar.Firma;
import ru.mail.loginregister.siniflar.User;
import ru.mail.loginregister.siniflar.UserLocalStore;

public class maps_activity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener ,GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    UserLocalStore userLocalStore;

    //Google ApiClient
    private GoogleApiClient googleApiClient;

    //To store longitude and latitude from map
    private double longitude;
    private double latitude;

    Marker myMarker,myBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_firma);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(maps_activity.this);

        userLocalStore=new UserLocalStore(this);

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
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
            getFirmData();
        }
    }

    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
        myMarker=mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        //Clearing all the markers
        //mMap.clear();

        //Adding a new marker to the current pressed position
        /*mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));*/

        latitude=latLng.latitude;
        longitude=latLng.longitude;

        if(myBusiness != null){
            myBusiness.remove();

        }
        //Adding marker to map
        myBusiness = mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("İsyerim")); //Adding a title

        //Moving the camera
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

        /*Intent i = new Intent(maps_activity.this, firma_bilgileri_girme.class);
        i.putExtra("lat",latitude);
        i.putExtra("lon",longitude);
        startActivity(i);*/
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(myBusiness)){
            Double lat=marker.getPosition().latitude;//firma's latitude
            Double lon=marker.getPosition().longitude;//firma's longtitude

            Intent i = new Intent(maps_activity.this, firma_bilgileri_girme.class);
            i.putExtra("lat",lat);
            i.putExtra("lon", lon);
            startActivity(i);
        }else{
            Toast.makeText(this, "Yeni Bir İşyeri Seçiniz!.", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void anasayfa(View view){
        startActivity(new Intent(this, main_page_for_firma.class));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        //latitude = marker.getPosition().latitude;
        //longitude = marker.getPosition().longitude;

        //Moving the map
        //moveMap();
    }

    public void cikis(View view){
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {}
    @Override
    public void onMarkerDrag(Marker marker) {}
    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onClick(View v) {}
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    private void getFirmData(){

        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchLocationDataInBackground(new GetFirmCallBack() {
            @Override
            public void done(ArrayList<Firma> returnedFirma) {
                if (returnedFirma != null) {

                    for (int i = 0; i < returnedFirma.size(); i++) {

                        long tcno = returnedFirma.get(i).getTcno();
                        User user=userLocalStore.getLoggedInUser();

                        if (tcno==user.getTc_no()){
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            mMap.addMarker(
                                    new MarkerOptions()
                                            .position(location)
                                            .title(returnedFirma.get(i).getFirmaAdi())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            );
                        }
                    }
                } else {
                    Toast.makeText(maps_activity.this, "Internet Baglatinizi Kontrol Ediniz!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}