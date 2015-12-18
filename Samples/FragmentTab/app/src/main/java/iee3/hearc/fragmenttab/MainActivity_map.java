package iee3.hearc.presentationgooglemaps;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener,MapFragmentPerso.OnFragmentInteractionListener
{

    //Attributs
    GoogleMap gmp=null; //Map que l'on va modifier
    Marker mkr=null;//Marquer
    Circle circle=null;//Cercle
    Polyline line=null;//Ligne
    @Override
    protected void onCreate(Bundle savedInstanceState) {
               super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapfrag = (MapFragment) getFragmentManager().findFragmentById(R.id.location_map);

        mapfrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmp=googleMap;

        // Mise en place d'un markeur
        mkr=googleMap.addMarker(new MarkerOptions()
                .alpha((float)0.5)
                .position(new LatLng(0, 0))
                .title("Marker")
                .snippet("Test")
                );

        //Déplacement de la caméra
        //Simple
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));
        //Avec Zoom
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), (float) (1.0)));
        //Le plus complet
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(0, 0))
                        .tilt((float) 2.0)
                        .zoom((float) 0.0)
                        .bearing((float) 1.0)
                        .build()
        ));


        //Personnalisation des paramètres graphiques via l'objet google map
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);

        //Ajout d'un listener
        googleMap.setOnMapClickListener(this);

        //Ajout d'une mise à jour graphique de l'infowindow si on veut avoir une fenêtre custom
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
        {
            @Override
            public View getInfoWindow(Marker marker)
            {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker)
            {
                View v = (View) getLayoutInflater().inflate(R.layout.info_window_layout,null);

                //On indique les positions sur l'infowindow
                TextView inf_lat = (TextView)v.findViewById(R.id.infw_lat);
                TextView inf_lng = (TextView)v.findViewById(R.id.infw_lng);
                TextView inf_title = (TextView) v.findViewById(R.id.infw_title);
                TextView inf_snippet = (TextView)v.findViewById(R.id.infw_snippet);

                inf_lat.setText("LAT :"+marker.getPosition().latitude);
                inf_lng.setText("LNG :"+marker.getPosition().longitude);
                inf_snippet.setText("Test");
                inf_title.setText("Marker");

                return v;
            }
        });




    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        Toast.makeText(MainActivity.this, latLng.toString(), Toast.LENGTH_SHORT).show();


        //On dessine une ligne
        gmp.addPolyline(new PolylineOptions()
                        .add(latLng)
                        .add(new LatLng(0,0))
                        .color(Color.RED)
                        .visible(true));
    }


}
