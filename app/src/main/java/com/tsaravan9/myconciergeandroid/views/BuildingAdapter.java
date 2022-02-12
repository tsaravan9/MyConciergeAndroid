package com.tsaravan9.myconciergeandroid.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.models.Building;

import java.util.ArrayList;
import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingHolder>{

    private List<Building> buildings = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public BuildingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.building_item, parent, false);
        return new BuildingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingHolder holder, int position) {
        Building currentBuilding = buildings.get(position);
        //manipulate address
        holder.textViewName.setText(currentBuilding.getAddress());
        //set total residents here
        holder.textViewTotalResidents.setText("5");
        //change image later
        holder.imageViewBuildingImage.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
        notifyDataSetChanged();
    }

    public Building getBuildingAt(int position) {
        return buildings.get(position);
    }

    class BuildingHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewTotalResidents;
        private ImageView imageViewBuildingImage;

        public BuildingHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.building_name);
            textViewTotalResidents = itemView.findViewById(R.id.building_total_residents);
            imageViewBuildingImage= itemView.findViewById(R.id.building_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(buildings.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Building building);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

