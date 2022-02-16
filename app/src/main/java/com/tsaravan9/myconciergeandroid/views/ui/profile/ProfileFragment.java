package com.tsaravan9.myconciergeandroid.views.ui.profile;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.FragmentProfileBinding;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.BottomNavigationActivity;
import com.tsaravan9.myconciergeandroid.views.MainActivity;
import com.tsaravan9.myconciergeandroid.views.resident.ProfileActivity;

import java.io.IOException;
import java.lang.ref.Reference;
import java.util.UUID;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private FragmentProfileBinding binding;
    private UsersViewModel usersViewModel;
    private User loggedInUser;
    private SharedPreferences prefs;
    private Activity context;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        context = this.getActivity();
        prefs = context.getApplicationContext().getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        this.usersViewModel = UsersViewModel.getInstance(this.context.getApplication());
        loggedInUser = usersViewModel.getUserRepository().loggedInUser;
        String fullName = loggedInUser.getFirstname() + " " + loggedInUser.getLastname();
        this.binding.fullNameDisplay.setText(fullName);
        this.binding.emailDisplay.setText(loggedInUser.getEmail());
        this.binding.mobileDisplay.setText("Mobile Number: " + loggedInUser.getMobileNumber());
        this.binding.addressDisplay.setText("Address:" + loggedInUser.getApartment() + " - " + loggedInUser.getAddress());
        getProfilePicData();

        this.binding.proPicDisplay.setOnClickListener(this);
        this.binding.llcContactUsDisplay.setOnClickListener(this);
        this.binding.fbShareClicked.setOnClickListener(this);
        this.binding.waShareClicked.setOnClickListener(this);
        this.binding.instaShareClicked.setOnClickListener(this);
        this.binding.logOut.setOnClickListener(this);

        return root;
    }

    private void getProfilePicData() {
        if (!loggedInUser.getProPic().isEmpty()) {
            StorageReference mImageRef = storage.getReference(loggedInUser.getProPic());
            final long ONE_MEGABYTE = 1024 * 1024;
            mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    DisplayMetrics dm = new DisplayMetrics();
                    context.getWindowManager().getDefaultDisplay().getMetrics(dm);
                    binding.proPicDisplay.setImageBitmap(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.logOut: {
                    LogOutClicked();
                    break;
                }
                case R.id.llcContactUsDisplay: {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + "+16475108066"));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    break;
                }
                case R.id.proPicDisplay: {
                    chooseAndUploadImage();
                    break;
                }
                case R.id.fbShareClicked: {
                    if (isAppInstalled(context, "com.facebook.orca") || isAppInstalled(context, "com.facebook.katana")
                            || isAppInstalled(context, "com.example.facebook") || isAppInstalled(context, "com.facebook.android")) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/{fb_page_numerical_id}")));
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/{fb_page_name}")));
                    }
                    break;
                }
                case R.id.waShareClicked: {
                    Toast.makeText(context, "Instagram", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.instaShareClicked: {
                    if (isAppInstalled(context.getApplicationContext(), "com.instagram.android")) {
                        Toast.makeText(context, "Instagram", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/{instagram_page_name}")));
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/{instagram_page_name}")));
                    }
                    break;
                }
            }
        }
    }

    private void chooseAndUploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                binding.proPicDisplay.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String tempRef = "images/" + UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(tempRef);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            loggedInUser.setProPic(tempRef);
                            usersViewModel.updateUserPic(loggedInUser);
                            Toast.makeText(context.getApplicationContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context.getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
        }
    }

    private void LogOutClicked() {
        if (prefs.contains("USER_EMAIL")) {
            prefs.edit().remove("USER_EMAIL").apply();
        }
        this.mAuth = FirebaseAuth.getInstance();
        this.mAuth.signOut();
        Intent logOutIntent = new Intent(context, MainActivity.class);
        startActivity(logOutIntent);
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}