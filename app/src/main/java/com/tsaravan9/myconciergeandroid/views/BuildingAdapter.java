package com.tsaravan9.myconciergeandroid.views;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.models.Building;

import java.util.ArrayList;
import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingHolder> implements Filterable {

    private List<Building> buildings = new ArrayList<>();
    private List<Building> tempBuildings = new ArrayList<>();
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
        holder.textViewTotalResidents.setText("Total Residents: 5");
        //change image later
        holder.imageViewBuildingImage.setImageResource(R.drawable.apartment);
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public void setBuildings(List<Building> buildings) {
        this.tempBuildings = buildings;
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

    @Override
    public Filter getFilter(){
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                buildings = tempBuildings;
                FilterResults filterResults = new FilterResults();
                ArrayList<Building> tempList=new ArrayList<>();
                //constraint is the result from text you want to filter against.
                //objects is your data set you will filter from
                if(constraint != null && buildings!=null) {
                    int length=buildings.size();
                    int i=0;
                    while(i<length){
                        Building item=buildings.get(i);
                        //do whatever you wanna do here
                        //adding result set output array
                        if (item.getAddress().toLowerCase().contains(constraint)){
                            tempList.add(item);
                        }
                        i++;
                    }
                    //following two lines is very important
                    //as publish result can only take FilterResults objects
                    filterResults.values = tempList;
                    filterResults.count = tempList.size();
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                buildings = (ArrayList<Building>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    Log.d("Filter Buildings", "No search results");
                    notifyDataSetChanged();
                }
            }
        };
    }
}


