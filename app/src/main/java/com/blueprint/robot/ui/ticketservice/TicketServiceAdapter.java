package com.blueprint.robot.ui.ticketservice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueprint.robot.R;
import com.blueprint.robot.data.entity.ScenicSpot;

import java.util.List;

public class TicketServiceAdapter extends RecyclerView.Adapter<TicketServiceAdapter.TicketServiceViewHolder> {
    List<ScenicSpot> scenicSpotList;

    public TicketServiceAdapter(List<ScenicSpot> scenicSpotList) {
        this.scenicSpotList = scenicSpotList;
    }

    @NonNull
    @Override
    public TicketServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_ticket, parent, false);
        return new TicketServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketServiceViewHolder holder, int position) {
        ScenicSpot currentSpot = scenicSpotList.get(position);
        holder.textView_name.setText(currentSpot.getName());
        holder.textView_time.setText(currentSpot.getStartOpenTime() + "至" +currentSpot.getEndOpenTime());
        holder.textView_price.setText(String.valueOf(currentSpot.getPrice()));
    }

    @Override
    public int getItemCount() {
        return scenicSpotList.size();
    }

    static class TicketServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_name;
        private TextView textView_time;
        private TextView textView_price;

        public TicketServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.textView_name_cell_Ticket);
            textView_time = itemView.findViewById(R.id.textView_time_cell_Ticket);
            textView_price = itemView.findViewById(R.id.textView_price_cell_Ticket);
        }
    }
}
