package iee3.he_arc.cityresto;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.common.io.Closer;
import com.google.common.io.Files;

import net.sf.sprockets.google.ImmutablePhoto;
import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;
import net.sf.sprockets.google.PlacesParams;
import net.sf.sprockets.google.StreetView;
import net.sf.sprockets.net.HttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;

import static net.sf.sprockets.google.Places.URL_PHOTO;
import static net.sf.sprockets.io.MoreFiles.DOT_PART;

/**
 * Created by vincent.meier on 16.11.2015.
 */
public class ClassRestoAdapter extends ArrayAdapter<Place> {

    //tweets est la liste des models à afficher
    public ClassRestoAdapter(Context context, List<Place> resto) {
        super(context, 0, resto);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_restos,parent, false);
        }

        RestoViewHolder viewHolder = (RestoViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new RestoViewHolder();
            viewHolder.pseudo = (TextView) convertView.findViewById(R.id.pseudo);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        }

        Place resto = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.pseudo.setText(resto.getName());
        viewHolder.text.setText(resto.getFormattedAddress());

        LatLng restoLatLng = new LatLng(resto.getLatitude(),resto.getLongitude());
     //   String url = url(resto, 50, 50);
    //    photo (resto.get.getPlaceId(), url);

        try
        {
            //InputStream in = new GetPlacePhotoTask().execute(restoLatLng).get();
            InputStream in = new GetPlacePhotoTask().execute(resto).get();
            viewHolder.avatar.setImageBitmap(BitmapFactory.decodeStream(in));
            Closeables.closeQuietly(in);

        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        catch(ExecutionException e)
        {
            e.printStackTrace();
        }


        return convertView;
    }

    private class RestoViewHolder{
        public TextView pseudo;
        public TextView text;
        public ImageView avatar;
    }


    private class GetPlacePhotoTask extends AsyncTask<Place,Void,InputStream>
    {
        @Override
        protected void onPreExecute() {
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
        protected InputStream doInBackground(Place... params) {
            StreetView.Response<InputStream> imagestreet = null;
            Places.Response<InputStream> imageplace = null;
            try {



                //Méthode Place
                List<Place.Photo> photos = params[0].getPhotos();
                if(!photos.isEmpty())
                {
                    Place.Photo photo = photos.get(0);
                    PlacesParams placesparams = Places.Params.create().reference(photo.getReference()).maxHeight(photo.getHeight()).maxWidth(photo.getWidth());
                    imageplace = Places.photo(placesparams);
                }
                else
                {
                    //Méthode stretview
                    imagestreet = StreetView.image(StreetView.Params.create()
                        .longitude(params[0].getLongitude())
                        .latitude(params[0].getLatitude()).height(50)
                        .width(50));
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            if(imagestreet!=null)
            {
                int status = imagestreet.getStatus();
                InputStream in = imagestreet.getResult();

                if (status == HttpURLConnection.HTTP_OK && in != null) {
                    return in;
                } else {
                    System.out.println("error, HTTP status code: " + status);
                    return null;
                }

            }
            else
            {
                InputStream in = imageplace.getResult();
                String status = imageplace.getStatus();

                if (Places.Response.STATUS_OK.equals(status) && in != null) {
                    return in;
                } else {
                    System.out.println("error: " + imageplace.getErrorMessage() );
                    return null;
                }
            }



        }
    }

}