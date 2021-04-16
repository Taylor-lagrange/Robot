package com.blueprint.robot.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.blueprint.robot.R;
import com.blueprint.robot.data.ViewModel.ScenicSpotViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ToHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToHomeFragment newInstance(String param1, String param2) {
        ToHomeFragment fragment = new ToHomeFragment();
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
        return inflater.inflate(R.layout.fragment_to_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ScenicSpotViewModel viewModel = new ViewModelProvider(requireActivity()).get(ScenicSpotViewModel.class);
        viewModel.setNumScenic(-1);
        ImageButton backButton = view.findViewById(R.id.imageButton_back_toHome);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.getNumScenic() == -1) {//在全览界面
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_toHomeFragment_to_functionSelectionFragment);
                } else {
                    viewModel.setNumScenic(-1);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_toHome);
                    navController.navigateUp();
                }
            }
        });
    }
}