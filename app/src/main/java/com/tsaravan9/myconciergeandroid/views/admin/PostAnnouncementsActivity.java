package com.tsaravan9.myconciergeandroid.views.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityEnterPackageDetailsBinding;
import com.tsaravan9.myconciergeandroid.databinding.ActivityPostAnnouncementsBinding;
import com.tsaravan9.myconciergeandroid.models.Announcement;
import com.tsaravan9.myconciergeandroid.models.Delivery;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;

public class PostAnnouncementsActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityPostAnnouncementsBinding binding;
    private UsersViewModel usersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityPostAnnouncementsBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.btnPostAnnouncement.setOnClickListener(this);
        usersViewModel = UsersViewModel.getInstance(this.getApplication());
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btn_post_announcement:{
                    try {
                        this.validate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private void validate() throws Exception {
        Boolean validData = true;
        String name = "";
        String description = "";

        if (this.binding.editAnnouncementTitle.getText().toString().isEmpty()){
            validData = false;
            this.binding.editAnnouncementTitle.setError("Announcement Title Cannot Be Empty");
        }
        else{
            name = this.binding.editAnnouncementTitle.getText().toString();
        }
        if (this.binding.editAnnouncementDescription.getText().toString().isEmpty()){
            validData = false;
            this.binding.editAnnouncementDescription.setError("Announcement Description Cannot Be Empty");
        }
        else{
            description = this.binding.editAnnouncementDescription .getText().toString();
        }

        if (validData){
            Announcement announcement = new Announcement(name, description, System.currentTimeMillis());
            this.usersViewModel.addAnnouncement(announcement);
            Toast.makeText(this, "Announcement Posted!", Toast.LENGTH_SHORT).show();
            this.binding.editAnnouncementTitle.setText("");
            this.binding.editAnnouncementDescription.setText("");
        }
        else{
            Toast.makeText(this, "Please provide correct inputs", Toast.LENGTH_SHORT).show();
        }
    }
}