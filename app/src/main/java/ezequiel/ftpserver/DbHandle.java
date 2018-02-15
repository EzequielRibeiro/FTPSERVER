package ezequiel.ftpserver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import static eu.chainfire.libsuperuser.Debug.TAG;

/**
 * Created by Ezequiel on 05/02/2018.
 */

public class DbHandle extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "users_ftp";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";

    private static final String TABLE_LOG = "log";

    // Post Table Columns
    public static final String KEY_ID = "id";
    public static final String KEY_USER = "user";
    public static final String KEY_PASS = "pass";
    public static final String KEY_PATH = "path";
    public static final String KEY_PERMISSIONS = "perm";

    public static final String KEY_DATA = "data";
    public static final String KEY_REGISTRO = "register";



    public DbHandle(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_POSTS_TABLE = "CREATE TABLE if not exists " + TABLE_USERS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_USER + " TEXT," + // Define a foreign key
                KEY_PASS + " TEXT," +
                KEY_PATH + " TEXT, " +
                KEY_PERMISSIONS + " TEXT, " +
                "UNIQUE("+KEY_USER+"))";

        db.execSQL(CREATE_POSTS_TABLE);


        String CREATE_LOG_TABLE = "CREATE TABLE if not exists " + TABLE_LOG +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_DATA + " TEXT," + // Define a foreign key
                KEY_REGISTRO + " TEXT,"+
                "UNIQUE("+KEY_ID+","+KEY_DATA+"))";

        db.execSQL(CREATE_LOG_TABLE);

    }

    public void onInsertLog(String data,String register){

        SQLiteDatabase db = getWritableDatabase();
       /*
        long logId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_DATA,data);
            values.put(KEY_REGISTRO, register);

            logId = db.insertOrThrow(TABLE_LOG, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d("Insert Log",e.getMessage());

        } finally {
            db.endTransaction();
        }
        */
        long dataLog = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_DATA,data);
            values.put(KEY_REGISTRO, register);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_LOG, values, KEY_DATA + "= ?", new String[]{data});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_ID, TABLE_LOG, KEY_DATA);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(data)});
                try {
                    if (cursor.moveToFirst()) {
                        dataLog = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                dataLog = db.insertOrThrow(TABLE_LOG, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }

    }


    /*
    public int getMaxRegister(){

        String QUERY =
                String.format("SELECT MAX("+KEY_ID+") FROM %s",TABLE_LOG);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        return cursor.getCount();

    }
    */

    public Cursor getLastRegister(){


        String QUERY =
                String.format("SELECT * FROM %s WHERE "+KEY_ID+" = (SELECT MAX("+KEY_ID+") FROM %s)",
                        TABLE_LOG,TABLE_LOG);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        return cursor;


    }

    public Cursor getAllRegister() {

        // List<PrefsBean> users = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_LOG);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        return cursor;
    }


    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public long onUpgradeOrADD(UserDataBase userDataBase){

        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER, userDataBase.getUserName());
            values.put(KEY_PASS, userDataBase.getPassword());
            values.put(KEY_PATH, userDataBase.getPath());
            values.put(KEY_PERMISSIONS, userDataBase.getPermission());

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_USERS, values, KEY_USER + "= ?", new String[]{userDataBase.getUserName()});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER, TABLE_USERS, KEY_USER);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(userDataBase.getUserName())});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_USERS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return userId;
    }


    public Cursor getAllUsers() {

       // List<PrefsBean> users = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_USERS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);

        /*
        try {
            if (cursor.moveToFirst()) {
                do {
                    PrefsBean prefsBean = new PrefsBean(
                            cursor.getString(cursor.getColumnIndex(KEY_USER)),
                            cursor.getString(cursor.getColumnIndex(KEY_PASS)),
                            cursor.getString(cursor.getColumnIndex(KEY_PATH)),
                            cursor.getString(cursor.getColumnIndex(KEY_PERMISSIONS)));

                   users.add(prefsBean);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } */
        return cursor;
    }

    public Integer deleteUser (PrefsBean prefsBean) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS,
                "user = ? ",
                new String[] { prefsBean.getUserName()});
    }


    }

