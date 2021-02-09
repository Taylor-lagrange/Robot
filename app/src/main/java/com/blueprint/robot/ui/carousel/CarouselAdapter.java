package com.blueprint.robot.ui.carousel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueprint.robot.R;

public abstract class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    private static final String TAG = "CarouselFigureAdapter";

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_carousel, parent, false);
        CarouselViewHolder viewHolder = new CarouselViewHolder(view);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigate");
                endActivity();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        loadImage(holder.imageView, position);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public abstract void endActivity();

    public abstract void loadImage(ImageView imageView, int position);

    static class CarouselViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_carousel);
        }
    }
}
