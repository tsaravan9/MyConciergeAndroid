package com.tsaravan9.myconciergeandroid.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.models.Announcement;
import com.tsaravan9.myconciergeandroid.models.Delivery;
import com.tsaravan9.myconciergeandroid.models.Text;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.databinding.ActivityMainBinding;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.admin.BuildingsListActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding activityMainBinding;
    private final String TAG = this.getClass().getCanonicalName();
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;
    private UsersDBRepository userdb;
    private UsersViewModel usersViewModel;
    private User matchedUser;
//    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.activityMainBinding.getRoot());

        prefs = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);
        this.mAuth = FirebaseAuth.getInstance();
        this.usersViewModel = UsersViewModel.getInstance(this.getApplication());
        this.userdb = this.usersViewModel.getUserRepository();

        activityMainBinding.btnSignIn.setOnClickListener(this);
        activityMainBinding.btnSignup.setOnClickListener(this);
        activityMainBinding.txtFrgtPass.setOnClickListener(this);

        checkIfUserPrefExisted();

    }

    private void checkIfUserPrefExisted() {
//        TODO: add logic if user data is pre-existed.
//        if (prefs.contains("USER_EMAIL") && prefs.contains("USER_PASSWORD")) {
//
//        }
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btnSignIn: {
                    Log.d(TAG, "onClick: Sign In Button Clicked");
                    this.validateData();
                    //goToDashboardPage();
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

    private void validateData() {
        Boolean validData = false;
        String email = this.activityMainBinding.tedMail.getText().toString();
        String password = this.activityMainBinding.tedPass.getText().toString();

        if (email.isEmpty()) {
            this.activityMainBinding.TILMail.setError("Email Cannot be Empty");
            validData = false;
        } else {
            this.activityMainBinding.TILMail.setErrorEnabled(false);
            validData = true;
        }

        if (password.isEmpty()) {
            this.activityMainBinding.TILPass.setError("Email Cannot be Empty");
            validData = false;
        } else {
            this.activityMainBinding.TILPass.setErrorEnabled(false);
            validData = true;
        }

        if (validData) {
            this.signIn(email, password);
        } else {
            Snackbar.make(this.activityMainBinding.llc, "Please provide correct inputs", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Sign In Successful");
                            userdb.loggedInUserEmail = email;
                            saveToPrefs(email, password);
                            //goToBuildingsListPage();
                            goToDashboardPage();
                        } else {
                            Log.e(TAG, "onComplete: Sign In Failed", task.getException());
                            Snackbar.make(activityMainBinding.llc, "Authentication Failed", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveToPrefs(String email, String password) {
        prefs.edit().putString("USER_EMAIL", email).apply();
//        if (this.activityMainBinding.chkBox.isChecked()) {
//            prefs.edit().putString("USER_EMAIL", email).apply();
//            prefs.edit().putString("USER_PASSWORD", password).apply();
//        } else {
//            if (prefs.contains("USER_EMAIL")) {
//                prefs.edit().remove("USER_EMAIL").apply();
//            }
//            if (prefs.contains("USER_PASSWORD")) {
//                prefs.edit().remove("USER_PASSWORD").apply();
//            }
//        }
    }

    private void goToDashboardPage() {
        //this.testingDeliveries();
        //this.testingAnnouncements();
        Intent intent = new Intent(this, BottomNavigationActivity.class);
        startActivity(intent);
    }

    private void goToBuildingsListPage(){
        Intent intent = new Intent(this, BuildingsListActivity.class);
        startActivity(intent);
    }

    //temporary method
    private void testingDeliveries(){
        Log.d("email test", prefs.getString("USER_EMAIL", ""));
        usersViewModel.getUserRepository().loggedInUserEmail = prefs.getString("USER_EMAIL", "");
        usersViewModel.getAllDeliveries();
        usersViewModel.allDeliveries.observe(this, new Observer<List<Delivery>>() {
            @Override
            public void onChanged(List<Delivery> deliveries) {
                for (Delivery delivery : deliveries){
                    Log.d("testMe", delivery.toString());
                    if (delivery.getVisitor()){
                        delivery.setAccepted(true);
                        usersViewModel.updateDelivery(delivery);
                    }
                }
            }
        });
    }

    //temporary method
    private void testingAnnouncements(){
        usersViewModel.getUserRepository().loggedInUserEmail = prefs.getString("USER_EMAIL", "");
        usersViewModel.searchUserByEmail(usersViewModel.getUserRepository().loggedInUserEmail);
        this.usersViewModel.getUserRepository().userFromDB.observe(MainActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                matchedUser = user;
                usersViewModel.getUserRepository().currentBuilding = matchedUser.getAddress();
                usersViewModel.getAllAnnouncements();
                usersViewModel.allAnnouncements.observe(MainActivity.this, new Observer<List<Announcement>>() {
                    @Override
                    public void onChanged(List<Announcement> announcements) {
                        for (Announcement announcement : announcements)
                            Log.d("testMe", announcement.toString());
                    }
                });
            }
        });
    }

}