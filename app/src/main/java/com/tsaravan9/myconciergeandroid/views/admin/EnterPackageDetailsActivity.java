package com.tsaravan9.myconciergeandroid.views.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityBuildingsListBinding;
import com.tsaravan9.myconciergeandroid.databinding.ActivityEnterPackageDetailsBinding;
import com.tsaravan9.myconciergeandroid.models.Delivery;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;

public class EnterPackageDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityEnterPackageDetailsBinding binding;
    private UsersViewModel usersViewModel;
    private Boolean isVisitor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityEnterPackageDetailsBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.switchPackageVisitor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!compoundButton.isChecked()){
                    binding.tvPackageName.setText("Package Name");
                    binding.editPackageName.setHint("Enter Package Name");
                    binding.tvPackageDescription.setText("Package Description");
                    binding.editPackageDescription.setHint("Enter Package Details");
                    binding.editPackageName.setText("");
                    binding.editPackageDescription.setText("");
                }
                else{
                    binding.tvPackageName.setText("Visitor Name");
                    binding.editPackageName.setHint("Enter Visitor's Name");
                    binding.tvPackageDescription.setText("Visitor Contact");
                    binding.editPackageDescription.setHint("Enter Visitor's Phone Number");
                    isVisitor = true;
                    binding.editPackageName.setText("");
                    binding.editPackageDescription.setText("");
                }
            }
        });

        this.binding.btnSubmitPackage.setOnClickListener(this);
        usersViewModel = UsersViewModel.getInstance(this.getApplication());
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btn_submit_package:{
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

        if (this.binding.editPackageName.getText().toString().isEmpty()){
            validData = false;
            this.binding.editPackageName.setError(this.binding.tvPackageName.getText().toString() +  " Cannot Be Empty");
        }
        else{
            name = this.binding.editPackageName.getText().toString();
        }
        if (this.binding.editPackageDescription.getText().toString().isEmpty()){
            validData = false;
            this.binding.editPackageDescription.setError(this.binding.tvPackageDescription.getText().toString() + " Cannot Be Empty");
        }
        else{
            description = this.binding.editPackageDescription.getText().toString();
        }

        if (validData){
            Delivery delivery = new Delivery(name, description, this.isVisitor);
            this.usersViewModel.addPackage(delivery);
            Toast.makeText(this, "Information Submitted!", Toast.LENGTH_SHORT).show();
            this.binding.editPackageName.setText("");
            this.binding.editPackageDescription.setText("");
        }
        else{
            Toast.makeText(this, "Please provide correct inputs", Toast.LENGTH_SHORT).show();
        }
    }




}