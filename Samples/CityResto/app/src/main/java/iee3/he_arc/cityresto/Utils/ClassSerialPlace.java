package iee3.he_arc.cityresto.Utils;

import net.sf.sprockets.google.Place;

import java.io.Serializable;

/**
 * Created by mohammed.bensalah on 13.01.2016.
 */
public class ClassSerialPlace implements Serializable
{
    Place mPlace;

    public ClassSerialPlace(Place mPlace)
    {
        this.mPlace = mPlace;
    }

    public Place getmPlace() {
        return mPlace;
    }

    public void setmPlace(Place mPlace) {
        this.mPlace = mPlace;
    }
}
