package com.blueprint.robot.data.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.blueprint.robot.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//entity to store the scenic spot information
@Entity
public class ScenicSpot {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;//scenic spot name

    @ColumnInfo(name = "start_open_time")
    private String startOpenTime;//start time describe of the spot open

    @ColumnInfo(name = "end_open_time")
    private String endOpenTime;//end time describe of the spot close

    @ColumnInfo(name = "suggest_play_time")
    private float suggestPlayTime;//the average time suggest tourist to play, an hour is equal to 1 ,half hour is equal to 0.5

    @ColumnInfo(name = "brief_intro")
    private String briefIntro;//the brief introduction to scenic spot

    @ColumnInfo(name = "price")
    private float price;//the ticket price to the scenic spot

    @Ignore
    private List<Integer> scenicPicUrlList;//the list to store the address of the picture in local machine

    public ScenicSpot(String name, String startOpenTime, String endOpenTime, float suggestPlayTime, String briefIntro, float price) {
        this.name = name;
        this.startOpenTime = startOpenTime;
        this.endOpenTime = endOpenTime;
        this.suggestPlayTime = suggestPlayTime;
        this.briefIntro = briefIntro;
        this.price = price;
    }

    @Ignore//test info
    public ScenicSpot() {
        this.name = "神泉谷";
        this.startOpenTime = "每周一到周五上午10点，周六周天上午12点";
        this.endOpenTime = "每周一到周五晚上10点，周六周天晚上10点";
        this.suggestPlayTime = 3f;
        this.briefIntro = "贵州省神泉谷景区位于长顺县城北部乌麻河流域，属典型喀斯特峰丛山谷地貌。在峡谷的平坦地带，种植有千亩花海，形成了风光旖旎的独特景观。";
        this.price = 150.9f;
        this.scenicPicUrlList = new ArrayList<>();
        this.scenicPicUrlList.add(R.drawable.attractionimage1);
        this.scenicPicUrlList.add(R.drawable.attractionimage2);
    }

    @Ignore
    public Bitmap getLocalBitmap(Context context, int index) {
//        try {
//            FileInputStream fis = new FileInputStream(this.scenicPicUrlList.get(index));
//            //return BitmapFactory.decodeStream(fis);
//            Bitmap bitmap = BitmapFactory.decodeStream(fis);
//            fis.close();
//            return bitmap;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }

        return BitmapFactory.decodeResource(context.getResources(), this.scenicPicUrlList.get(index), null);
    }

    @Override
    public String toString() {
        return "ScenicSpot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startOpenTime='" + startOpenTime + '\'' +
                ", endOpenTime='" + endOpenTime + '\'' +
                ", suggestPlayTime=" + suggestPlayTime +
                ", briefIntro='" + briefIntro + '\'' +
                ", price=" + price +
                ", scenicPicUrlList=" + scenicPicUrlList +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartOpenTime() {
        return startOpenTime;
    }

    public void setStartOpenTime(String startOpenTime) {
        this.startOpenTime = startOpenTime;
    }

    public String getEndOpenTime() {
        return endOpenTime;
    }

    public void setEndOpenTime(String endOpenTime) {
        this.endOpenTime = endOpenTime;
    }

    public List<Integer> getScenicPicUrlList() {
        return scenicPicUrlList;
    }

    public void setScenicPicUrlList(List<Integer> scenicPicUrlList) {
        this.scenicPicUrlList = scenicPicUrlList;
    }

    public float getSuggestPlayTime() {
        return suggestPlayTime;
    }

    public void setSuggestPlayTime(float suggestPlayTime) {
        this.suggestPlayTime = suggestPlayTime;
    }

    public String getBriefIntro() {
        return briefIntro;
    }

    public void setBriefIntro(String briefIntro) {
        this.briefIntro = briefIntro;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
