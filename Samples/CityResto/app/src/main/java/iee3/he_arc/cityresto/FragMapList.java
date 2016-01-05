package iee3.he_arc.cityresto;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import android.location.Location;
import android.location.LocationManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;

import java.io.IOException;
import java.util.List;

public class FragMapList extends Fragment implements ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
        ,OnMapReadyCallback,GoogleMap.OnMapClickListener,LocationListener
{

    private Switch swList;
    MapView mapView;
    static GoogleMap map;
    LatLng CENTER = null;

    public LocationManager locationManager;

    double longitudeDouble;
    double latitudeDouble;

    String snippet;
    String title;
    Location location;
    String myAddress;

    String LocationId;
    String CityName;
    String imageURL;

    // GPSTracker class
    static ServiceGPSTracker gps;

    ListView listView;
    private GoogleApiClient mGoogleApiClient;
    private  Location mLastLocation;
    //Location request
    private  LocationRequest mLocationRequest;
    final int PLACE_PICKER_REQUEST = 1;
    protected static final String TAG = "FragMapList";


    private static Circle circle;
    private static Context fragMapListContext;

    public FragMapList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frag_map_list, container, false);


        fragMapListContext = getContext();

        // Initialize checkbox for switching between map and list
        swList = (Switch) v.findViewById(R.id.cbSwitchList);
        listView = (ListView) v.findViewById(R.id.listView);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .addApi(LocationServices.API) // On rajoute le service de localisation
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // MAP ---------------------------------------------------------------------
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());




        map.addMarker(new MarkerOptions()
                .position(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                        ClassMainStorageManager.gps.getLastLocationLatLng().longitude))
                .title("You are here"));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                ClassMainStorageManager.gps.getLastLocationLatLng().longitude), 13);
        map.animateCamera(cameraUpdate);


        // Update camera when user touche the map
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Log.d("Map", "Map clicked");
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                        ClassMainStorageManager.gps.getLastLocationLatLng().longitude), 13);
                map.animateCamera(cameraUpdate);
            }
        });

        // create class object
        //ClassMainStorageManager.gps = new ServiceGPSTracker(getContext());

        /*
        // check if GPS enabled
        if(gps.canGetLocation()){
            // Updates the location and zoom of the MapView
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(ClassMainStorageManager.gps.getLatitude(), ClassMainStorageManager.gps.getLongitude()))
                    .title("You are here"));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(ClassMainStorageManager.gps.getLatitude(), ClassMainStorageManager.gps.getLongitude()), 13);
            map.animateCamera(cameraUpdate);


        }else{
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
            Toast.makeText(getActivity(), (R.string.TouchMapForUpdate), Toast.LENGTH_LONG).show();
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {
                    Log.d("Map", "Map clicked");
                    gps = new ServiceGPSTracker(getContext());
                    // Updates the location and zoom of the MapView
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(gps.getLatitude(), gps.getLongitude()))
                            .title("You are here"));

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(ClassMainStorageManager.gps.getLatitude(), ClassMainStorageManager.gps.getLongitude()), 13);
                    map.animateCamera(cameraUpdate);

                }
            });
        }
*/


        circle = map.addCircle(new CircleOptions()
                .center(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                        ClassMainStorageManager.gps.getLastLocationLatLng().longitude))
                .radius(ClassMainStorageManager.getRadius(getContext()))
                .strokeColor(Color.CYAN));

        // Switch to List
        swList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Check if user want to switch to List
                if(swList.isChecked()){
                    mapView.setVisibility(mapView.GONE);
                    listView.setVisibility(listView.VISIBLE);

                }else{
                    mapView.setVisibility(mapView.VISIBLE);
                    listView.setVisibility(listView.GONE);
                }
            }
        });


        return v;
    }


    // Update for example Circle's radius
    public static void updateFragMapList(){
        circle.remove();
        circle = map.addCircle(new CircleOptions()
                .center(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                        ClassMainStorageManager.gps.getLastLocationLatLng().longitude))
                .radius(ClassMainStorageManager.getRadius(fragMapListContext))
                .strokeColor(Color.CYAN));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                ClassMainStorageManager.gps.getLastLocationLatLng().longitude), 13));

    }

    @Override
    public void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume()
    {
        mapView.onResume();
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected())
        {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }


    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");
        createLocationRequest();
        startLocationUpdates();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    }


    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    /*Partie Handler*/
    public void BT_RequestPlace (View view)
    {
        new GetPlacesTask().execute(1000);
    }
    public void BT_RequestGps(View view)
    {
        startLocationUpdates();
    }

    private class GetPlacesTask extends AsyncTask<Integer,Void,Places.Response<List<Place>>> {
        private net.sf.sprockets.google.Places.Response<List<net.sf.sprockets.google.Place>> mResp;

        @Override
        protected Places.Response<List<Place>> doInBackground(Integer... radius) {
            try {
                mResp = Places.nearbySearch(Places.Params.create().latitude(mLastLocation.getLatitude()).longitude(mLastLocation.getLongitude()).radius(1000)
                        .addTypes("restaurant"));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            String status = mResp.getStatus();
            List<Place> places = mResp.getResult();

            if (Places.Response.STATUS_OK.equals(status)) {
                for (Place place : places) {
                    System.out.println(place.getName() + " @ " + place.getVicinity());

                }
                return mResp;
            } else if (Places.Response.STATUS_ZERO_RESULTS.equals(status)) {
                System.out.println("no results");
                return null;
            } else {
                System.out.println("error: " + status);
                return null;
            }

        }

        protected void onPostExecute(Places.Response<List<Place>> result) {
            System.out.println("Got a result !");
            Toast.makeText(getActivity(), "salut", Toast.LENGTH_SHORT).show();
        }
    }
}
