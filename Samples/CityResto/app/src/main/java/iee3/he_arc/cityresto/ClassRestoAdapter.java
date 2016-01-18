package iee3.he_arc.cityresto;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.common.io.Closer;
import com.google.common.io.Files;
import com.squareup.okhttp.internal.DiskLruCache;

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
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;

import android.app.Activity;

import static net.sf.sprockets.google.Places.URL_PHOTO;
import static net.sf.sprockets.io.MoreFiles.DOT_PART;

/**
 * Created by vincent.meier on 16.11.2015.
 */
public class ClassRestoAdapter extends ArrayAdapter<Place> {

    // Memory cache
    private LruCache<String, Bitmap> mMemoryCache;

    private final Activity context;
    private final List<Place> list;
    private Bitmap bitmap;

    public ClassRestoAdapter(Activity context, List<Place> list) {
        super(context, R.layout.row_restos, list);
        this.context = context;
        this.list = list;

    }

    private static class ViewHolder
    {
        protected TextView mText;
        protected ImageView mImage;
        protected int mPosition;
    }

    private class HolderPlace
    {
       protected ViewHolder mViewHolder;
       protected Place      mPlace;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = null;

        if (convertView == null)
        {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.row_restos, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.mText = (TextView) view.findViewById(R.id.pseudo);
            viewHolder.mText.setTextColor(Color.BLACK);
            viewHolder.mImage = (ImageView) view.findViewById(R.id.avatar);

            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
        }


        Place resto = getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.mText.setText(list.get(position).getName());
        holder.mPosition = position;


        new GetPlacePhotoTask(position,holder,resto).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void)null);
        return view;

    }



    private class GetPlacePhotoTask extends AsyncTask <Void,Void,Bitmap>
    {
        private ViewHolder mViewHolder;
        private Place      mPlace;
        private int        mPosition;


        public GetPlacePhotoTask (int _Position,ViewHolder _Holder,Place _Place)
        {
           mPlace = _Place;
           mViewHolder = _Holder;
           mPosition = _Position;
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
                        PlacesParams placesparams = Places.Params.create().reference(photo.getReference()).maxHeight(200).maxWidth(200);
                        imageplace = Places.photo(placesparams);
                    }
                }
                else
                {
                    if(!ActMainResto.mDiskLruImageCache.containsKey(mPlace.getPlaceId().getId()))
                    {
                        //Méthode stretview
                        imagestreet = StreetView.image(StreetView.Params.create()
                                .longitude(mPlace.getLongitude())
                                .latitude(mPlace.getLatitude()).height(200)
                                .width(200));
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
                if(mViewHolder.mPosition == this.mPosition)
                {
                    //Check if image is already in cache
                    if(ActMainResto.mDiskLruImageCache.containsKey(mPlace.getPlaceId().getId()))
                    {
                        mViewHolder.mImage.setImageBitmap(getResizedBitmap(result,80,80));
                    }
                    else
                    {
                        ActMainResto.mDiskLruImageCache.put(mPlace.getPlaceId().getId(), result); // Save image in disk cache
                        mViewHolder.mImage.setImageBitmap(getResizedBitmap(result,80,80));
                    }

                }
            }
            //Check if image is already in cache
            if(ActMainResto.mDiskLruImageCache.containsKey(mPlace.getPlaceId().getId()))
            {
                mViewHolder.mImage.setImageBitmap(getResizedBitmap(ActMainResto.mDiskLruImageCache.getBitmap(mPlace.getPlaceId().getId()),80,80));
            }

        }
    }



    // decodes image and scales it to reduce memory consumption
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

}