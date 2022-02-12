package com.tsaravan9.myconciergeandroid.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tsaravan9.myconciergeandroid.models.Building;
import com.tsaravan9.myconciergeandroid.models.User;

import java.util.ArrayList;
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
    public String currentBuilding = "";
    public MutableLiveData<List<Building>> allBuildings = new MutableLiveData<>();
    public MutableLiveData<List<User>> allResidents = new MutableLiveData<>();

    private final String COLLECTION_BUILDINGS = "Buildings";
    private final String FIELD_ADMIN = "admin";
    private final String FIELD_ADDRESS = "address";


//    public MutableLiveData<List<Friend>> allFriends = new MutableLiveData<>();
//    public MutableLiveData<Friend> friendFromDB = new MutableLiveData<>();

    public UsersDBRepository() {
        DB = FirebaseFirestore.getInstance();
    }

    public void addUser(User newUser) throws Exception {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("firstname", newUser.getFirstname());
            data.put("lastname", newUser.getLastname());
            data.put("email", newUser.getEmail());
            data.put("pass", newUser.getPass());
            data.put("mobile", newUser.getMobileNumber());
            data.put("isAdmin", newUser.getAdmin());

            DB.collection(COLLECTION_USERS)
                    .document(newUser.getEmail())
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

    public void getAllBuildings(){
        try{
            DB.collection(COLLECTION_BUILDINGS)
                    .whereEqualTo(FIELD_ADMIN, loggedInUserEmail)
                    //.orderBy(FIELD_BUILDING_NAME, Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            if (error != null){
                                Log.e(TAG, "onEvent: Unable to get document changes " + error );
                                return;
                            }

                            List<Building> buildingList = new ArrayList<>();

                            if (snapshot != null){
                                Log.d(TAG, "onEvent: Current Changes " + snapshot.getDocumentChanges());

                                for (DocumentChange documentChange: snapshot.getDocumentChanges()){

                                    Building currentBuilding = documentChange.getDocument().toObject(Building.class);
                                    currentBuilding.setId(documentChange.getDocument().getId());
                                    Log.d(TAG, "onEvent: currentUser : " + currentBuilding.toString());

                                    switch (documentChange.getType()){
                                        case ADDED:
                                            buildingList.add(currentBuilding);
                                            break;
                                        case MODIFIED:
                                            //TODO - search in friendList for existing object and replace it with new one - currentFriend
                                            break;
                                        case REMOVED:
                                            buildingList.remove(currentBuilding);
                                            break;
                                    }
                                }

                                allBuildings.postValue(buildingList);

                            }else{
                                Log.e(TAG, "onEvent: No changes received");
                            }
                        }
                    });


        }catch(Exception ex){
            Log.e(TAG, "getAllFriends: Exception occured " + ex.getLocalizedMessage() );
            Log.e(TAG, String.valueOf(ex.getStackTrace()));
        }
    }

    public void getAllResidents(){
        try{
            DB.collection(COLLECTION_USERS)
                    .whereEqualTo(FIELD_ADDRESS, currentBuilding)
                    //.orderBy(FIELD_BUILDING_NAME, Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            if (error != null){
                                Log.e(TAG, "onEvent: Unable to get document changes " + error );
                                return;
                            }

                            List<User> residentList = new ArrayList<>();

                            if (snapshot != null){
                                Log.d(TAG, "onEvent: Current Changes " + snapshot.getDocumentChanges());

                                for (DocumentChange documentChange: snapshot.getDocumentChanges()){

                                    User currentUser = documentChange.getDocument().toObject(User.class);
                                    //currentUser.setId(documentChange.getDocument().getId());
                                    Log.d(TAG, "onEvent: currentUser : " + currentUser.toString());

                                    switch (documentChange.getType()){
                                        case ADDED:
                                            residentList.add(currentUser);
                                            break;
                                        case MODIFIED:
                                            //TODO - search in friendList for existing object and replace it with new one - currentFriend
                                            break;
                                        case REMOVED:
                                            residentList.remove(currentUser);
                                            break;
                                    }
                                }

                                allResidents.postValue(residentList);

                            }else{
                                Log.e(TAG, "onEvent: No changes received");
                            }
                        }
                    });


        }catch(Exception ex){
            Log.e(TAG, "getAllFriends: Exception occured " + ex.getLocalizedMessage() );
            Log.e(TAG, String.valueOf(ex.getStackTrace()));
        }
    }


}
