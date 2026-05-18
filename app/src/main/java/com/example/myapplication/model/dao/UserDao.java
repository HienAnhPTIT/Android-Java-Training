package com.example.myapplication.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.db.entity.UserEntity;

@Dao
public interface UserDao {
    @Insert
    long insert(UserEntity userEntity);

    @Query("SELECT EXISTS(SELECT 1 FROM userS WHERE username = :user AND password = :pw)" )
    boolean checkLogin(String user, String pw);
}
