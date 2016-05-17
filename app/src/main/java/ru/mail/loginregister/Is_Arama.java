package ru.mail.loginregister;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class Is_Arama extends FragmentActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    UserLocalStore userLocalStore;

    //Google ApiClient
    private GoogleApiClient googleApiClient;
    private Button geri;

    List<LatLng> points=new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_arama);

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

    public void anamenu(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng turkey = new LatLng(39.1667, 35.6667);
        //mMap.addMarker(new MarkerOptions().position(turkey).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(turkey));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(turkey, 6));
        mMap.setOnMarkerClickListener(this);
    }

    private void getFirmData(){
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchLocationDataInBackground(new GetFirmCallBack() {
            @Override
            public void done(ArrayList<Firma> returnedFirma) {
                double sum_lat=0,sum_lon=0,ort_lat,ort_lon;

                for (int i = 0; i < returnedFirma.size(); i++) {
                    LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                    points.add(location);

                    sum_lat=sum_lat+points.get(i).latitude;
                    sum_lon=sum_lon+points.get(i).longitude;

                    mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                }

                ort_lat=sum_lat/returnedFirma.size();
                ort_lon=sum_lon/returnedFirma.size();
                LatLng result=new LatLng(ort_lat,ort_lon);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(result));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(result, 6));
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
    public void onConnected(Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public boolean onMarkerClick(final Marker marker) {

        String firma_adi=marker.getTitle().toString();
        Double lat=marker.getPosition().latitude;//firma's latitude
        Double lon=marker.getPosition().longitude;//firma's longtitude

        Intent i = new Intent(Is_Arama.this, Randevu_Islemleri.class);
        i.putExtra("firma_adi",firma_adi);
        i.putExtra("lat",lat);
        i.putExtra("lon", lon);
        startActivity(i);
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void anasayfa(View view){
        startActivity(new Intent(this,MainActivity.class));
    }

    public void cikis(View view){
        startActivity(new Intent(this,Login.class));
    }
}
