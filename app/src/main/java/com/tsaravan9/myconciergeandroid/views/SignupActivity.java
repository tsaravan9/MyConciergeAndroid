package com.tsaravan9.myconciergeandroid.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivitySignupBinding;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getCanonicalName();
    private FirebaseAuth mAuth;
    private ActivitySignupBinding binding;
    private String fName, lName, mail, pass, rePass;
    private int mob;

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
        String mobile = this.binding.tedMob.getText().toString().trim();
        Boolean validData = false;

        if (fName.isEmpty()) {
            this.binding.tilFName.setError("First name cannot be empty.");
            validData = false;
        } else {
            this.binding.tilFName.setErrorEnabled(false);
            validData = true;
        }
        if (lName.isEmpty()) {
            this.binding.tilLName.setError("last name cannot be empty.");
            validData = false;
        } else {
            this.binding.tilLName.setErrorEnabled(false);
            validData = true;
        }
        if (mail.isEmpty()) {
            this.binding.tilMail.setError("E-Mail cannot be empty.");
            validData = false;
        } else {
            this.binding.tilMail.setErrorEnabled(false);
            validData = true;
        }

        if (mobile.isEmpty()) {
            this.binding.tilMob.setError("Mobile number cannot be empty.");
            validData = false;
        } else {
            mob = Integer.parseInt(mobile);
            this.binding.tilMob.setErrorEnabled(false);
            validData = true;
        }

        if (pass.isEmpty()) {
            this.binding.tilPass.setError("This field cannot be empty.");
            validData = false;
        } else {
            if (rePass.isEmpty()) {
                this.binding.tilRePass.setError("This field cannot be empty.");
                validData = false;
            } else {
                if (!pass.equals(rePass)) {
                    this.binding.tilRePass.setError("Passwords don't match");
                    validData = false;
                } else {
                    this.binding.tilRePass.setErrorEnabled(false);
                    validData = true;
                }
            }
        }

        //TODO: check if the account already exists or not with email
        if (validData) {
            User newUser = new User(fName, lName, mail, pass, mob, false);
            createAcccount(newUser);
        } else {
            Snackbar.make(this.binding.llSignUp, "Please provide valid details", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void createAcccount(User newUser) {
        mAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPass())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                UsersViewModel.getInstance(getApplication()).addUser(newUser);
                                Toast.makeText(SignupActivity.this, "Sign up Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                Snackbar.make(binding.llSignUp, "There was a problem creating your account, Please try again later", Snackbar.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else {
                            Log.e(TAG, "onComplete: Failed to create user with email and password" + task.getException() + task.getException().getLocalizedMessage());
                            Snackbar.make(binding.llSignUp, "Authentication Failed", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}