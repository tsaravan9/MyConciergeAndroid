package com.tsaravan9.myconciergeandroid.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding activityMainBinding;
    private final String TAG = this.getClass().getCanonicalName();
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;
    private UsersDBRepository userdb;
//    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.activityMainBinding.getRoot());

        activityMainBinding.btnSignIn.setOnClickListener(this);
        activityMainBinding.btnSignup.setOnClickListener(this);
        activityMainBinding.txtFrgtPass.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btnSignIn: {
                    Log.d(TAG, "onClick: Sign In Button Clicked");
//                    this.validateData();
                    break;
                }
                case R.id.btnSignup: {
                    Log.d(TAG, "onClick: Sign Up Button Clicked");
                    Intent signUpIntent = new Intent(this, SignupActivity.class);
                    startActivity(signUpIntent);
                    break;
                }
                case R.id.txtFrgtPass: {
                    Log.d(TAG, "onClick: Forgot password text Clicked");

                    break;
                }
            }
        }
    }
}