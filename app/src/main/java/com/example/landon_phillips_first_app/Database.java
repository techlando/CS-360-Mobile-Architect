package com.example.landon_phillips_first_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weight_tracker.db";
    private static final int DATABASE_VERSION = 3;

    // User table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Weight table
    public static final String TABLE_WEIGHTS = "weights";
    public static final String COLUMN_WEIGHT_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_DATE = "date";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                COLUMN_PHONE + " TEXT NOT NULL, " +
                COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                "goal_weight REAL)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create weights table
        String CREATE_WEIGHTS_TABLE = "CREATE TABLE " + TABLE_WEIGHTS + " (" +
                COLUMN_WEIGHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_WEIGHT + " REAL NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_WEIGHTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // --- USER LOGIC ---

    public boolean registerUser(String fullName, String phone, String username, String password, double goalWeight) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put("goal_weight", goalWeight);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int result = db.update(
                TABLE_USERS,
                values,
                COLUMN_USERNAME + "=?",
                new String[]{username}
        );

        db.close();

        return result > 0;
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;

        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return userId;
    }

    // --- WEIGHT TRACKING LOGIC ---

    public boolean addWeight(int userId, double weight, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_DATE, date);

        long result = db.insert(TABLE_WEIGHTS, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getWeightsForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_WEIGHTS +
                " WHERE " + COLUMN_USER_ID + "=? ORDER BY " + COLUMN_DATE + " DESC";

        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    public boolean updateWeight(int weightId, double newWeight, String newDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WEIGHT, newWeight);
        values.put(COLUMN_DATE, newDate);

        int result = db.update(
                TABLE_WEIGHTS,
                values,
                COLUMN_WEIGHT_ID + "=?",
                new String[]{String.valueOf(weightId)}
        );

        db.close();
        return result > 0;
    }

    public boolean deleteWeight(int weightId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_WEIGHTS,
                COLUMN_WEIGHT_ID + "=?",
                new String[]{String.valueOf(weightId)}
        );

        db.close();
        return result > 0;
    }

    public double getGoalWeight(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double goal = -1;

        Cursor cursor = db.rawQuery(
                "SELECT goal_weight FROM " + TABLE_USERS + " WHERE id=?",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            goal = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return goal;
    }
}