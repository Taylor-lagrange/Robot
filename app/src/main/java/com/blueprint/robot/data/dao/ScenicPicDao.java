package com.blueprint.robot.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.blueprint.robot.data.entity.ScenicPic;

import java.util.List;

//offer the operation interface to operate database
@Dao
public interface ScenicPicDao {
    @Insert
    void insertScenicPic(ScenicPic scenicPic);

    @Query("DELETE from ScenicPic where scenic_id=:scenicId")
    void deleteAllScenicPic(int scenicId);

    //return live data when data be changed, list update automatically
    @Query("select * from ScenicPic where scenic_id=:scenicId")
    List<ScenicPic> getScenicPicById(int scenicId);
}
