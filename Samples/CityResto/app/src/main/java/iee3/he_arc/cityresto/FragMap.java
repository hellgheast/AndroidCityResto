package iee3.he_arc.cityresto;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.location.Location;
import android.location.LocationManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragMap extends Fragment {

    private CheckBox cbList;
    MapView mapView;
    GoogleMap map;
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
    ServiceGPSTracker gps;

    public FragMap() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frag_map, container, false);

        // Initialize checkbox for switching between map and list
        cbList = (CheckBox) v.findViewById(R.id.cbSwitchList);

        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        // create class object
        gps = new ServiceGPSTracker(getContext());

        // check if GPS enabled
        if(!gps.canGetLocation()){
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();

        }else{
            // Updates the location and zoom of the MapView
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(gps.getLatitude(), gps.getLongitude()))
                    .title("You are here"));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 13);
            map.animateCamera(cameraUpdate);

        }

        // TODO : Actualiser le rayon lorsqu'on le modifie
        Circle circle = map.addCircle(new CircleOptions()
                .center(new LatLng(gps.getLatitude(), gps.getLongitude()))
                .radius(ClassMainStorageManager.getRadius(getContext()))
                .strokeColor(Color.CYAN));

        // Switch to List
        cbList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Check if user want to switch to List
                if(cbList.isChecked()){
                    ActMainResto.switchList(getContext());
                    ActMainResto.setPage(getContext());
                }
            }
        });



        return v;
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
