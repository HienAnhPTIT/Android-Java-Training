package com.example.myapplication.model.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.db.entity.EmployeeEntity;

import java.util.List;

@Dao
public interface EmployeeDao {
    @Query("SELECT * FROM employees ORDER BY name COLLATE NOCASE ASC")
    LiveData<List<EmployeeEntity>> getAllLive();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceAll(List<EmployeeEntity> entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(EmployeeEntity entity);

    @Query("DELETE FROM employees WHERE id = :id")
    void deleteById(int id);

    @Query("DELETE FROM employees")
    void deleteAll();
}
