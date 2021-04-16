package com.blueprint.robot.ui.Choose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.blueprint.robot.R;
import com.blueprint.robot.data.ViewModel.ScenicSpotViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FunctionSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FunctionSelectionFragment extends Fragment {

    public FunctionSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FunctionSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FunctionSelectionFragment newInstance() {
        FunctionSelectionFragment fragment = new FunctionSelectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_function_selection, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton toHomeButton=getView().findViewById(R.id.toHomeButton);
        ImageButton toGoodsButton = getView().findViewById(R.id.toGoodsButton);
        ImageButton toShowButton = getView().findViewById(R.id.toShowButton);
        toHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller= Navigation.findNavController(view);
                controller.navigate(R.id.action_functionSelectionFragment_to_toHomeFragment);
            }
        });

        toGoodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller= Navigation.findNavController(view);
                controller.navigate(R.id.action_functionSelectionFragment_to_toGoodsFragment);
            }
        });

        toShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ScenicSpotViewModel scenicSpotViewModel = new ViewModelProvider(requireActivity()).get(ScenicSpotViewModel.class);
                scenicSpotViewModel.setNumScenic(0);
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_functionSelectionFragment_to_toShowFragment);
            }
        });
    }
}