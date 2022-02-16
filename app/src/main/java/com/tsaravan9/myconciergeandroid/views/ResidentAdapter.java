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
import com.tsaravan9.myconciergeandroid.models.User;

import java.util.ArrayList;
import java.util.List;

public class ResidentAdapter extends RecyclerView.Adapter<ResidentAdapter.ResidentHolder> implements Filterable {

    private List<User> residents = new ArrayList<>();
    private List<User> tempResidents = new ArrayList<>();
    private ResidentAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public ResidentAdapter.ResidentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resident_item, parent, false);
        return new ResidentAdapter.ResidentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResidentAdapter.ResidentHolder holder, int position) {
        User currentUser = residents.get(position);
        holder.textViewName.setText(currentUser.getFirstname() + " " + currentUser.getLastname());
        //set total residents here
        holder.textViewApartmentNumber.setText("#"+currentUser.getApartment());
        //change image later
        holder.imageViewResidentImage.setImageResource(R.drawable.man);
        holder.contact.setText(currentUser.getMobileNumber() + "");
    }

    @Override
    public int getItemCount() {
        return residents.size();
    }

    public void setResidents(List<User> residents) {
        this.residents = residents;
        this.tempResidents = residents;
        notifyDataSetChanged();
    }

    public User getUserAt(int position) {
        return residents.get(position);
    }

    class ResidentHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewApartmentNumber;
        private ImageView imageViewResidentImage;
        private TextView contact;

        public ResidentHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.resident_name);
            textViewApartmentNumber = itemView.findViewById(R.id.resident_apartment_number);
            contact = itemView.findViewById(R.id.mobile_number);
            imageViewResidentImage= itemView.findViewById(R.id.resident_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(residents.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(ResidentAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Filter getFilter(){
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                residents = tempResidents;
                FilterResults filterResults = new FilterResults();
                ArrayList<User> tempList=new ArrayList<>();
                //constraint is the result from text you want to filter against.
                //objects is your data set you will filter from
                if(constraint != null && residents!=null) {
                    int length=residents.size();
                    int i=0;
                    while(i<length){
                        User item=residents.get(i);
                        //do whatever you wanna do here
                        //adding result set output array
                        if ((item.getFirstname() + " " + item.getLastname()).toLowerCase().contains(constraint)){
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
                residents = (ArrayList<User>) results.values;
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
