package kz.yergalizhakhan.fingerprint.SQLite.ORM;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kz.yergalizhakhan.fingerprint.Entity.Account;
import kz.yergalizhakhan.fingerprint.SQLite.DatabaseWrapper;

/**
 * Created by zhakhanyergali on 21.11.17.
 */

public class AccountORM implements InterfaceORM<Account> {

    private static final String TAG = AccountORM.class.getCanonicalName();

    private static final String TABLE_NAME = "history";
    private static final String COMMA_SEPARATOR = ", ";

    private static final String COLUMN_ID_TYPE = "integer PRIMARY KEY AUTOINCREMENT";
    private static final String COLUMN_ID = "id";

    private static final String COLUMN_TITLE_TYPE = "TEXT";
    private static final String COLUMN_TITLE = "title";

    private static final String COLUMN_LOGIN_TYPE = "TEXT";
    private static final String COLUMN_LOGIN = "login";

    private static final String COLUMN_PASSWORD_TYPE = "TEXT";
    private static final String COLUMN_PASSWORD = "password";

    private static final String COLUMN_ACTIVE_TYPE = "integer DEFAULT 1";
    private static final String COLUMN_ACTIVE = "active";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " " + COLUMN_ID_TYPE + COMMA_SEPARATOR +
            COLUMN_TITLE + " " + COLUMN_TITLE_TYPE + COMMA_SEPARATOR +
            COLUMN_LOGIN + " " + COLUMN_LOGIN_TYPE + COMMA_SEPARATOR +
            COLUMN_PASSWORD + " " + COLUMN_PASSWORD_TYPE + COMMA_SEPARATOR +
            COLUMN_ACTIVE + " " + COLUMN_ACTIVE_TYPE +
            ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    @Override
    public Account cursorToObject(Cursor cursor) {
        Account h = new Account();
        h.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        h.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        h.setLogin(cursor.getString(cursor.getColumnIndex(COLUMN_LOGIN)));
        h.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
        return h;
    }

    @Override
    public void add(Context context, Account h) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        String query = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_TITLE + ", " + COLUMN_LOGIN + ", " + COLUMN_PASSWORD + ") VALUES ( '" + h.getTitle() + "', '" + h.getLogin() + "', '" + h.getPassword() + "' )";
        database.execSQL(query);

        database.close();
    }

    public void deleteGood(Context context, int id) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ACTIVE, "0");

        database.update(TABLE_NAME, cv, "id="+id, null);
    }

    @Override
    public void clearAll(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        database.delete(TABLE_NAME, null, null);
    }

    @Override
    public List<Account> getAll(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        List<Account> historyList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ACTIVE + "= '" + 1 + "' ", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Account h = cursorToObject(cursor);
                    historyList.add(h);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get history from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        database.close();

        return historyList;
    }
}
