package com.blueprint.robot.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.blueprint.robot.data.dao.ScenicPicDao;
import com.blueprint.robot.data.entity.ScenicPic;

//use singleton to create only connection to the database
@Database(entities = {ScenicPic.class}, version = 1, exportSchema = false)
public abstract class ScenicPicDataBase extends RoomDatabase {
    private static ScenicPicDataBase instance;

    static public synchronized ScenicPicDataBase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), ScenicPicDataBase.class, "scenic_spot_picture_data_base").allowMainThreadQueries().build();
        return instance;
    }

    public abstract ScenicPicDao getScenicPicDao();
}
