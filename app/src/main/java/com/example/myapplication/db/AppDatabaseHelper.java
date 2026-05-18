package com.example.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "company.db";
    private static final int DB_VERSION = 1;

    //Table users
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    //Table employees
    public static final String TABLE_EMPLOYEE = "employee";
    public static final String COL_EMP_ID = "id";
    public static final String COL_EMP_NAME = "name";
    public static final String COL_EMP_MAIL = "mail";
    public static final String COL_EMP_DEPARTMENT = "department";
    public static final String COL_EMP_POSITION = "position";
    public static final String COL_EMP_SALARY = "salary";

    public AppDatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USERS + "(" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " TEXT NOT NULL UNIQUE," +
                COL_PASSWORD + " TEXT NOT NULL" +
                ");";
        String createEmpTable =
                "CREATE TABLE " + TABLE_EMPLOYEE + "(" +
                        COL_EMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COL_EMP_NAME + " TEXT NOT NULL," +
                        COL_EMP_MAIL + " TEXT," +
                        COL_EMP_DEPARTMENT + " TEXT," +
                        COL_EMP_POSITION + " TEXT," +
                        COL_EMP_SALARY + " REAL" +
                        ");";
        db.execSQL(createUserTable);
        db.execSQL(createEmpTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        onCreate(db);
    }
}
