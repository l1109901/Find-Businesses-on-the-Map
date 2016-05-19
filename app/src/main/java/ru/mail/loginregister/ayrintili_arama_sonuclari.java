package ru.mail.loginregister;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import java.util.ArrayList;
import java.util.List;

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
        GMapV2Direction.DirecitonReceivedListener,LocationListener {

    private Button btnDirection;
    ToggleButton tbMode;

    private GoogleMap mMap;
    UserLocalStore userLocalStore;

    //Google ApiClient
    private GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;

    private LatLng myLocation=null;

    //To store longitude and latitude from map
    private double longitude;
    private double latitude;

    private String il,ilce,alan;
    List<Firma> isyerleri=new ArrayList<>();

    List<LatLng> points=new ArrayList<>();

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
        ilce= extras.getString("ilce");
        alan= extras.getString("alan");

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API)
                .build();

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

                    Firma myfirm=new Firma("Ben",myLocation.latitude,myLocation.longitude);
                    points.add(myLocation);
                    isyerleri.add(myfirm);

                    for (int i = 0; i < returnedFirma.size(); i++) {

                        String rt_il = returnedFirma.get(i).getIl().toString();
                        String rt_ilce = returnedFirma.get(i).getIlce();
                        String rt_alan = returnedFirma.get(i).getAlan();

                        if (rt_il.equals(il) && rt_ilce.equals(ilce)) {//il ve ilce secilmis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            //mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        } else if (rt_il.equals(il) && ilce.equals("Ilce Seciniz")) {//sadece il secilmis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            //mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        } else if (rt_ilce.equals(ilce) && il.equals("Il seciniz")) {//sadece ilce secilmis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            //mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        } else if (il.equals("Il seciniz") && ilce.equals("Ilce Seciniz")) {//hicbisey secilmemis ise
                            isyerleri.add(returnedFirma.get(i));
                            LatLng location = new LatLng(returnedFirma.get(i).getLatitude(), returnedFirma.get(i).getLongtitude());
                            points.add(location);
                            //mMap.addMarker(new MarkerOptions().position(location).title(returnedFirma.get(i).getFirmaAdi()));
                        }
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(0)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 12));

                    mMap.addMarker(
                            new MarkerOptions()
                                    .position(myLocation)
                                    .title("Ben")
                    );

                    for(int i=0;i<isyerleri.size();i++){
                        LatLng location = new LatLng(isyerleri.get(i).getLatitude(), isyerleri.get(i).getLongtitude());

                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(location)
                                        .title(isyerleri.get(i).getFirmaAdi())
                        );
                    }

                } else {
                    Toast.makeText(ayrintili_arama_sonuclari.this, "Internet Baglatinizi Kontrol Ediniz!",
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
                if(tmp<minDistance){
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
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
        mMap.clear();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);

        Location location=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        myLocation=new LatLng(location.getLatitude(),location.getLongitude());
    }



    @Override
    public void onClick(View v) {

        if (v == btnDirection) {
            mMap.clear();

            sortByDistance();

            if (tbMode.isChecked()) {

                for(int i=0;i<points.size()-1;i++){
                    new GetRotueListTask(this, points.get(i),
                            points.get(i+1), GMapV2Direction.MODE_DRIVING, this)
                            .execute();
                }

            } else {
                for(int i=0;i<points.size()-1;i++){
                    new GetRotueListTask(this, points.get(i),
                            points.get(i+1), GMapV2Direction.MODE_WALKING, this)
                            .execute();
                }
            }
        }
    }

    @Override
    public void OnDirectionListReceived(List<LatLng> mPointList) {

        for(int i=0;i<isyerleri.size();i++) {
            LatLng location = new LatLng(isyerleri.get(i).getLatitude(), isyerleri.get(i).getLongtitude());

            mMap.addMarker(
                    new MarkerOptions()
                            .position(location)
                            .title(isyerleri.get(i).getFirmaAdi())
            );
        }

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

    @Override
    public void onLocationChanged(Location location) {
        myLocation=new LatLng(location.getLatitude(),location.getLongitude());
    }
}