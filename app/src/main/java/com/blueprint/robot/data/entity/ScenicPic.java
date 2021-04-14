package com.blueprint.robot.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//entity to store the scenic spot information
@Entity
public class ScenicPic {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "scenic_id")
    private int scenicId;//scenic spot id indicate the picture belong to

    @ColumnInfo(name = "pic_url")
    private int picUrl;//the file path to the pic store

    public ScenicPic(int scenicId, int picUrl) {
        this.scenicId = scenicId;
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return "ScenicPic{" +
                "id=" + id +
                ", scenicId=" + scenicId +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScenicId() {
        return scenicId;
    }

    public void setScenicId(int scenicId) {
        this.scenicId = scenicId;
    }

    public int getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(int picUrl) {
        this.picUrl = picUrl;
    }
}
