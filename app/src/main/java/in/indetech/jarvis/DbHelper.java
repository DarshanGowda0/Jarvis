package in.indetech.jarvis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by darshan on 16/04/16.
 */
public class DbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_NO = 1;
    public static final String DATABASE_NAME = "MyDb.db";
    public static final String TAG = "DAMM";
    public static final String USERS_TABLE = "users";
    public static final String MESSAGES_TABLE = "messages";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String MESSAGE = "message";
    public static final String MESSAGE_TIME = "message_time";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_NO);
        Log.d(TAG, "inside db helper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUsersTableQuery = "create table " + USERS_TABLE + " (" + USER_ID + " integer primary key autoincrement," + USER_NAME + " string)";
        db.execSQL(createUsersTableQuery);

        String createMessagesTable = "create table " + MESSAGES_TABLE + " (" + USER_NAME + " string," + MESSAGE + " string," + MESSAGE_TIME + " TIMESTAMP)";
        db.execSQL(createMessagesTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE);
        onCreate(db);
    }

    public boolean insertUsers(String user_name) {

        String insertQuery = "insert into " + USERS_TABLE + " (" + USER_NAME + ") values ('" + user_name + "')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insertQuery);
        Log.d(TAG, "user " + user_name + " inserted successfully");


        return true;
    }

    public boolean insertMessages(String username, String time_stamp) {

        String insertMessage = "insert into " + MESSAGES_TABLE + " (" + USER_NAME + "," + MESSAGE_TIME + ") values ('" + username + "','" + time_stamp + "')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insertMessage);
        Log.d(TAG, "user " + username + "with time " + time_stamp + " inserted successfully");

        return true;
    }

    public int getAllMessagesCount(String username, String from_time, String to_time) {
        ArrayList<String> array_list = new ArrayList<>();


        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MESSAGES_TABLE
                //+
                //" where " + USER_NAME + " = '" + username + "' "
                //"and " + MESSAGE_TIME + " >= '" + from_time + "' and " + MESSAGE_TIME + " <= '" + to_time + "'"
                , null);
        Log.d(TAG, "in cursor");

        res.moveToFirst();
        Log.d(TAG, res.isAfterLast() + "");

        while (!res.isAfterLast()) {

            String time = res.getString(res.getColumnIndex(MESSAGE_TIME));

            array_list.add(time);

            res.moveToNext();
        }

        res.close();

        return array_list.size();
    }

    public ArrayList<String> getAllUsers() {

        ArrayList<String> users = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + USERS_TABLE, null);

        res.moveToFirst();
        while (!res.isAfterLast()) {

            String username = res.getString(res.getColumnIndex(USER_NAME));
            users.add(username);

            res.moveToNext();
        }
        res.close();
        return users;
    }



}
