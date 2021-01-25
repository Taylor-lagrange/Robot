package com.blueprint.robot.data.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.blueprint.robot.data.entity.ScenicSpot;
import com.blueprint.robot.data.repository.ScenicSpotRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ScenicSpotViewModel extends AndroidViewModel {

    private int numScenic;

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

}
