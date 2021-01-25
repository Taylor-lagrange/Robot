package com.blueprint.robot.ui.carouselactivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.blueprint.robot.data.entity.ScenicSpot;
import com.blueprint.robot.data.repository.ScenicSpotRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarouselViewModel extends AndroidViewModel {
    private ScenicSpotRepository scenicSpotRepository;
    private int term = 0;
    private int position = 0;
    private int pictureNum = 0;

    public CarouselViewModel(@NonNull Application application) {
        super(application);
        scenicSpotRepository = new ScenicSpotRepository(application);
    }

    public List<ScenicSpot> getPictureScenicSpotList() {
        List<ScenicSpot> scenicSpotList = scenicSpotRepository.getScenicSpotList();
        List<ScenicSpot> scenicSpots = new ArrayList<ScenicSpot>();
        for (ScenicSpot spot : scenicSpotList) {
            if(spot.getScenicPicUrlList().size() != 0) {
                scenicSpots.add(spot);
            }
        }
        pictureNum = scenicSpots.size();
        return scenicSpots;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPictureNum() {
        return pictureNum;
    }
}
