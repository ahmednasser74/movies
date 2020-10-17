package com.example.moviesapp.data.local.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.moviesapp.data.model.moviesModel.MovieData;

@Database(entities = {FavoriteItem.class}, version = 4, exportSchema = false)

public abstract class RoomManger extends RoomDatabase {
    public abstract RoomDao roomDao();

    private static RoomManger roomManger;

    public static synchronized RoomManger getInstance(Context context) {
        if (roomManger == null) {
            roomManger = Room.databaseBuilder(context.getApplicationContext(), RoomManger.class, "movieDatabase")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return roomManger;
    }
}
