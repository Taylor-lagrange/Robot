package com.blueprint.robot.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueprint.robot.R;
import com.blueprint.robot.data.ViewModel.ScenicSpotViewModel;
import com.blueprint.robot.data.entity.ScenicSpot;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowFragment newInstance(String param1, String param2) {
        ShowFragment fragment = new ShowFragment();
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
        return inflater.inflate(R.layout.fragment_show, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ScenicSpotViewModel scenicSpotViewModel = new ViewModelProvider(requireActivity()).get(ScenicSpotViewModel.class);//通过ScenicSpotViewModel对象获取所有景点信息
        final List<ScenicSpot> scenicSpotList = scenicSpotViewModel.getScenicSpotList();
        final View view = getView();

        ScenicSpot currentScenicSpot = scenicSpotList.get(scenicSpotViewModel.getNumScenic());
        final ImageView imageView = view.findViewById(R.id.showViewImage);
        imageView.setImageResource(currentScenicSpot.getScenicPicUrlList().get(0));
        final TextView scenicName = view.findViewById(R.id.showViewName);
        scenicName.setText(currentScenicSpot.getName());
        final TextView nameInCard = view.findViewById(R.id.textView_name_show);
        nameInCard.setText(currentScenicSpot.getName());
        final TextView openTime = view.findViewById(R.id.textView_openTime_show);
        openTime.setText("开放时间：" + currentScenicSpot.getStartOpenTime() + " 至 " + currentScenicSpot.getEndOpenTime());
        final TextView price = view.findViewById(R.id.textView_price_show);
        price.setText("价格：" + currentScenicSpot.getPrice());
        final TextView introduction = getView().findViewById(R.id.showViewBriefIntro);
        introduction.setText(currentScenicSpot.getBriefIntro());

        Button button = getView().findViewById(R.id.lastViewButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scenicSpotViewModel.setNumScenic((scenicSpotViewModel.getNumScenic() - 1 + scenicSpotList.size()) % scenicSpotList.size());
                ScenicSpot currentScenicSpot = scenicSpotList.get(scenicSpotViewModel.getNumScenic());
                imageView.setImageResource(currentScenicSpot.getScenicPicUrlList().get(0));
                scenicName.setText(currentScenicSpot.getName());
                nameInCard.setText(currentScenicSpot.getName());
                openTime.setText("开放时间：" + currentScenicSpot.getStartOpenTime() + " 至 " + currentScenicSpot.getEndOpenTime());
                price.setText("价格：" + currentScenicSpot.getPrice());
                introduction.setText(currentScenicSpot.getBriefIntro());
            }
        });

        Button button2 = getView().findViewById(R.id.nextViewButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scenicSpotViewModel.setNumScenic((scenicSpotViewModel.getNumScenic() + 1) % scenicSpotList.size());
                ScenicSpot currentScenicSpot = scenicSpotList.get(scenicSpotViewModel.getNumScenic());
                imageView.setImageResource(currentScenicSpot.getScenicPicUrlList().get(0));
                scenicName.setText(currentScenicSpot.getName());
                nameInCard.setText(currentScenicSpot.getName());
                openTime.setText("开放时间：" + currentScenicSpot.getStartOpenTime() + " 至 " + currentScenicSpot.getEndOpenTime());
                price.setText("票价：" + currentScenicSpot.getPrice());
                introduction.setText(currentScenicSpot.getBriefIntro());
            }
        });
    }
}