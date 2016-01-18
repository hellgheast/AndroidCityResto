package iee3.he_arc.cityresto;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.io.Closeables;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;
import net.sf.sprockets.google.PlacesParams;
import net.sf.sprockets.google.StreetView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import iee3.he_arc.cityresto.ActMainResto;
import iee3.he_arc.cityresto.InternDB.ClassInternRestaurant;
import iee3.he_arc.cityresto.R;

/**
 * Created by vincent.meier on 14.01.2016.
 */
public class ClassRestoFavouriteAdapter extends ArrayAdapter<ClassInternRestaurant> {

    private final Activity context;
    private final ArrayList<ClassInternRestaurant> list;
    private Bitmap bitmap;

    public ClassRestoFavouriteAdapter(Activity context, ArrayList<ClassInternRestaurant> list) {
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


        ClassInternRestaurant resto = getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.mText.setText(list.get(position).getName());
        holder.mPosition = position;

        new GetFavouritePlacePhotoTask(position,holder,resto).execute();
        return view;

    }



    private class GetFavouritePlacePhotoTask extends AsyncTask <Void,Void,Bitmap>
    {
        private ViewHolder                 mViewHolder;
        private ClassInternRestaurant      mResto;
        private int                        mPosition;


        public GetFavouritePlacePhotoTask (int _Position, ViewHolder _Holder,ClassInternRestaurant _Resto)
        {
            mResto = _Resto;
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
            Bitmap bitmap;

            if(mViewHolder.mPosition == this.mPosition)
            {
                //Check if image is already in cache
                if(ActMainResto.mDiskLruImageCache.containsKey(mResto.getPlaceID()))
                {
                    bitmap = ActMainResto.mDiskLruImageCache.getBitmap(mResto.getPlaceID());
                    bitmap = (getResizedBitmap(bitmap, 80, 80));
                    return bitmap;

                }

            }
            return null;
        }

        @Override
        protected  void onPostExecute(Bitmap result)
        {
            if (result!=null)
            {
                mViewHolder.mImage.setImageBitmap(result);
            }
            //Check if image is already in cache
            if(ActMainResto.mDiskLruImageCache.containsKey(mResto.getPlaceID()))
            {
                mViewHolder.mImage.setImageBitmap(getResizedBitmap(ActMainResto.mDiskLruImageCache
                        .getBitmap(mResto.getPlaceID()),80,80));
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