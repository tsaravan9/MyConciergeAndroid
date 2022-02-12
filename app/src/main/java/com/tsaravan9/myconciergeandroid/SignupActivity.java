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
    private String fName, lName, mail, pass, rePass, mob;

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
            switch (v.getId()) {
                case R.id.btnNextToBuilding: {
                    validateData();
                    break;
                }
            }
        }
    }

    private void validateData() {
        fName = this.binding.tedFName.getText().toString().trim();
        lName = this.binding.tedLName.getText().toString().trim();
        mail = this.binding.tedMail.getText().toString().trim();
        pass = this.binding.tedPass.getText().toString().trim();
        rePass = this.binding.tedRePass.getText().toString().trim();
        mob = this.binding.tedMob.getText().toString().trim();

        if (fName.isEmpty()) {
            this.binding.tilFName.setError("First name cannot be empty.");
        } else {
            this.binding.tilFName.setErrorEnabled(false);
        }
        if (lName.isEmpty()) {
            this.binding.tilLName.setError("last name cannot be empty.");
        } else {
            this.binding.tilLName.setErrorEnabled(false);
        }
        if (mail.isEmpty()) {
            this.binding.tilMail.setError("E-Mail cannot be empty.");
        } else {
            this.binding.tilMail.setErrorEnabled(false);
        }
        if (pass.isEmpty()) {
            this.binding.tilPass.setError("This field cannot be empty.");
        } else {
            this.binding.tilPass.setErrorEnabled(false);
        }
        if (rePass.isEmpty()) {
            this.binding.tilRePass.setError("This field cannot be empty.");
        } else {
            this.binding.tilRePass.setErrorEnabled(false);
        }
        if (mob.isEmpty()) {
            this.binding.tilMob.setError("Mobile number cannot be empty.");
        } else {
            this.binding.tilMob.setErrorEnabled(false);
        }

        if (!pass.equals(rePass)) {
            this.binding.tilRePass.setError("Passwords don't match");
        } else {
            this.binding.tilRePass.setErrorEnabled(false);
        }
    }
}