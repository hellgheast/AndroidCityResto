package iee3.hearc.googleplacetest;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;




import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import net.sf.sprockets.content.GooglePlacesLoader;
import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;
import net.sf.*;

import java.io.IOException;
import java.util.List;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback,GoogleMap.OnMapClickListener,LocationListener
{

    final int PLACE_PICKER_REQUEST = 1;
    protected static final String TAG = "MainActivity";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location  mLastLocation;
    //Location request
    private LocationRequest mLocationRequest;
    //API Loader request


    //Graphic view
    private Button RequestPlace=null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestPlace = (Button)findViewById(R.id.BTRequestPlace);


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .addApi(LocationServices.API) // On rajoute le service de localisation
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    /*Configuration  de test*/


    };

    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume()
    {
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
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    /*Partie Google Maps*/
    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    /*Partie localisation*/
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

    /*Partie Handler*/
    public void BT_RequestPlace (View view)
    {
        new GetPlacesTask().execute(1000);
    }
    public void BT_RequestGps(View view)
    {
        startLocationUpdates();
    }


    private class GetPlacesTask extends AsyncTask<Integer,Void,Places.Response<List<Place>>>
    {
        private net.sf.sprockets.google.Places.Response<List<net.sf.sprockets.google.Place>> mResp;

        @Override
        protected Places.Response<List<Place>> doInBackground(Integer... radius) {
            try
            {
                mResp = Places.nearbySearch(Places.Params.create().latitude(mLastLocation.getLatitude()).longitude(mLastLocation.getLongitude()).radius(1000)
                        .addTypes("restaurant"));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            String status = mResp.getStatus();
            List<Place> places = mResp.getResult();

            if (Places.Response.STATUS_OK.equals(status))
            {
                for (Place place : places)
                {
                    System.out.println(place.getName() + " @ " + place.getVicinity());

                }
                return mResp;
            }
            else if (Places.Response.STATUS_ZERO_RESULTS.equals(status)) {
                System.out.println("no results");
                return null;
            } else {
                System.out.println("error: " + status);
                return null;
            }

        }
            protected void onPostExecute(Places.Response<List<Place>> result)
            {
                System.out.println("Got a result !");
            }


    }
}
