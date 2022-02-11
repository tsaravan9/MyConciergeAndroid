package com.tsaravan9.myconciergeandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.tsaravan9.myconciergeandroid.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getCanonicalName();
    private FirebaseAuth mAuth;
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.btnNextToBuilding.setOnClickListener(this);
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()){
                case R.id.btnNextToBuilding:{

                    break;
                }
            }
        }
    }
}