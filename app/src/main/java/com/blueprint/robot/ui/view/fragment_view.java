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
        final ScenicSpotViewModel scenicSpotViewModel = new ViewModelProvider(requireActivity()).get(ScenicSpotViewModel.class);//通过ScenicSpotViewModel对象获取所有景点信息
        List<ScenicSpot> scenicSpotList = scenicSpotViewModel.getScenicSpotList();
        ImageView imageView=getView().findViewById(R.id.scenicImage);
        if (scenicSpotViewModel.getNumScenic()==1)
        {
            imageView.setImageResource(R.drawable.attractionimage1);
        }
        else
        {
            if (scenicSpotViewModel.getNumScenic()==2)
            {
                imageView.setImageResource(R.drawable.attractionimage2);
            }
            else
            {
                if (scenicSpotViewModel.getNumScenic()==3)
                {
                    imageView.setImageResource(R.drawable.attractionimage3);
                }
            }
        }
        TextView scenicName=getView().findViewById(R.id.scenicName);
        scenicName.setText(scenicSpotList.get(scenicSpotViewModel.getNumScenic()-1).getName());

        TextView openTime=getView().findViewById(R.id.openTime);
        openTime.setText("开启时间："+scenicSpotList.get(scenicSpotViewModel.getNumScenic()-1).getStartOpenTime()+"\n结束时间："+scenicSpotList.get(scenicSpotViewModel.getNumScenic()-1).getEndOpenTime());

        TextView ticketPrice=getView().findViewById(R.id.ticketPrice);
        ticketPrice.setText("票价"+String.valueOf(scenicSpotList.get(scenicSpotViewModel.getNumScenic()-1).getPrice()));

        TextView suggestedTime=getView().findViewById(R.id.suggestedTime);
        suggestedTime.setText("建议游玩时长"+String.valueOf(scenicSpotList.get(scenicSpotViewModel.getNumScenic()-1).getSuggestPlayTime())+"h");

        TextView introduction=getView().findViewById(R.id.introduction);
        introduction.setText(scenicSpotList.get(scenicSpotViewModel.getNumScenic()-1).getBriefIntro());

        ImageButton button=getView().findViewById(R.id.imageButton_back_View);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_fragment_view_to_homeFragment);
            }
        });
    }

}