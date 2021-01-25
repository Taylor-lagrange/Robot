package com.blueprint.robot.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.blueprint.robot.data.dao.ScenicSpotDao;
import com.blueprint.robot.data.entity.ScenicSpot;

//use singleton to create only connection to the database
@Database(entities = {ScenicSpot.class}, version = 1, exportSchema = false)
public abstract class ScenicSpotDataBase extends RoomDatabase {
    private static ScenicSpotDataBase instance;

    static public synchronized ScenicSpotDataBase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), ScenicSpotDataBase.class, "scenic_spot_data_base").allowMainThreadQueries().build();
        return instance;
    }

    public abstract ScenicSpotDao getScenicSpotDao();
}
