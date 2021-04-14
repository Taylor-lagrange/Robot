package com.blueprint.robot.ui.view;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueprint.robot.R;
import com.blueprint.robot.data.ViewModel.ScenicSpotViewModel;
import com.blueprint.robot.data.entity.ScenicSpot;

import java.util.List;

public class fragment_view extends Fragment {
    private ScenicSpotViewModel viewModel;

    public static fragment_view newInstance() {
        return new fragment_view();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ScenicSpotViewModel.class);//通过ScenicSpotViewModel对象获取所有景点信息
        List<ScenicSpot> scenicSpotList = viewModel.getScenicSpotList();
//        if (scenicSpotViewModel.getNumScenic()==1)
//        {
//            imageView.setImageResource(R.drawable.attractionimage1);
//        }
//        else
//        {
//            if (scenicSpotViewModel.getNumScenic()==2)
//            {
//                imageView.setImageResource(R.drawable.attractionimage2);
//            }
//            else
//            {
//                if (scenicSpotViewModel.getNumScenic()==3)
//                {
//                    imageView.setImageResource(R.drawable.attractionimage3);
//                }
//            }
//        }

        View view = getView();
        ScenicSpot currentScenicSpot = scenicSpotList.get(viewModel.getNumScenic() - 1);

        ImageView imageView = view.findViewById(R.id.viewViewImage);
        imageView.setImageResource(currentScenicSpot.getScenicPicUrlList().get(0));
        TextView scenicName = view.findViewById(R.id.viewViewName);
        scenicName.setText(currentScenicSpot.getName());
        TextView nameInCard = view.findViewById(R.id.textView_name_view);
        nameInCard.setText(currentScenicSpot.getName());
        TextView openTime = view.findViewById(R.id.textView_openTime_view);
        openTime.setText("开放时间：" + currentScenicSpot.getStartOpenTime() + " 至 " + currentScenicSpot.getEndOpenTime());
        TextView price = view.findViewById(R.id.textView_price_view);
        price.setText("票价：" + currentScenicSpot.getPrice());
        TextView introduction = getView().findViewById(R.id.viewViewBriefIntro);
        introduction.setText(currentScenicSpot.getBriefIntro());
        TextView suggestedTime = view.findViewById(R.id.textView_suggest_view);
        suggestedTime.setText("建议游玩时长：" + currentScenicSpot.getSuggestPlayTime()+"h");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //viewModel.setNumScenic(-1);
    }
}