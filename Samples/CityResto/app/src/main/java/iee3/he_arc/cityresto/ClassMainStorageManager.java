package iee3.he_arc.cityresto;

import android.content.Context;
import android.widget.CheckBox;

import java.util.HashMap;
/**
 * Created by vincent.meier on 22.12.2015.
 */
public class ClassMainStorageManager {

    public static int lRadius = 500; // Radius of research in meters
    public static boolean isCbChecked = false; // Boolean to know if user want to switch to List/Map

    // We use a Hashmap for saving choices of types of restaurants
    // The key is the type of restaurant, and the value is a boolean who said if it's checked or not.
    static  HashMap<String, Boolean> hmTypesChecked = new HashMap<String, Boolean>();

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
    public static Boolean getHmTypesChecked(String key, Context c) {
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

}

