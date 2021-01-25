package com.blueprint.robot.ui.ticketservice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.blueprint.robot.R;
import com.blueprint.robot.data.ViewModel.ScenicSpotViewModel;
import com.blueprint.robot.data.entity.ScenicSpot;

import java.util.List;

public class TicketServiceFragment extends Fragment {
    private static final String TAG = "TicketServiceFragment";
    private RecyclerView recyclerView;
    private ImageButton imageButton;//if there is no ActionBar, press to get back

    public TicketServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.RecyclerView_Ticket);
        ScenicSpotViewModel viewModel = new ViewModelProvider(requireActivity()).get(ScenicSpotViewModel.class);
        TicketServiceAdapter adapter = new TicketServiceAdapter(viewModel.getScenicSpotList());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);

        //back to home page
        imageButton = view.findViewById(R.id.imageButton_back_Ticket);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigate");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_ticketServiceFragment_to_homeFragment);//navigate to home page
            }
        });
    }
}