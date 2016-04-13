package ru.mail.loginregister;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Is_Arama extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    UserLocalStore userLocalStore;

    //Google ApiClient
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is__arama);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userLocalStore=new UserLocalStore(this);

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();
        getFirmData();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng turkey = new LatLng(39.1667, 35.6667);
        //mMap.addMarker(new MarkerOptions().position(turkey).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(turkey));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(turkey, 6));

    }

    private void getFirmData(){
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchLocationDataInBackground(new GetFirmCallBack() {
            @Override
            public void done(ArrayList<Firma> returnedFirma) {
                for (int i=0;i<returnedFirma.size();i++){
                    LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                    mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                }
            }
        });
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


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
