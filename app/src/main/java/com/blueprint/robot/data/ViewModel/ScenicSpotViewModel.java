package com.blueprint.robot.data.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.blueprint.robot.data.entity.ScenicSpot;
import com.blueprint.robot.data.repository.ScenicSpotRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ScenicSpotViewModel extends AndroidViewModel {

    private int numScenic;
    private int term = 0;
    private int position = 0;
    private int pictureNum = 0;

    public int getNumScenic() {
        return numScenic;
    }

    public void setNumScenic(int numScenic) {
        this.numScenic = numScenic;
    }

    private ScenicSpotRepository scenicSpotRepository;

    public ScenicSpotViewModel(@NotNull Application application) {
        super(application);
        scenicSpotRepository = new ScenicSpotRepository(application);
    }

    public List<ScenicSpot> getScenicSpotList() {
        return scenicSpotRepository.getScenicSpotList();
    }

    public void insertScenicSpot(ScenicSpot scenicSpot) {
        scenicSpotRepository.insertScenicSpot(scenicSpot);
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
