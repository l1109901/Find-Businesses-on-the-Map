package ru.mail.loginregister;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;
import ru.mail.loginregister.calisan.main_page_for_isci;
import ru.mail.loginregister.siniflar.Firma;
import ru.mail.loginregister.siniflar.UserLocalStore;

/**
 * Created by gafur on 5/21/2016.
 */
public class ayrintili_arama_sonuclari_calisan extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleMap mMap;
    UserLocalStore userLocalStore;

    //Google ApiClient
    private GoogleApiClient googleApiClient;

    private String il,ilce;

    List<LatLng> points=new ArrayList<>();

    private Marker myMarker;
    private LatLng myLocation=null;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tumisyeri_arama_calisan);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(ayrintili_arama_sonuclari_calisan.this);

        userLocalStore=new UserLocalStore(this);

        Bundle extras=getIntent().getExtras();
        il=extras.getString("il");
        ilce= extras.getString("ilce");

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);

        Location location = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        //myLocation=new LatLng(location.getLatitude(),location.getLongitude());

        getFirmData();
    }

    @Override
    public void onLocationChanged(Location location) {

        if(myMarker != null){
            myMarker.remove();

        }
        myLocation=new LatLng(location.getLatitude(),location.getLongitude());
        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();
        // Setting the position of the marker
        options.position(myLocation);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        options.title("Ben");

        // Add new marker to the Google Map Android API V2
        myMarker=mMap.addMarker(options);

    }

    private void getFirmData(){

        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchLocationDataInBackground(new GetFirmCallBack() {
            @Override
            public void done(ArrayList<Firma> returnedFirma) {
                if (returnedFirma.size()!=0) {

                    for (int i = 0; i < returnedFirma.size(); i++) {
                        String rt_il = returnedFirma.get(i).getIl().toString();
                        String rt_ilce = returnedFirma.get(i).getIlce();

                        if (rt_il.equals(il) && rt_ilce.equals(ilce)) {//il ve ilce secilmis ise
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        } else if (rt_il.equals(il) && ilce.equals("Ilce Seciniz")) {//sadece il secilmis ise
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        } else if (rt_ilce.equals(ilce) && il.equals("Il seciniz")) {//sadece ilce secilmis ise
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        } else if (il.equals("Il seciniz") && ilce.equals("Ilce Seciniz")) {//hicbisey secilmemis ise
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        }


                    }

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(0)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 12));

                } else {
                    Toast.makeText(ayrintili_arama_sonuclari_calisan.this, "Kriterlere uygun işyeri bulunmamaktadır !",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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

    public void anasayfa(View view){
        mMap.clear();
        startActivity(new Intent(this, main_page_for_isci.class));
    }

    public void cikis(View view){
        startActivity(new Intent(this,MainActivity.class));
    }

    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback,boolean instant) {
        if (instant) {
            mMap.animateCamera(update, 1, callback);
        } else {
            mMap.animateCamera(update, 4000, callback);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
