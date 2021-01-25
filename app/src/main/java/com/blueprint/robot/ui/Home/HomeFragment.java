package com.blueprint.robot.ui.Home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blueprint.robot.R;
import com.blueprint.robot.data.ViewModel.ScenicSpotViewModel;
import com.blueprint.robot.data.entity.ScenicSpot;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Button storeButton,ticketButton,attraction1Button,attraction2Button,attraction3Button;
        storeButton=getView().findViewById(R.id.storeButton);
        ticketButton=getView().findViewById(R.id.ticketButton);
        attraction1Button=getView().findViewById(R.id.attraction1);
        attraction2Button=getView().findViewById(R.id.attraction2);
        attraction3Button=getView().findViewById(R.id.attraction3);


        final ScenicSpotViewModel scenicSpotViewModel = new ViewModelProvider(requireActivity()).get(ScenicSpotViewModel.class);//通过ScenicSpotViewModel对象获取所有景点信息
        final List<ScenicSpot> scenicSpotList = scenicSpotViewModel.getScenicSpotList();
        //background.setImageBitmap(scenicSpotList.get(scenicSpotList.size()-1).getLocalBitmap(0));
       storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_layout, null,false);
                final PopupWindow PopupWindow=new PopupWindow(contentView, ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT,true);
                PopupWindow.setTouchable(true);
                PopupWindow.showAsDropDown(attraction1Button,600,0);
                Button backButton=PopupWindow.getContentView().findViewById(R.id.popBackButton);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupWindow.dismiss();
                    }
                });
            }
        });
        ticketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_layout, null,false);
                final PopupWindow PopupWindow=new PopupWindow(contentView, ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT,true);
                PopupWindow.setTouchable(true);
                TextView title= PopupWindow.getContentView().findViewById(R.id.popTitle);
                title.setText("售票厅");
                PopupWindow.showAsDropDown(attraction1Button,600,0);
                Button backButton=PopupWindow.getContentView().findViewById(R.id.popBackButton);
                Button introduceButton=PopupWindow.getContentView().findViewById(R.id.popIntroduceButton);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupWindow.dismiss();
                    }
                });
                introduceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupWindow.dismiss();
                            View v= getView();
                            NavController controller = Navigation.findNavController(v);
                            controller.navigate(R.id.action_homeFragment_to_ticketServiceFragment);
                    }
                });
            }
        });
        attraction1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_layout, null,false);

                final PopupWindow PopupWindow=new PopupWindow(contentView, ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT,true);
                PopupWindow.setTouchable(true);
                ImageView attractionView=PopupWindow.getContentView().findViewById(R.id.popImage);
                attractionView.setImageResource(R.drawable.attractionimage1);
                TextView title= PopupWindow.getContentView().findViewById(R.id.popTitle);
                title.setText(scenicSpotList.get(0).getName());
                PopupWindow.showAsDropDown(attraction1Button,600,0);
                Button backButton=PopupWindow.getContentView().findViewById(R.id.popBackButton);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupWindow.dismiss();
                    }
                });
                Button introduceButton=PopupWindow.getContentView().findViewById(R.id.popIntroduceButton);
                introduceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scenicSpotViewModel.setNumScenic(1);
                        PopupWindow.dismiss();
                        View v= getView();
                        NavController controller = Navigation.findNavController(v);
                        controller.navigate(R.id.action_homeFragment_to_fragment_view);
                    }
                });
            }
        });
        attraction2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_layout, null,false);
                final PopupWindow PopupWindow=new PopupWindow(contentView, ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT,true);
                PopupWindow.setTouchable(true);
                ImageView attractionView=PopupWindow.getContentView().findViewById(R.id.popImage);
                attractionView.setImageResource(R.drawable.attractionimage2);
                TextView title= PopupWindow.getContentView().findViewById(R.id.popTitle);
                title.setText(scenicSpotList.get(1).getName());
                PopupWindow.showAsDropDown(attraction1Button,600,0);
                Button backButton=PopupWindow.getContentView().findViewById(R.id.popBackButton);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupWindow.dismiss();
                    }
                });
                Button introduceButton=PopupWindow.getContentView().findViewById(R.id.popIntroduceButton);
                introduceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scenicSpotViewModel.setNumScenic(2);
                        PopupWindow.dismiss();
                        View v= getView();
                        NavController controller = Navigation.findNavController(v);
                        controller.navigate(R.id.action_homeFragment_to_fragment_view);
                    }
                });
            }
        });
        attraction3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_layout, null,false);
                final PopupWindow PopupWindow=new PopupWindow(contentView, ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT,true);
                PopupWindow.setTouchable(true);
                ImageView attractionView=PopupWindow.getContentView().findViewById(R.id.popImage);
                attractionView.setImageResource(R.drawable.attractionimage3);
                TextView title= PopupWindow.getContentView().findViewById(R.id.popTitle);
                title.setText(scenicSpotList.get(2).getName());
                PopupWindow.showAsDropDown(attraction1Button,600,0);
                Button backButton=PopupWindow.getContentView().findViewById(R.id.popBackButton);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupWindow.dismiss();
                    }
                });
                Button introduceButton=PopupWindow.getContentView().findViewById(R.id.popIntroduceButton);
                introduceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scenicSpotViewModel.setNumScenic(3);
                        PopupWindow.dismiss();
                        View v= getView();
                        NavController controller = Navigation.findNavController(v);
                        controller.navigate(R.id.action_homeFragment_to_fragment_view);
                    }
                });
            }
        });
    }
}