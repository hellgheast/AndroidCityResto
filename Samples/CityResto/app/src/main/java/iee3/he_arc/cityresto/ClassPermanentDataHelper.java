package iee3.he_arc.cityresto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import iee3.he_arc.cityresto.InternDB.ClassInternUser;

/**
 * Created by mohammed.bensalah on 24.12.2015.
 */

/*=====================================================================*
 | This file declares the following classes:
 |    ClassDBHelper
 |
 |
 | Description of the class ClassDBHelper :
 | Classe qui permet la communication avec la base de donnée interne et entre l'application
 |
 |
 | <p>Copyright : HE-ARC, all rights reserved</p>
 | @autor : Mohammed-Ismail Ben Salah & Vincent Meier
 | @version : 1.1
 |
 | Mofification tag : Modification 08012015 : Mise en place d'une certaine généricité
 |
 *=====================================================================*/


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

    //Table Photo
    private static final String PHOTO_PLACEID_TYPE = " VARCHAR(30)";
    private static final String PHOTO_REFERENCE_TYPE = " TEXT";
    private static final String PHOTO_URI_TYPE = " TEXT";

    private static final String TABLE_PHOTO_SQL_CREATE_ENTRIES=
            "CREATE TABLE "+ClassPermanentData.InternSetting.TABLE_NAME + " ("
                    + ClassPermanentData.PhotoRestaurants._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID + PHOTO_PLACEID_TYPE + " NOT NULL"
                    + COMMA_SEP+ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_REFERENCE + PHOTO_REFERENCE_TYPE+" NOT NULL"
                    + COMMA_SEP+ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_URI + PHOTO_URI_TYPE+" NOT NULL"
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
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLE_USER_SQL_CREATE_ENTRIES);;
        db.execSQL(TABLE_INTERN_SQL_CREATE_ENTRIES);
        db.execSQL(TABLE_RESTAURANT_SQL_CREATE_ENTRIES);
        db.execSQL(TABLE_PHOTO_SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ClassPermanentData.PhotoRestaurants.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ClassPermanentData.InternSetting.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ClassPermanentData.FavoriteRestaurants.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ClassPermanentData.UserEntry.TABLE_NAME);
        onCreate(db);

    }

    /**
     * Operations CRUD sur chacune des tables
     */

    /**
     * Table Users
     */

    public void insertUser(ClassInternUser _User)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME,_User.getUsername());
        values.put(ClassPermanentData.UserEntry.COLUMN_NAME_PASSWORD,_User.getPassword());

        //Insertion
        db.insert(ClassPermanentData.UserEntry.TABLE_NAME,null,values);
        db.close();
    }
    public ClassInternUser readUser(String _UserName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ClassPermanentData.UserEntry.TABLE_NAME,
                                new String[]{ClassPermanentData.UserEntry._ID,
                                            ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME,
                                            ClassPermanentData.UserEntry.COLUMN_NAME_PASSWORD},
                                     ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME + "= "+_UserName,
                                    null,
                                    null,
                                    null,
                                    null
                                );
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        return new ClassInternUser(cursor.getString(1),cursor.getString(2));
    }

    public int updateUser(ClassInternUser _User )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME,_User.getUsername());
        values.put(ClassPermanentData.UserEntry.COLUMN_NAME_PASSWORD,_User.getPassword());

        //Update of the row
        return db.update(ClassPermanentData.UserEntry.TABLE_NAME, values, ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME + " = " + _User.getUsername() ,null);
    }

    public void deleteUser(ClassInternUser _User)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete the row
        db.delete(ClassPermanentData.UserEntry.TABLE_NAME,ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME + " = " + _User.getUsername(),null);
        db.close();
    }


}
