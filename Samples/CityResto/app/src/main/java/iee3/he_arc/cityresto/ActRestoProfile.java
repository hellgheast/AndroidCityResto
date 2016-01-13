package iee3.he_arc.cityresto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

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


public class ActRestoProfile extends AppCompatActivity {

    private String restoID;
    private Place resto;
    private float lRestoRating;
    private ImageButton lStarButton;
    private boolean lIsFavourite = false;

    private ClassInternRestaurant mClassInternRestaurant;
    private ClassPermanentDataHelper mClassPermanentDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_resto_profile);
        lStarButton = (ImageButton) findViewById(R.id.ibStar);

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




        // Find which Resto correspond to the ID

        RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        lRestoRating = resto.getRating();
        ratingbar.setRating(lRestoRating);

        TextView lRestoName = new TextView(this);
        lRestoName = (TextView) findViewById(R.id.tvProfileRestoName);
        lRestoName.setText(resto.getName());

        TextView lRestoAddress = new TextView(this);
        lRestoAddress = (TextView) findViewById(R.id.tvProfileRestoAddress);
        lRestoAddress.setText(resto.getVicinity());

        mClassPermanentDataHelper = new ClassPermanentDataHelper(this);
        mClassInternRestaurant = mClassPermanentDataHelper.readRestaurantPlaceID(resto.getPlaceId().getId());
       // mClassInternRestaurant = null if resto isn't saved


        if(null == mClassInternRestaurant){
            // This resto is not saved, it's not a favourite
            lStarButton.setBackgroundResource(R.drawable.empty_star2);
            lIsFavourite = false;
        }
        else {
            lStarButton.setBackgroundResource(R.drawable.full_star);
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
                    lStarButton.setBackgroundResource(R.drawable.empty_star2);

                    // Remove this resto frome the database
                    mClassPermanentDataHelper.deleteRestaurant(mClassInternRestaurant);
                }
                else{
                    lIsFavourite = true;
                    lStarButton.setBackgroundResource(R.drawable.full_star);

                    // Add this resto to the database
                    mClassPermanentDataHelper.addRestaurant(mClassInternRestaurant);
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
        //Intent intent = new Intent(ActRestoProfile.this ,ActMainResto.class);
       // startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onPause() {
        super.onPause();
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
}
