package iee3.hearc.presentationgooglemaps;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener,MapFragmentPerso.OnFragmentInteractionListener
{

    //Attributs
    GoogleMap gmp=null; //Map que l'on va modifier

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
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));

        //Déplacement de la caméra
        //Simple
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));
        //Avec Zoom
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), (float) (1.0)));
        //Le plus complet
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                                                    .target(new LatLng(0,0))
                                                                    .tilt((float)2.0)
                                                                    .zoom((float)1.0)
                                                                    .bearing((float)1.0)
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

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        Toast.makeText(MainActivity.this, latLng.toString(), Toast.LENGTH_SHORT).show();
    }
}
