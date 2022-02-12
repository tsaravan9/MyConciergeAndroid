package com.tsaravan9.myconciergeandroid;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersDBRepository {
    private final String TAG = this.getClass().getCanonicalName();
    private final FirebaseFirestore DB;
    private final String COLLECTION_USERS = "Users";
    //    private final String COLLECTION_FRIENDS = "MyFriends";
//    private final String FIELD_NAME = "name";
//    private final String FIELD_PHONE = "phoneNumber";
//    private final String FIELD_BIRTHDATE = "birthdate";
    public String loggedInUserEmail = "";

//    public MutableLiveData<List<Friend>> allFriends = new MutableLiveData<>();
//    public MutableLiveData<Friend> friendFromDB = new MutableLiveData<>();

    public UsersDBRepository() {
        DB = FirebaseFirestore.getInstance();
    }

    public void addUser() {
        try {
            Map<String, Object> data = new HashMap<>();
//            data.put(FIELD_NAME, newFriend.getName());
//            data.put(FIELD_PHONE, newFriend.getPhoneNumber());
//            data.put(FIELD_BIRTHDATE, newFriend.getBirthdate());

            //create subcollections containing documents
            DB.collection(COLLECTION_USERS)
                    .document(loggedInUserEmail)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: Document Added successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Error while creating document " + e.getLocalizedMessage());
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, "addFriend: " + ex.getLocalizedMessage());
        }
    }
}
