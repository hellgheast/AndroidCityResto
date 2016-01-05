package iee3.he_arc.cityresto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mohammed.bensalah on 24.12.2015.
 */
public class ClassPermanentDataHelper extends SQLiteOpenHelper
{
    //SQL Variables
    private static final String COMMA_SEP = ",";

    //Table User
    private static final String USERNAME_TYPE = " VARCHAR(20)";
    private static final String PASSWORD_TYPE = " VARCHAR(45)";

    private static final String TABLE_USER_SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ ClassPermanentData.UserEntry.TABLE_NAME + " ("
                    + ClassPermanentData.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME+ USERNAME_TYPE+ " NOT NULL"
                    + COMMA_SEP+ClassPermanentData.UserEntry.COLUMN_NAME_PASSWORD+PASSWORD_TYPE + " NOT NULL"
                    + " )";

    //Table Restaurants
    private static final String PLACEID_TYPE = " VARCHAR(30)";
    private static final String RESTO_NAME_TYPE = " TEXT";
    private static final String RESTO_ADDRESS = " TEXT";

    private static final String TABLE_RESTAURANT_SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ClassPermanentData.FavoriteRestaurants.TABLE_NAME + " ("
                    + ClassPermanentData.FavoriteRestaurants._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_PLACEID + PLACEID_TYPE + " NOT NULL"
                    + COMMA_SEP+ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_RESTO_NAME + RESTO_NAME_TYPE+ " NOT NULL"
                    + COMMA_SEP+ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_ADDRESS + RESTO_ADDRESS+" NOT NULL"
                    + " )";

    //Table InternSetting
    private static final String LANG_TYPE = " VARCHAR(3)";

    private static final String TABLE_INTERN_SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ClassPermanentData.InternSetting.TABLE_NAME + " ("
                    + ClassPermanentData.InternSetting._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ClassPermanentData.InternSetting.COLUMN_NAME_USERNAME + USERNAME_TYPE + " NOT NULL"
                    + COMMA_SEP+ClassPermanentData.InternSetting.COLUMN_NAME_LANGUAGE + LANG_TYPE +" NOT NULL"
                    + " )";


    //Main Settings
    public static final int     DATABASE_VERSION = 1;
    public static final String  DATABASE_NAME="PermanentData.db";

    //TODO Tout ce qu'il faut pour manipuler ça !

    /**
     *
     * Constructeur Simple
     */
    public ClassPermanentDataHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
