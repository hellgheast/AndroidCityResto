package iee3.he_arc.cityresto;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
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


public class ServiceGoogleHelper extends Service implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener
{

    private final Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location        mLastLocation;
    protected static final String TAG = "ServiceGoogleHelper";


    public ServiceGoogleHelper(Context context)
    {
        this.mContext = context;
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
                .addOnConnectionFailedListener(this)
                .build();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //TODO Implémenter des fonctions de gestions des paramètres
        mGoogleApiClient.connect();
        Log.i(TAG,"Google API Connected");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle)
    {
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

    //Méthodes

    //Localisation
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
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
            mKeywords = _Keywords.clone();
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
                mKeywords = params[0].getmKeywords().clone();
                for(String i:mKeywords)
                {
                    mKeywordString += i+",";
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


}