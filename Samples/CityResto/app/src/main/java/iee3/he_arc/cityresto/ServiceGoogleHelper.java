package iee3.he_arc.cityresto;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ServiceGoogleHelper extends Service implements LocationListener,GoogleApiClient.ConnectionCallbacks
{

    public static final String GOOGLEAPICONNECTED = "ISCONNECTED";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location        mLastLocation;
    protected static final String TAG = "ServiceGoogleHelper";

    //Remplacement des différents Implementations



    public ServiceGoogleHelper()
    {
        super();
    }


    //Partie communication via Binding

    final IBinder mBinder = new LocalBinder();

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        createLocationRequest();
        startLocationUpdates();
        Log.i(TAG, "Google API Connected");


        LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(new Intent(GOOGLEAPICONNECTED)); //Envoi du message en broadcast afin que l'on puisse rajouter les markeurs correctement
        //Raison : Ordre d'execution sur ServiceConnection qui se lance après le onStart

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }
    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();

    }


    public class LocalBinder extends Binder {
        ServiceGoogleHelper getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServiceGoogleHelper.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        mGoogleApiClient.connect();
        Log.i(TAG, "Google API try Connection");
        return mBinder;
    }



    @Override
    public void onCreate()
    {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .addApi(LocationServices.API) // On rajoute le service de localisation
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
                    }
                })
                .build();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //TODO Implémenter des fonctions de gestions des paramètres
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        mGoogleApiClient.connect();
        Log.i(TAG, "Google API Connected");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
    }





    //Méthodes



    //Localisation
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    //Recupération des données.
    public Location getmLastLocation ()
    {
        return mLastLocation;
    }

    public LatLng getLastLocationLatLng()
    {
        return new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
    }

    /**
     * getPlaces
     * Get the places corresponding to the right parameters
     *
     * @param  _Radius : The radius within we search a restaurants
     * @param  _Keywords : The keywords for the restaurants
     * @return List<Place> LPlacesResult
     */
    List<Place> getPlaces(Integer _Radius,String [] _Keywords)
    {
        List<Place> mResultPlace;

        try
        {
            mResultPlace=new GetPlacesTask().execute(new SearchParams(_Radius, _Keywords)).get();
            return mResultPlace;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<Place> getPlaceNoArg()
    {
        List<Place> mResultPlace;
        try{
            mResultPlace=new GetPlacesTask().execute(new SearchParams(ClassMainStorageManager.getRadius(getApplicationContext()),null)).get();
            return mResultPlace;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Private class used to send the parameters to the AsyncTask
     */
    private class SearchParams
    {
        String [] mKeywords;
        Integer mRadius;

        /*Constructeurs*/
        SearchParams(Integer _Radius,String [] _Keywords)
        {
            mRadius=_Radius;
            if(_Keywords==null)
            {
                mKeywords=null;
            }
            else
            {
                mKeywords = _Keywords.clone();
            }

        }

        /*Getters*/

        public String[] getmKeywords() {
            return mKeywords;
        }

        public Integer getmRadius() {
            return mRadius;
        }


    }

    private class GetPlacesTask extends AsyncTask<SearchParams,Void,List<Place>>
    {

        private net.sf.sprockets.google.Places.Response<List<Place>> mResp;
        private String [] mKeywords;
        private String mKeywordString;

        @Override
        protected List<Place> doInBackground(SearchParams... params)
        {
            try
            {
                if(params[0].getmKeywords()==null)
                {
                    mKeywordString="";
                }
                else
                {
                    mKeywords = params[0].getmKeywords().clone();
                    for(String i:mKeywords)
                    {
                        mKeywordString += i+",";
                    }
                }



                mResp = Places.nearbySearch(
                        Places.Params
                                .create()
                                .latitude(mLastLocation.getLatitude())
                                .longitude(mLastLocation.getLongitude())
                                .radius(params[0].getmRadius())
                                .addTypes("restaurant").keyword(mKeywordString));
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
            String status = mResp.getStatus();
            List<Place> mResultPlace = mResp.getResult();

            //Verification of if the result is okey
            if(Places.Response.STATUS_OK.equals(status))
            {
                return mResultPlace;
            }
            else if(Places.Response.STATUS_ZERO_RESULTS.equals(status))
            {
                System.out.println("No results");
                return null;
            }
            else {
                System.out.println("Error" + mResp.getErrorMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
        }
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        final Context mContext = getApplicationContext();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS activation");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to activate it ?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}