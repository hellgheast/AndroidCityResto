package iee3.he_arc.cityresto;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;

import java.io.IOException;
import java.util.List;

public class FragMapList extends Fragment implements OnMapReadyCallback,GoogleMap.OnMapClickListener
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
        listView.setVisibility(listView.GONE);




        // MAP ---------------------------------------------------------------------
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());





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
                .strokeColor(Color.BLUE)
                .strokeWidth(1));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                ClassMainStorageManager.gps.getLastLocationLatLng().longitude), 13));

    }

    @Override
    public void onStart()
    {
        super.onStart();
        //Partie qui nous permmetre d'ajouter les markeurs au bon moment (quand on recoit un message du via le broadcast
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceGoogleHelper.GOOGLEAPICONNECTED);
        intentFilter.addAction(FragParameters.ACCEPTPARAMETERS);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    public void onStop() {
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
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }



    //BroadcastReceiver qui nous permet de rajouter correctement les markeurs sur la carte!
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase(ServiceGoogleHelper.GOOGLEAPICONNECTED))
            {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                                ClassMainStorageManager.gps.getLastLocationLatLng().longitude))
                        .title("You are here")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

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

                circle = map.addCircle(new CircleOptions()
                        .center(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                                ClassMainStorageManager.gps.getLastLocationLatLng().longitude))
                        .radius(ClassMainStorageManager.getRadius(getContext()))
                        .strokeColor(Color.BLUE)
                        .strokeWidth(1));

                //Partie population de la listview
                listView.setAdapter(new ClassRestoAdapter(getActivity(),ClassMainStorageManager.gps.getPlaces(Integer.valueOf(ClassMainStorageManager.getRadius(getContext())),null)));

                ClassMainStorageManager.lListOfRestaurants = ClassMainStorageManager.gps.getPlaceNoArg();

                //Population de markeurs
                for(Place place : ClassMainStorageManager.lListOfRestaurants )
                {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(place.getLatitude(),place.getLongitude()))
                            .title(place.getName()));
                }



            }
            else if (intent.getAction().equalsIgnoreCase(FragParameters.ACCEPTPARAMETERS))
            {
                Toast.makeText(getContext(),"FragParamOk",1000).show();
                map.clear();
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                                ClassMainStorageManager.gps.getLastLocationLatLng().longitude))
                        .title("You are here")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                        ClassMainStorageManager.gps.getLastLocationLatLng().longitude), 13);
                map.animateCamera(cameraUpdate);


                circle = map.addCircle(new CircleOptions()
                        .center(new LatLng(ClassMainStorageManager.gps.getLastLocationLatLng().latitude,
                                ClassMainStorageManager.gps.getLastLocationLatLng().longitude))
                        .radius(ClassMainStorageManager.getRadius(getContext()))
                        .strokeColor(Color.BLUE)
                        .strokeWidth(1));


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



                //Partie population de la listview
                listView.setAdapter(new ClassRestoAdapter(getActivity(),ClassMainStorageManager.gps.getPlaces(Integer.valueOf(ClassMainStorageManager.getRadius(getContext())),null)));

                ClassMainStorageManager.lListOfRestaurants = ClassMainStorageManager.gps.getPlaceNoArg();

                //Population de markeurs
                for(Place place : ClassMainStorageManager.lListOfRestaurants )
                {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(place.getLatitude(),place.getLongitude()))
                            .title(place.getName()));
                }

            }
        }
    };
}
