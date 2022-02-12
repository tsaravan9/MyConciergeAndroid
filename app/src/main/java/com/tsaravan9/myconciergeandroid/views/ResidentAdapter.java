package com.tsaravan9.myconciergeandroid.views;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.models.User;

import java.util.ArrayList;
import java.util.List;

public class ResidentAdapter extends RecyclerView.Adapter<ResidentAdapter.ResidentHolder>{

    private List<User> residents = new ArrayList<>();
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
        Log.d("gggggg", currentUser.getFirstname() + " " + currentUser.getLastname());
        holder.textViewName.setText(currentUser.getFirstname() + " " + currentUser.getLastname());
        //set total residents here
        holder.textViewApartmentNumber.setText(currentUser.getApartment());
        //change image later
        holder.imageViewResidentImage.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
    }

    @Override
    public int getItemCount() {
        return residents.size();
    }

    public void setResidents(List<User> residents) {
        this.residents = residents;
        notifyDataSetChanged();
    }

    public User getUserAt(int position) {
        return residents.get(position);
    }

    class ResidentHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewApartmentNumber;
        private ImageView imageViewResidentImage;

        public ResidentHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.resident_name);
            textViewApartmentNumber = itemView.findViewById(R.id.resident_apartment_number);
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
}
