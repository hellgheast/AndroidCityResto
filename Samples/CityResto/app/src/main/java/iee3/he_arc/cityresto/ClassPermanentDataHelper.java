package iee3.he_arc.cityresto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import iee3.he_arc.cityresto.InternDB.ClassInternPhotoResto;
import iee3.he_arc.cityresto.InternDB.ClassInternRestaurant;
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
                      Modification 11012015 : Mise en place d'une méthode de vérification des tables et fabrication des requêtes correctes.
 |
 *=====================================================================*/


public class ClassPermanentDataHelper extends SQLiteOpenHelper
{

    private static final String TAG = "DataHelper";
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
                    + COMMA_SEP+"UNIQUE("+ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME +")"
                    + "ON CONFLICT REPLACE"
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
                    + COMMA_SEP+"UNIQUE("+ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_PLACEID +")"
                    + "ON CONFLICT REPLACE"
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
            "CREATE TABLE "+ClassPermanentData.PhotoRestaurants.TABLE_NAME + " ("
                    + ClassPermanentData.PhotoRestaurants._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID + PHOTO_PLACEID_TYPE + " NOT NULL"
                    + COMMA_SEP+ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_REFERENCE + PHOTO_REFERENCE_TYPE+" NOT NULL"
                    + COMMA_SEP+ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_URI + PHOTO_URI_TYPE+" NOT NULL"
                    + COMMA_SEP+"UNIQUE("+ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID +")"
                    + " )";


    //Main Settings
    public static final int     DATABASE_VERSION = 2;
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

    public boolean isTableExists(String tableName, boolean openDb,SQLiteDatabase mDatabase) {
        if(openDb) {
            if(mDatabase == null || !mDatabase.isOpen()) {
                mDatabase = this.getReadableDatabase();
            }

            if(!mDatabase.isReadOnly()) {
                mDatabase.close();
                mDatabase = this.getReadableDatabase();
            }
        }

        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        File temp = new File(db.getPath());
        //Véfification si les tables existent

        if(temp.exists()==false)
        {
            db.execSQL(TABLE_USER_SQL_CREATE_ENTRIES);
            db.execSQL(TABLE_INTERN_SQL_CREATE_ENTRIES);
            db.execSQL(TABLE_RESTAURANT_SQL_CREATE_ENTRIES);
            db.execSQL(TABLE_PHOTO_SQL_CREATE_ENTRIES);
        }
        else
        {
            if(!isTableExists(ClassPermanentData.UserEntry.TABLE_NAME,false,db))
            {
                db.execSQL(TABLE_USER_SQL_CREATE_ENTRIES);
                Log.d(TAG, "onCreate " + "USER");
            }
            if(!isTableExists(ClassPermanentData.InternSetting.TABLE_NAME,false,db))
            {
                db.execSQL(TABLE_INTERN_SQL_CREATE_ENTRIES);
                Log.d(TAG, "onCreate " + "INTERN");
            }
            if(!isTableExists(ClassPermanentData.FavoriteRestaurants.TABLE_NAME,false,db))
            {
                db.execSQL(TABLE_RESTAURANT_SQL_CREATE_ENTRIES);
                Log.d(TAG, "onCreate " + "RESTAURANT");
            }
            if(!isTableExists(ClassPermanentData.PhotoRestaurants.TABLE_NAME,false,db))
            {
                db.execSQL(TABLE_PHOTO_SQL_CREATE_ENTRIES);
                Log.d(TAG, "onCreate " + "PHOTO");
            }

        }


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
        values.put(ClassPermanentData.UserEntry.COLUMN_NAME_PASSWORD, _User.getPassword());

        //Insertion
        db.insert(ClassPermanentData.UserEntry.TABLE_NAME,null,values);
        Log.i("DataHelper", "insertUser " + _User.getPassword() + _User.getUsername());
        db.close();
    }
    public ClassInternUser readUser(String _UserName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ClassPermanentData.UserEntry.TABLE_NAME,
                new String[]{ClassPermanentData.UserEntry._ID,
                        ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME,
                        ClassPermanentData.UserEntry.COLUMN_NAME_PASSWORD},
                ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME + "=?",
                new String[]{_UserName},
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
        values.put(ClassPermanentData.UserEntry.COLUMN_NAME_PASSWORD, _User.getPassword());

        //Update of the row
        return db.update(ClassPermanentData.UserEntry.TABLE_NAME, values, ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME + " = " + _User.getUsername() ,null);
    }

    public void deleteUser(ClassInternUser _User)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete the row
        db.delete(ClassPermanentData.UserEntry.TABLE_NAME, ClassPermanentData.UserEntry.COLUMN_NAME_USERNAME + " = " + _User.getUsername(), null);
        db.close();
    }

    /**
     * Table Restaurants photo
     */
    public void addPhoto(ClassInternPhotoResto _Photo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID,_Photo.getPlaceID());
        values.put(ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_REFERENCE,_Photo.getPhotoReference());
        values.put(ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_REFERENCE, _Photo.getPhotoUri());


        //Insertion
        db.insert(ClassPermanentData.PhotoRestaurants.TABLE_NAME, null, values);
        db.close();

    }
    public ClassInternPhotoResto readPhoto( ClassInternPhotoResto _Photo)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ClassPermanentData.UserEntry.TABLE_NAME,
                new String[]{ClassPermanentData.PhotoRestaurants._ID,
                        ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID,
                        ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_REFERENCE,
                ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_URI},
                ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID + "=?",
                new String[]{_Photo.getPlaceID()},
                null,
                null,
                null
        );
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        return new ClassInternPhotoResto(cursor.getString(1), cursor.getString(2),cursor.getString(3));
    }

    public ClassInternPhotoResto readPhotoReference (String _PhotoReference)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ClassPermanentData.UserEntry.TABLE_NAME,
                new String[]{ClassPermanentData.PhotoRestaurants._ID,
                        ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID,
                        ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_REFERENCE,
                        ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_URI},
                ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_REFERENCE + "=?",
                new String[]{_PhotoReference},
                null,
                null,
                null
        );
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        return new ClassInternPhotoResto(cursor.getString(1), cursor.getString(2),cursor.getString(3));
    }

    public ClassInternPhotoResto readPhotoPlaceID (String _PlaceID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ClassPermanentData.UserEntry.TABLE_NAME,
                new String[]{ClassPermanentData.PhotoRestaurants._ID,
                        ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID,
                        ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_REFERENCE,
                        ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_URI},
                ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID + "=?",
                new String[]{_PlaceID},
                null,
                null,
                null
        );
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        return new ClassInternPhotoResto(cursor.getString(1), cursor.getString(2),cursor.getString(3));
    }

    public int updatePhoto(ClassInternPhotoResto _Photo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID,_Photo.getPlaceID());
        values.put(ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_REFERENCE,_Photo.getPhotoReference());
        values.put(ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_REFERENCE, _Photo.getPhotoUri());


        //Update of the row
        return db.update(ClassPermanentData.PhotoRestaurants.TABLE_NAME, values, ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID + " = " + _Photo.getPlaceID(), null);

    }

    public void deletePhoto(ClassInternPhotoResto _Photo)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete the row
        db.delete(ClassPermanentData.PhotoRestaurants.TABLE_NAME,ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID + " = " + _Photo.getPlaceID(),null);
        db.close();
    }

    public void deletePhotoReference(String _Reference)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete the row
        db.delete(ClassPermanentData.PhotoRestaurants.TABLE_NAME,ClassPermanentData.PhotoRestaurants.COLUMN_PHOTO_REFERENCE + " = " + _Reference,null);
        db.close();
    }
    public void deletePhotoPlaceID(String _PlaceID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete the row
        db.delete(ClassPermanentData.PhotoRestaurants.TABLE_NAME, ClassPermanentData.PhotoRestaurants.COLUMN_NAME_PLACEID + " = " + _PlaceID, null);
        db.close();
    }

    /***
     * CRUD Operation for the save of the restaurants
     */

    public void addRestaurant( ClassInternRestaurant _Restaurant)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_PLACEID,_Restaurant.getPlaceID());
        values.put(ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_ADDRESS,_Restaurant.getAdress());
        values.put(ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_RESTO_NAME,_Restaurant.getName());

        //Insertion
        db.insert(ClassPermanentData.FavoriteRestaurants.TABLE_NAME, null, values);
        db.close();

    }

    public ClassInternRestaurant readRestaurantPlaceID (String _PlaceID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ClassPermanentData.FavoriteRestaurants.TABLE_NAME,
                new String[]{ClassPermanentData.FavoriteRestaurants._ID,
                        ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_PLACEID,
                        ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_RESTO_NAME,
                        ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_ADDRESS},
                ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_PLACEID + "=?",
                new String[]{_PlaceID},
                null,
                null,
                null
        );
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }

        return new ClassInternRestaurant(cursor.getString(1), cursor.getString(2),cursor.getString(3));
    }

    public ClassInternRestaurant readRestaurantName(String _Name)
    {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(ClassPermanentData.FavoriteRestaurants.TABLE_NAME,
                    new String[]{ClassPermanentData.FavoriteRestaurants._ID,
                            ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_PLACEID,
                            ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_RESTO_NAME,
                            ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_ADDRESS},
                    ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_RESTO_NAME+ "=?",
                    new String[]{_Name},
                    null,
                    null,
                    null
            );
            if(cursor!=null)
            {
                cursor.moveToFirst();
            }

            return new ClassInternRestaurant(cursor.getString(1), cursor.getString(2),cursor.getString(3));

    }

    public ClassInternRestaurant readRestaurantAdress(String _Adress)
    {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ClassPermanentData.FavoriteRestaurants.TABLE_NAME,
                new String[]{ClassPermanentData.FavoriteRestaurants._ID,
                        ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_PLACEID,
                        ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_RESTO_NAME,
                        ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_ADDRESS},
                ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_ADDRESS+ "=?",
                new String[]{_Adress},
                null,
                null,
                null
        );
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }

        return new ClassInternRestaurant(cursor.getString(1), cursor.getString(2),cursor.getString(3));

    }

    public int updateRestaurant(ClassInternRestaurant _Restaurant)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_PLACEID,_Restaurant.getPlaceID());
        values.put(ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_ADDRESS,_Restaurant.getAdress());
        values.put(ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_RESTO_NAME,_Restaurant.getName());


        //Update of the row
        return db.update(ClassPermanentData.FavoriteRestaurants.TABLE_NAME, values, ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_PLACEID + " = " + _Restaurant.getPlaceID(), null);

    }

    public void deleteRestaurant(ClassInternRestaurant _Restaurant)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete the row
        db.delete(ClassPermanentData.FavoriteRestaurants.TABLE_NAME,ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_PLACEID + " = " + _Restaurant.getPlaceID(),null);
        db.close();
    }

    public void deleteRestaurantPlaceID(String _PlaceID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete the row
        db.delete(ClassPermanentData.FavoriteRestaurants.TABLE_NAME,ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_PLACEID + " = " + _PlaceID,null);
        db.close();
    }

    public void deleteRestaurantName(String _Name)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete the row
        db.delete(ClassPermanentData.FavoriteRestaurants.TABLE_NAME, ClassPermanentData.FavoriteRestaurants.COLUMN_NAME_RESTO_NAME + " = " + _Name, null);
        db.close();
    }


}
