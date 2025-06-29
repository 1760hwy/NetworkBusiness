package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
   private static final String DATABASE_NAME = "user.db";
   private static final int DATABASE_VERSION = 1;
   private static final String TABLE_USERS = "users";
   private static final String COLUMN_ID = "id";
   private static final String COLUMN_USERNAME = "username";
   private static final String COLUMN_PASSWORD = "password";

   public DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
              COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
              COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
              COLUMN_PASSWORD + " TEXT NOT NULL)";
      db.execSQL(createTable);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
      onCreate(db);
   }

   public boolean registerUser(String username, String password) {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(COLUMN_USERNAME, username);
      values.put(COLUMN_PASSWORD, password); // In production, hash the password
      long result = db.insert(TABLE_USERS, null, values);
      return result != -1;
   }

   public boolean checkUsernameExists(String username) {
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
              COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
      boolean exists = cursor.getCount() > 0;
      cursor.close();
      return exists;
   }

   public boolean loginUser(String username, String password) {
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
              COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
              new String[]{username, password}, null, null, null);
      boolean success = cursor.getCount() > 0;
      cursor.close();
      return success;
   }
}