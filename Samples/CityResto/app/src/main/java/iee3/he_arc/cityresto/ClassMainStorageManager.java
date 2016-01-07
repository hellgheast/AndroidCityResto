package iee3.he_arc.cityresto;

import android.content.Context;
import android.widget.CheckBox;

import com.google.android.gms.maps.model.LatLng;

import net.sf.sprockets.google.Place;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vincent.meier on 22.12.2015.
 */
public class ClassMainStorageManager {

    private static int lRadius = 500; // Radius of research in meters
    private static String[] lTypesChecked = new String[10]; // All types checked are put in a String array.
    private static String[] lTypesNames = new String[10]; // Array containing types names
    public static boolean isCbChecked = false; // Boolean to know if user want to switch to List/Map
    public static List<Place> lListOfRestaurants;
    public static LatLng lCoordonates;
    public static ServiceGoogleHelper gps;

    // We use a Hashmap for saving choices of types of restaurants
    // The key is the type of restaurant, and the value is a boolean who said if it's checked or not.
    static  HashMap<String, Boolean> hmTypesChecked = new HashMap<String, Boolean>();

    public static void initTypesNamesArray(){

        lTypesNames[0] = ((Integer)R.string.type1).toString();
        lTypesNames[1] = ((Integer)R.string.type2).toString();
        lTypesNames[2] = ((Integer)R.string.type3).toString();
        lTypesNames[3] = ((Integer)R.string.type4).toString();
        lTypesNames[4] = ((Integer)R.string.type5).toString();
        lTypesNames[5] = ((Integer)R.string.type6).toString();
        lTypesNames[6] = ((Integer)R.string.type7).toString();
        lTypesNames[7] = ((Integer)R.string.type8).toString();
        lTypesNames[8] = ((Integer)R.string.type9).toString();
        lTypesNames[9] = ((Integer)R.string.type10).toString();

    }

    public static void initCheckBoxes(Context c) {
        // Set alls types to "uncheck", because noone are checked yet
        hmTypesChecked.put(c.getString(R.string.type1), false);
        hmTypesChecked.put(c.getString(R.string.type2), false);
        hmTypesChecked.put(c.getString(R.string.type3), false);
        hmTypesChecked.put(c.getString(R.string.type4), false);
        hmTypesChecked.put(c.getString(R.string.type5), false);
        hmTypesChecked.put(c.getString(R.string.type6), false);
        hmTypesChecked.put(c.getString(R.string.type7), false);
        hmTypesChecked.put(c.getString(R.string.type8), false);
        hmTypesChecked.put(c.getString(R.string.type9), false);
        hmTypesChecked.put(c.getString(R.string.type10), false);
    }

    // Set value of a specific key
    public static void setHmTypesChecked(String key, Boolean isChecked, Context c) {
        // To change a value, we update it with "put"
       hmTypesChecked.put(key, isChecked);
    }

    // Get value of a specific key
    public static Boolean getHmTypesChecked(String key) {
        return hmTypesChecked.get(key);
    }

    // Set radius
    public static void setRadius(int rad, Context c) {
        lRadius = rad;
    }

    // Get radius
    public static int getRadius(Context c){
        return lRadius;
    }

    // Get array of types checked{
    public static String[] getArrayOfTypesChecked(Context c){
        return lTypesChecked;
    }

    // Fill a String Array with only checked types
    public static void fillArrayOfTypesChecked(){

        int i, j = 0; // j is used to avoid empty elements of the array
        String[] lBufferArrayOfTypesChecked = new String[10];  // Temporary array for types checked

        // Fill an array with types's names
        initTypesNamesArray();

        // First clean the old array
        lBufferArrayOfTypesChecked = new String[lTypesChecked.length];

        // Then check if type is checked, and save it if yes
        for(i=0 ; i<lTypesNames.length ; i++){
            if(getHmTypesChecked(lTypesNames[i]).equals(true)) {
                lBufferArrayOfTypesChecked[j] = lTypesNames[j];
                j++;
            }
        }
        // Create a new array without empty elements
        lTypesChecked= new String[j+1];
        for(i=0 ; i<lTypesChecked.length ; i++){
            lTypesChecked[i] = lBufferArrayOfTypesChecked[i];
        }
    }

}

