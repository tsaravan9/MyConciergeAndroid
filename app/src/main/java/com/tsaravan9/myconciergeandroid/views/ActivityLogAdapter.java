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
import com.tsaravan9.myconciergeandroid.models.Delivery;

import java.util.ArrayList;
import java.util.List;

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ActivityLogHolder> implements Filterable {

    private List<Delivery> deliveries = new ArrayList<>();
    private List<Delivery> tempDeliveries = new ArrayList<>();
    private ActivityLogAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public ActivityLogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_log_item, parent, false);
        return new ActivityLogHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityLogAdapter.ActivityLogHolder holder, int position) {
        Delivery currentDelivery = deliveries.get(position);
        holder.textViewName.setText(currentDelivery.getName());
        if (currentDelivery.getVisitor()){
            holder.imageViewActivityType.setImageResource(R.drawable.visitors);
        }
        else{
            holder.imageViewActivityType.setImageResource(R.drawable.newpackage);
        }
        if (currentDelivery.isAllowed()){
            holder.imageViewApprovedRejected.setImageResource(R.drawable.accept);
        }
        else{
            holder.imageViewApprovedRejected.setImageResource(R.drawable.rejected);
        }
    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.tempDeliveries = deliveries;
        this.deliveries = deliveries;
        notifyDataSetChanged();
    }

    public Delivery getBuildingAt(int position) {
        return deliveries.get(position);
    }

    class ActivityLogHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private ImageView imageViewActivityType;
        private ImageView imageViewApprovedRejected;

        public ActivityLogHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.activity_name);
            imageViewActivityType= itemView.findViewById(R.id.activity_img);
            imageViewApprovedRejected = itemView.findViewById(R.id.approved_rejected);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(deliveries.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Delivery delivery);
    }

    public void setOnItemClickListener(ActivityLogAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Filter getFilter(){
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                deliveries = tempDeliveries;
                FilterResults filterResults = new FilterResults();
                ArrayList<Delivery> tempList=new ArrayList<>();
                //constraint is the result from text you want to filter against.
                //objects is your data set you will filter from
                if(constraint != null && deliveries!=null) {
                    int length=deliveries.size();
                    int i=0;
                    while(i<length){
                        Delivery item=deliveries.get(i);
                        //do whatever you wanna do here
                        //adding result set output array
                        if (item.getName().toLowerCase().contains(constraint)){
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
                deliveries = (ArrayList<Delivery>) results.values;
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
