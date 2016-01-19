package iee3.he_arc.cityresto;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.CheckBox;

import com.google.android.gms.maps.model.LatLng;

import net.sf.sprockets.google.Place;

import java.util.HashMap;
import java.util.List;

import iee3.he_arc.cityresto.InternDB.ClassInternRestaurant;

/**
 * Created by vincent.meier on 22.12.2015.
 */
public class ClassMainStorageManager {



    private static int lRadius = 500; // Radius of research in meters
    private static String[] lTypesChecked = new String[10]; // All types checked are put in a String array.
    private static String[] lTypesNames = new String[10];
    private static String[] lTypesChoosen = new String[10];
    private static String[] lTypesRealNames = {"fastfood", "pizzeria", "kebap", "italian", "turkish", "chinese", "thailand", "japanese", "spainish", "gastronomic"}; // Array containing types names
    public static String[] lListOfFavouriteID = new String[100]; // Array containing ID of favourite restaurants
    public static boolean isCbChecked = false; // Boolean to know if user want to switch to List/Map
    public static List<Place> lListOfRestaurants;
    public static List<Place> lListOfFavourites;
    public static LatLng lCoordonates;
    public static ServiceGoogleHelper gps;

    public static int lPositionTab = 0;
    public static boolean lDisplayList = false;

    // We use a Hashmap for saving choices of types of restaurants
    // The key is the type of restaurant, and the value is a boolean who said if it's checked or not.
    static  HashMap<String, Boolean> hmTypesChecked = new HashMap<String, Boolean>();

    public static void initTypesNamesArray(){

        lTypesNames[0] = "type1";
        lTypesNames[1] = "type2";
        lTypesNames[2] = "type3";
        lTypesNames[3] = "type4";
        lTypesNames[4] = "type5";
        lTypesNames[5] = "type6";
        lTypesNames[6] = "type7";
        lTypesNames[7] = "type8";
        lTypesNames[8] = "type9";
        lTypesNames[9] = "type10";

    }

    public static void initCheckBoxes(Context c) {
        // Set alls types to "uncheck", because noone are checked yet
        hmTypesChecked.put("type1", false);
        hmTypesChecked.put("type2", false);
        hmTypesChecked.put("type3", false);
        hmTypesChecked.put("type4", false);
        hmTypesChecked.put("type5", false);
        hmTypesChecked.put("type6", false);
        hmTypesChecked.put("type7", false);
        hmTypesChecked.put("type8", false);
        hmTypesChecked.put("type9", false);
        hmTypesChecked.put("type10", false);
    }


    //
    // Load favourite places
    public static List<Place> loadFavouriteRestos(Context c){
        // Create object for using DataBases
        ClassPermanentDataHelper mClassPermanentDataHelper = new ClassPermanentDataHelper(c);
        ClassInternRestaurant resto = new ClassInternRestaurant();

       return null;
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

    // Fill a String Array with only checked types, and return array with types choosen
    public static String[] fillArrayOfTypesChecked(){

        int i, j = 0; // j is used to avoid empty elements of the array
        String[] lBufferArrayOfTypesChecked;  // Temporary array for types checked

        // Fill an array with types's names
        initTypesNamesArray();

        // First clean the old array
        lBufferArrayOfTypesChecked = new String[lTypesChecked.length];

        // Clear array of types chosen
        for(i=0 ; i<lTypesChecked.length ; i++) {
            lTypesChecked[i] = "";
        }

        // Then check if type is checked, and save it if yes
        for(i=0 ; i<lTypesNames.length ; i++){
            if(getHmTypesChecked(lTypesNames[i]).equals(true)) {

                //lBufferArrayOfTypesChecked[j] = lTypesNames[i];
               // lTypesChoosen[j] = lTypesRealNames[i];
                lBufferArrayOfTypesChecked[j] = lTypesRealNames[i];
                j++;
            }
        }
        // Create a new array without empty elements
        if(0 == j){

            // No types choosen
            return null;

        }else {

            lTypesChecked = new String[j+1];
            for (i=0 ; i<lTypesChecked.length ; i++) {
                lTypesChecked[i] = lBufferArrayOfTypesChecked[i];
            }
            return lTypesChecked;
        }
    }



}

