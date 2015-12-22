package iee3.he_arc.cityresto;

import android.content.Context;
import android.widget.CheckBox;

import java.util.HashMap;
/**
 * Created by vincent.meier on 22.12.2015.
 */
public class ClassMainStorageManager {

    public int lRadius = 1000; // Radius of research in km
    public int lCondition = 0; // Condition to choose restaurants : 0 = AND, 1 = OR

    // We use a Hashmap for saving choices of types of restaurants
    // The key is the type of restaurant, and the value is a boolean who said if it's checked or not.
    public HashMap<String, Integer> hmTypesChecked = new HashMap<String, Integer>();

    // Set alls types to 0, because noone are checked
    public HashMap<String, Integer> initTypes(Context c) {
        hmTypesChecked.put(c.getString(R.string.type1), 0);
        hmTypesChecked.put(c.getString(R.string.type2), 0);
        hmTypesChecked.put(c.getString(R.string.type3), 0);
        hmTypesChecked.put(c.getString(R.string.type4), 0);
        hmTypesChecked.put(c.getString(R.string.type5), 0);
        hmTypesChecked.put(c.getString(R.string.type6), 0);
        hmTypesChecked.put(c.getString(R.string.type7), 0);
        hmTypesChecked.put(c.getString(R.string.type8), 0);
        hmTypesChecked.put(c.getString(R.string.type9), 0);
        hmTypesChecked.put(c.getString(R.string.type10), 0);
        return hmTypesChecked;
    }



    public HashMap<String, Integer> getHmTypesChecked() {
        return hmTypesChecked;
    }

    public int getlRadius() {
        return lRadius;
    }

    public void setRadius(int rad){
        lRadius = rad;
    }
}

