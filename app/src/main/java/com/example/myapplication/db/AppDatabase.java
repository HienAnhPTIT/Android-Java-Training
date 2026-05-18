package com.example.myapplication.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.db.entity.EmployeeEntity;
import com.example.myapplication.db.entity.UserEntity;
import com.example.myapplication.model.dao.EmployeeDao;
import com.example.myapplication.model.dao.UserDao;

@Database(entities = {UserEntity.class, EmployeeEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    public abstract EmployeeDao employeeDao();
    public abstract UserDao userDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(
                            context,
                            AppDatabase.class,
                            "employee_db")
                        .fallbackToDestructiveMigration().build();
            }
        }
        return INSTANCE;
    }
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE users ADD COLUMN age INTEGER DEFAULT 0");
        }
    };
}
