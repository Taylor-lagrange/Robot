package com.blueprint.robot.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.blueprint.robot.data.dao.ScenicPicDao;
import com.blueprint.robot.data.dao.ScenicSpotDao;
import com.blueprint.robot.data.database.ScenicPicDataBase;
import com.blueprint.robot.data.database.ScenicSpotDataBase;
import com.blueprint.robot.data.entity.ScenicPic;
import com.blueprint.robot.data.entity.ScenicSpot;

import java.util.ArrayList;
import java.util.List;

//the class to asynchronous implement the operation to the database
public class ScenicSpotRepository {
    private static List<ScenicSpot> scenicSpotList;
    private ScenicSpotDao scenicSpotDao;
    private ScenicPicDao scenicPicDao;


    public ScenicSpotRepository(Context context) {
        scenicSpotDao = ScenicSpotDataBase.getInstance(context).getScenicSpotDao();
        scenicPicDao = ScenicPicDataBase.getInstance(context).getScenicPicDao();
    }

    public void insertScenicSpot(ScenicSpot scenicSpot) {
        new InsertAsyncTask(scenicSpotDao, scenicPicDao).execute(scenicSpot);
    }

    public List<ScenicSpot> getScenicSpotList() {
        if (ScenicSpotRepository.scenicSpotList == null) {
            ScenicSpotRepository.scenicSpotList = scenicSpotDao.getAllScenicSpot();//query all scenic spot
            for (int i = 0; i < ScenicSpotRepository.scenicSpotList.size(); i++) {
                List<ScenicPic> scenicPicList = scenicPicDao.getScenicPicById(scenicSpotList.get(i).getId());//query all picture for each scenic spot
                List<Integer> picUrl = new ArrayList<>();//get url from Scenic Picture
                for (ScenicPic pic : scenicPicList)
                    picUrl.add(pic.getPicUrl());
                ScenicSpotRepository.scenicSpotList.get(i).setScenicPicUrlList(picUrl);//set url from Scenic Picture
            }
        }
        return ScenicSpotRepository.scenicSpotList;
    }

    static class InsertAsyncTask extends AsyncTask<ScenicSpot, Void, Void> {
        private ScenicSpotDao scenicSpotDao;
        private ScenicPicDao scenicPicDao;

        public InsertAsyncTask(ScenicSpotDao scenicSpotDao, ScenicPicDao scenicPicDao) {
            this.scenicPicDao = scenicPicDao;
            this.scenicSpotDao = scenicSpotDao;
        }

        @Override
        protected Void doInBackground(ScenicSpot... scenicSpots) {
            for (ScenicSpot s : scenicSpots)//insert spot first
            {
                int id = (int) scenicSpotDao.insertScenicSpot(s);//get the primer key of the insert operation
                if (s.getScenicPicUrlList() != null)//null point judge
                    for (int i = 0; i < s.getScenicPicUrlList().size(); i++)//if list not empty insert all url address it store
                        scenicPicDao.insertScenicPic(new ScenicPic(id, s.getScenicPicUrlList().get(i)));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ScenicSpotRepository.scenicSpotList = null;//set null to query the scenic info again
        }
    }
}
