package com.example.moviesapp.data.local.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface RoomDao {
    @Insert()
    void addItem(FavoriteItem... item);

    @Delete
    void removeItem(FavoriteItem... item);

    @Update
    void update(FavoriteItem... item);

    @Query("select * from FavoriteItem")
    List<FavoriteItem> getAll();

    @Query("delete from FavoriteItem")
    void deleteAll();
}