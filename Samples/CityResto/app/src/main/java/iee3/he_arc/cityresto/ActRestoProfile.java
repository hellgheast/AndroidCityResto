package iee3.he_arc.cityresto;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.io.Closeables;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;
import net.sf.sprockets.google.PlacesParams;
import net.sf.sprockets.google.StreetView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import iee3.he_arc.cityresto.InternDB.ClassInternRestaurant;
import iee3.he_arc.cityresto.InternDB.ClassInternUser;
import iee3.he_arc.cityresto.Utils.ClassSerialPlace;


public class ActRestoProfile extends AppCompatActivity {

    private List<String> lListOpenInfos;
    private String[] lListRestoHours = new String[7];
    private String restoID;
    private ClassSerialPlace lSerizedResto;
    private Place resto;
    private float lRestoRating;
    private ImageButton lStarButton;
    private boolean lIsFavourite = false;
    private ImageView lImageResto;
    private Bitmap bitmap;

    private ClassInternRestaurant mClassInternRestaurant;
    private ClassPermanentDataHelper mClassPermanentDataHelper;
    public static final String BACKTOACT = "BACKTOACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_resto_profile);
        lStarButton = (ImageButton) findViewById(R.id.ibStar);
        lImageResto = (ImageView) findViewById(R.id.ivRestoImage);

        Intent intent = getIntent();
        restoID = intent.getStringExtra("markerID");

        try
        {
            resto = new GetPlaceDetail().execute(restoID).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        new GetPlacePhotoTask(resto).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);


        TextView lRestoRating = new TextView(this);
        lRestoRating = (TextView) findViewById(R.id.tvRestoRating);
        lRestoRating.setText(resto.getRating() + "");

        TextView lRestoName = new TextView(this);
        lRestoName = (TextView) findViewById(R.id.tvProfileRestoName);
        lRestoName.setText(resto.getName());

        TextView lRestoAddress = new TextView(this);
        lRestoAddress = (TextView) findViewById(R.id.tvProfileRestoAddress);
        lRestoAddress.setText(resto.getVicinity());

        TextView lRestoPhoneNumber = new TextView(this);
        lRestoPhoneNumber = (TextView) findViewById(R.id.tvProfilePhoneNumber);
        lRestoPhoneNumber.setText(resto.getFormattedPhoneNumber());

        lListOpenInfos = resto.getFormattedOpeningHours();

        if(lListOpenInfos.isEmpty()){

            int i;
            for(i=0 ; i<lListRestoHours.length ; i++) {
                lListRestoHours[i] = "Hours not founded";
            }
        }else {
            // Memorize list of opening hours in an array of Strings
            (resto.getFormattedOpeningHours()).toArray(lListRestoHours);
        }

        // Hours of the days of the week
        TextView lRestoHourMonday = new TextView(this);
        lRestoHourMonday = (TextView) findViewById(R.id.tvHourMonday);
        lRestoHourMonday.setText(lListRestoHours[0]);

        TextView lRestoHourTuesday = new TextView(this);
        lRestoHourTuesday = (TextView) findViewById(R.id.tvHourTuesday);
        lRestoHourTuesday.setText(lListRestoHours[1]);

        TextView lRestoHourWednesday = new TextView(this);
        lRestoHourWednesday = (TextView) findViewById(R.id.tvHourWednesday);
        lRestoHourWednesday.setText(lListRestoHours[2]);

        TextView lRestoHourThursday = new TextView(this);
        lRestoHourThursday = (TextView) findViewById(R.id.tvHourThursday);
        lRestoHourThursday.setText(lListRestoHours[3]);

        TextView lRestoHourFriday = new TextView(this);
        lRestoHourFriday = (TextView) findViewById(R.id.tvHourFriday);
        lRestoHourFriday.setText(lListRestoHours[4]);

        TextView lRestoHourSaturday = new TextView(this);
        lRestoHourSaturday = (TextView) findViewById(R.id.tvHourSaturday);
        lRestoHourSaturday.setText(lListRestoHours[5]);

        TextView lRestoHourSunday = new TextView(this);
        lRestoHourSunday = (TextView) findViewById(R.id.tvHourSunday);
        lRestoHourSunday.setText(lListRestoHours[6]);



        mClassPermanentDataHelper = new ClassPermanentDataHelper(this);
        mClassInternRestaurant = mClassPermanentDataHelper.readRestaurantPlaceID(resto.getPlaceId().getId());
       // mClassInternRestaurant = null if resto isn't saved


        if(null == mClassInternRestaurant){
            // This resto is not saved, it's not a favourite
            lStarButton.setBackgroundResource(R.drawable.fav_emptystar);
            lIsFavourite = false;
        }
        else {
            lStarButton.setBackgroundResource(R.drawable.fav_fullstar);
            lIsFavourite = true;
        }

        // Now create an object for this resto, for delete or add it to the database
        mClassInternRestaurant = new ClassInternRestaurant(resto.getPlaceId().getId(),
                                                            resto.getName(),
                                                            resto.getAddress().toString());

        lStarButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(lIsFavourite) {
                    // If favourite, toggle favorite to false and put empty star
                    lIsFavourite = false;
                    lStarButton.setBackgroundResource(R.drawable.fav_emptystar);

                    // Remove this resto frome the database
                    mClassPermanentDataHelper.deleteRestaurant(mClassInternRestaurant);

                    Toast.makeText(getBaseContext()
                            , (R.string.FavouriteRemoved)
                            , Toast.LENGTH_LONG).show();
                }
                else{
                    lIsFavourite = true;
                    lStarButton.setBackgroundResource(R.drawable.fav_fullstar);

                    // Add this resto to the database
                    mClassPermanentDataHelper.addRestaurant(mClassInternRestaurant);

                    Toast.makeText(getBaseContext()
                            , (R.string.FavouriteAdded)
                            , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_resto_profile, menu);
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
    public void onBackPressed() {
        Intent intent = new Intent(ActRestoProfile.this ,ActMainResto.class);
        startActivity(intent);
        LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(new Intent(BACKTOACT));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class GetPlaceDetail extends AsyncTask<String,Void,Place> {

        private net.sf.sprockets.google.Places.Response<Place> mResp;
        private String[] mKeywords;
        private String mKeywordString;

        @Override
        protected Place doInBackground(String... params)
        {
            try {
                if (params == null) {
                    return null;
                }

                mResp = Places.details(
                        Places.Params
                                .create()
                                .placeId(params[0]));

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            String status = mResp.getStatus();
            Place mResultPlace = mResp.getResult();

            //Verification of if the result is okay
            if (Places.Response.STATUS_OK.equals(status)) {
                return mResultPlace;
            } else if (Places.Response.STATUS_ZERO_RESULTS.equals(status)) {
                System.out.println("No results");
                return null;
            } else {
                System.out.println("Error" + mResp.getErrorMessage());
                return null;
            }
        }
    }




    private class GetPlacePhotoTask extends AsyncTask <Void,Void,Bitmap>
    {
        private Place      mPlace;


        public GetPlacePhotoTask (Place _Place)
        {
            mPlace = _Place;
        }



        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Bitmap doInBackground(Void... params) {
            StreetView.Response<InputStream> imagestreet = null;
            Places.Response<InputStream> imageplace = null;
            try {
                //Méthode Place
                List<Place.Photo> photos = mPlace.getPhotos();
                if(!photos.isEmpty())
                {
                    if(!ActMainResto.mDiskLruImageCache.containsKey(mPlace.getPlaceId().getId()))
                    {
                        Place.Photo photo = photos.get(0);
                        PlacesParams placesparams = Places.Params.create().reference(photo.getReference());
                        imageplace = Places.photo(placesparams);
                    }
                }
                else
                {
                    if(!ActMainResto.mDiskLruImageCache.containsKey(mPlace.getPlaceId().getId()))
                    {
                        //Méthode stretview
                        imagestreet = StreetView.image(StreetView.Params.create()
                                .longitude(mPlace.getLongitude()));
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            if(imagestreet!=null)
            {
                int status = imagestreet.getStatus();
                InputStream in = imagestreet.getResult();

                if (status == HttpURLConnection.HTTP_OK && in != null) {
                    Bitmap bitmapPhoto = (BitmapFactory.decodeStream(in)); // Get image in an asynchronous task
                    Closeables.closeQuietly(in);

                    return bitmapPhoto;

                } else {
                    System.out.println("error, HTTP status code: " + status);
                    return null;
                }

            }
            else
            {
                if(imageplace!=null)
                {
                    InputStream in = imageplace.getResult();
                    String status = imageplace.getStatus();

                    if (Places.Response.STATUS_OK.equals(status) && in != null) {
                        Bitmap bitmapPhoto = (BitmapFactory.decodeStream(in)); // Get image in an asynchronous task
                        Closeables.closeQuietly(in);
                        return bitmapPhoto;
                    } else {
                        System.out.println("error: " + imageplace.getErrorMessage());
                        return null;
                    }
                }
                return null;
            }
        }

        @Override
        protected  void onPostExecute(Bitmap result)
        {

            if (result!=null)
            {
                  //Check if image is already in cache
                    if(ActMainResto.mDiskLruImageCache.containsKey(mPlace.getPlaceId().getId()))
                    {
                        lImageResto.setImageBitmap(result);
                    }
                    else
                    {
                        ActMainResto.mDiskLruImageCache.put(mPlace.getPlaceId().getId(), result); // Save image in disk cache
                        lImageResto.setImageBitmap(result);
                    }

            }
            //Check if image is already in cache
            if(ActMainResto.mDiskLruImageCache.containsKey(mPlace.getPlaceId().getId()))
            {
               bitmap = (ActMainResto.mDiskLruImageCache.getBitmap(mPlace.getPlaceId().getId()));
                lImageResto.setImageBitmap(bitmap);
            }

        }
    }
}