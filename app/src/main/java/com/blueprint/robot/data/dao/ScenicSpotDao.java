package com.blueprint.robot.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.blueprint.robot.data.entity.ScenicSpot;

import java.util.List;

//offer the operation interface to operate database
@Dao
public interface ScenicSpotDao {
    @Insert
    long insertScenicSpot(ScenicSpot scenicSpot);

    @Update
    void updateScenicSpot(ScenicSpot scenicSpot);

    @Delete
    void deleteScenicSpot(ScenicSpot scenicSpot);

    @Query("select * from ScenicSpot")
    List<ScenicSpot> getAllScenicSpot();
}
