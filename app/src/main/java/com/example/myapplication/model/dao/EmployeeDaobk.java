package com.example.myapplication.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.db.AppDatabaseHelper;
import com.example.myapplication.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDaobk {
    private AppDatabaseHelper mAppDatabaseHelper;

    public EmployeeDaobk(Context context) {

        mAppDatabaseHelper = new AppDatabaseHelper(context);
    }
    public long insert(Employee employee){
        SQLiteDatabase db = mAppDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppDatabaseHelper.COL_EMP_NAME, employee.getName());
        values.put(AppDatabaseHelper.COL_EMP_DEPARTMENT, employee.getDepartment());
        values.put(AppDatabaseHelper.COL_EMP_POSITION, employee.getPosition());
        values.put(AppDatabaseHelper.COL_EMP_SALARY, employee.getSalary());

        return db.insert(AppDatabaseHelper.TABLE_EMPLOYEE, null, values);
    }
    public List<Employee> getAllEmp(){
        List<Employee> list = new ArrayList<>();
        SQLiteDatabase db = mAppDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + AppDatabaseHelper.TABLE_EMPLOYEE, null);
        while (cursor.moveToNext()) {
            Employee employee = new Employee();
            employee.setId(cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_EMP_ID)));
            employee.setName(cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_EMP_NAME)));
            employee.setDepartment(cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_EMP_DEPARTMENT)));
            employee.setPosition(cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_EMP_POSITION)));
            list.add(employee);
        }
        cursor.close();
        return list;
    }
}

