package iee3.he_arc.cityresto;

import android.provider.BaseColumns;

/**
 * Created by mohammed.bensalah on 24.12.2015.
 */

/*=====================================================================*
 | This file declares the following classes:
 |    ClassPermanentData
 |
 |
 | Description of the class ClassDBHelper :
 | Classe qui va s'occuper de la sauvegarde des données et de l'envoi aux instances utiles
 |
 | !!
 |
 | <p>Copyright : HE-ARC, all rights reserved</p>
 | @autor : Mohammed-Ismail Ben Salah
 | @version : 1.0
 |
 | Mofification tag : //MODIF1
 |
 *=====================================================================*/

public class ClassPermanentData
{

    /**
     * Constructeur Simple qui ne doit pas être instancé
     *
     */
    public ClassPermanentData(){};

    /**
     * Attributs qui permet d'avoir le nom
     */
    public static final String Name = "PermanentData";

    /**
     * Inner class that defines the table contents
     */
    public static abstract class UserEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "TLocalUser";
        public static final String COLUMN_NAME_USERNAME = "User";
        public static final String COLUMN_NAME_PASSWORD = "Password";

    }

    /**
     * Inner class that defines the table contents
     */
    public static abstract class InternSetting implements BaseColumns
    {
        public static final String TABLE_NAME = "TLocalInternSetting";
        public static final String COLUMN_NAME_USERNAME = "User";
        public static final String COLUMN_NAME_LANGUAGE = "Language";

    }
    /**
     * Inner class that defines the table contents
     */
    public static abstract class FavoriteRestaurants implements BaseColumns
    {
        public static final String TABLE_NAME = "TLocalFavoriteRestaurants";
        public static final String COLUMN_NAME_PLACEID =  "PlaceID";
        public static final String COLUMN_NAME_RESTO_NAME =  "Name";
        public static final String COLUMN_NAME_ADDRESS =  "Address";

        //TODO il faut mettre les autres colonnes du restaurant
    }

    /**
     * Inner class that defines the table contents
     */
    public static abstract class PhotoRestaurants implements BaseColumns
    {
        public static final String TABLE_NAME = "TLocalPhotoRestaurants";
        public static final String COLUMN_NAME_PLACEID      =  "PlaceID";
        public static final String COLUMN_PHOTO_REFERENCE    =  "Reference";
        public static final String COLUMN_PHOTO_URI         =  "URI";

    }


}
