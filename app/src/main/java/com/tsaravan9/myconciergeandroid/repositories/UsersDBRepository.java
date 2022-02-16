package com.tsaravan9.myconciergeandroid.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tsaravan9.myconciergeandroid.models.Announcement;
import com.tsaravan9.myconciergeandroid.models.Booking;
import com.tsaravan9.myconciergeandroid.models.Building;
import com.tsaravan9.myconciergeandroid.models.Delivery;
import com.tsaravan9.myconciergeandroid.models.Text;
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
    public String currentResident = "";
    public MutableLiveData<List<Building>> allBuildings = new MutableLiveData<>();
    public MutableLiveData<List<Announcement>> allAnnouncements = new MutableLiveData<>();
    public MutableLiveData<List<User>> allResidents = new MutableLiveData<>();
    public MutableLiveData<List<String>> allBuildingList = new MutableLiveData<>();
    public MutableLiveData<List<Text>> allTexts = new MutableLiveData<>();
    public MutableLiveData<List<Delivery>> allDeliveries = new MutableLiveData<>();
    public MutableLiveData<List<Booking>> allBookings = new MutableLiveData<>();
    public MutableLiveData<User> userFromDB = new MutableLiveData<>();
    public User loggedInUser = new User();


    private final String COLLECTION_BUILDINGS = "Buildings";
    private final String COLLECTION_CHAT = "Chat";
    private final String COLLECTION_PACKAGES = "Packages";
    private final String COLLECTION_ANNOUNCEMENTS = "Announcements";
    private final String FIELD_ADMIN = "admin";
    private final String FIELD_ADDRESS = "address";
    private final String FIELD_EMAIL = "email";


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
            data.put("mobileNumber", newUser.getMobileNumber());
            data.put("admin", newUser.getAdmin());
            data.put("address", newUser.getAddress());
            data.put("apartment", newUser.getApartment());

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

    public void getAllBuildingsList() throws Exception {
        try {
            DB.collection(COLLECTION_BUILDINGS)
                    .orderBy(FIELD_ADDRESS, Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e(TAG, "onEvent: Unable to get document changes " + error);
                                return;
                            }

                            List<String> buildingList = new ArrayList<>();

                            if (snapshot != null) {
                                Log.d(TAG, "onEvent: Current Changes " + snapshot.getDocumentChanges());

                                for (DocumentChange documentChange : snapshot.getDocumentChanges()) {

                                    Building currentBuilding = documentChange.getDocument().toObject(Building.class);
                                    currentBuilding.setId(documentChange.getDocument().getId());
                                    Log.d(TAG, "onEvent: currentUser : " + currentBuilding.toString());

                                    switch (documentChange.getType()) {
                                        case ADDED:
                                            buildingList.add(currentBuilding.getAddress());
                                            break;
                                        case REMOVED:
                                            buildingList.remove(currentBuilding.getAddress());
                                            break;
                                    }
                                }

                                allBuildingList.postValue(buildingList);
                            } else {
                                Log.e(TAG, "onEvent: No changes received");
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "getAllBuildings: Exception occured " + e.getLocalizedMessage());
            Log.e(TAG, String.valueOf(e.getStackTrace()));
        }
    }

    public void getAllBuildings() {
        try {
            DB.collection(COLLECTION_BUILDINGS)
                    .whereEqualTo(FIELD_ADMIN, loggedInUserEmail)
                    //.orderBy(FIELD_BUILDING_NAME, Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e(TAG, "onEvent: Unable to get document changes " + error);
                                return;
                            }

                            List<Building> buildingList = new ArrayList<>();

                            if (snapshot != null) {
                                Log.d(TAG, "onEvent: Current Changes " + snapshot.getDocumentChanges());

                                for (DocumentChange documentChange : snapshot.getDocumentChanges()) {

                                    Building currentBuilding = documentChange.getDocument().toObject(Building.class);
                                    currentBuilding.setId(documentChange.getDocument().getId());
                                    Log.d(TAG, "onEvent: currentUser : " + currentBuilding.toString());

                                    switch (documentChange.getType()) {
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

                            } else {
                                Log.e(TAG, "onEvent: No changes received");
                            }
                        }
                    });


        } catch (Exception ex) {
            Log.e(TAG, "getAllFriends: Exception occured " + ex.getLocalizedMessage());
            Log.e(TAG, String.valueOf(ex.getStackTrace()));
        }
    }

    public void getAllResidents() {
        try {
            DB.collection(COLLECTION_USERS)
                    .whereEqualTo(FIELD_ADDRESS, currentBuilding)
                    //.orderBy(FIELD_BUILDING_NAME, Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e(TAG, "onEvent: Unable to get document changes " + error);
                                return;
                            }

                            List<User> residentList = new ArrayList<>();

                            if (snapshot != null) {
                                Log.d(TAG, "onEvent: Current Changes " + snapshot.getDocumentChanges());

                                for (DocumentChange documentChange : snapshot.getDocumentChanges()) {

                                    User currentUser = documentChange.getDocument().toObject(User.class);
                                    //currentUser.setId(documentChange.getDocument().getId());
                                    Log.d(TAG, "onEvent: currentUser : " + currentUser.toString());

                                    switch (documentChange.getType()) {
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

                            } else {
                                Log.e(TAG, "onEvent: No changes received");
                            }
                        }
                    });

        } catch (Exception ex) {
            Log.e(TAG, "getAllFriends: Exception occured " + ex.getLocalizedMessage());
            Log.e(TAG, String.valueOf(ex.getStackTrace()));
        }
    }

    public void addPackage(Delivery newPackage) throws Exception {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("name", newPackage.getName());
            data.put("description", newPackage.getDescription());
            data.put("visitor", newPackage.getVisitor());
            data.put("allowed", newPackage.isAllowed());
            data.put("rejected", newPackage.isRejected());
            data.put("enteredAt", newPackage.getEnteredAt());
            Log.d("allowed", data.get("allowed") + "");

            DB.collection(COLLECTION_USERS)
                    .document(currentResident)
                    .collection(COLLECTION_PACKAGES)
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference reference) {
                            Log.d(TAG, "onSuccess: Document Added successfully with ID : " + reference.getId());
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

    public void addAnnouncement(Announcement newAnnouncement) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("title", newAnnouncement.getTitle());
            data.put("description", newAnnouncement.getDescription());
            data.put("postedAt", newAnnouncement.getPostedAt());

            DB.collection(COLLECTION_BUILDINGS)
                    .whereEqualTo(FIELD_ADDRESS, currentBuilding)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().size() != 0) {
                                    DB.collection(COLLECTION_BUILDINGS)
                                            .document(task.getResult().getDocuments().get(0).getId())
                                            .collection(COLLECTION_ANNOUNCEMENTS)
                                            .add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d("Done", "Announcement Posted");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("Failed", "Announcement Not Posted");
                                                }
                                            });
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Failed", "Document Not Found");
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, "addFriend: " + ex.getLocalizedMessage());
        }
    }

    public void addTextToChat(Text newText) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("sender", newText.getSender());
            data.put("message", newText.getMessage());
            data.put("sentAt", newText.getSentAt());

            DB.collection(COLLECTION_BUILDINGS)
                    .whereEqualTo(FIELD_ADDRESS, currentBuilding)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().size() != 0) {
                                    DB.collection(COLLECTION_BUILDINGS)
                                            .document(task.getResult().getDocuments().get(0).getId())
                                            .collection(COLLECTION_CHAT)
                                            .add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d("Done", "Message Sent");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("Failed", "Message Not Sent");
                                                }
                                            });
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Failed", "Document Not Found");
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, "addFriend: " + ex.getLocalizedMessage());
        }
    }

    public void getAllTexts() {
        try {
            DB.collection(COLLECTION_BUILDINGS)
                    .whereEqualTo(FIELD_ADDRESS, currentBuilding)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().size() != 0) {
                                    DB.collection(COLLECTION_BUILDINGS)
                                            .document(task.getResult().getDocuments().get(0).getId())
                                            .collection(COLLECTION_CHAT)
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                                                    if (error != null) {
                                                        Log.e(TAG, "onEvent: Unable to get document changes " + error);
                                                        return;
                                                    }

                                                    List<Text> textList = new ArrayList<>();

                                                    if (snapshot != null) {
                                                        Log.d(TAG, "onEvent: Current Changes " + snapshot.getDocumentChanges());

                                                        for (DocumentChange documentChange : snapshot.getDocumentChanges()) {

                                                            Text currentText = documentChange.getDocument().toObject(Text.class);
                                                            Log.d(TAG, "fefefefrrrr" + currentText.toString());

                                                            switch (documentChange.getType()) {
                                                                case ADDED:
                                                                    textList.add(currentText);
                                                                    break;
                                                                case MODIFIED:
                                                                    textList.add(currentText);
                                                                    //TODO - search in friendList for existing object and replace it with new one - currentFriend
                                                                    break;
                                                                case REMOVED:
                                                                    textList.remove(currentText);
                                                                    break;
                                                            }
                                                        }

                                                        allTexts.postValue(textList);

                                                    } else {
                                                        Log.e(TAG, "onEvent: No changes received");
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Failed", "Document Not Found");
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, "addFriend: " + ex.getLocalizedMessage());
        }
    }

    public void searchUserByEmail(String email) {
        try {
            DB.collection(COLLECTION_USERS).whereEqualTo(FIELD_EMAIL, email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().size() != 0) {
                                    User matchedUser = task.getResult().getDocuments().get(0).toObject(User.class);

                                    if (matchedUser != null) {
                                        Log.d("hey", matchedUser.getFirstname());
                                        userFromDB.postValue(matchedUser);
                                        loggedInUser = matchedUser;
                                    } else {
                                        Log.e(TAG, "onComplete: Unable to convert the matching document to Friend object");
                                    }

                                } else {
                                    //no friend with given name
                                    Log.e(TAG, "onComplete: No friend with give name found");
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, "searchFriendByName: Exception occured " + ex.getLocalizedMessage());
        }
    }

    public void getAllAnnouncements() {
        try {
            DB.collection(COLLECTION_BUILDINGS)
                    .whereEqualTo(FIELD_ADDRESS, currentBuilding)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().size() != 0) {
                                    DB.collection(COLLECTION_BUILDINGS)
                                            .document(task.getResult().getDocuments().get(0).getId())
                                            .collection(COLLECTION_ANNOUNCEMENTS)
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                                                    if (error != null) {
                                                        Log.e(TAG, "onEvent: Unable to get document changes " + error);
                                                        return;
                                                    }

                                                    List<Announcement> announcementList = new ArrayList<>();

                                                    if (snapshot != null) {
                                                        Log.d(TAG, "onEvent: Current Changes " + snapshot.getDocumentChanges());

                                                        for (DocumentChange documentChange : snapshot.getDocumentChanges()) {

                                                            Announcement currentAnnouncement = documentChange.getDocument().toObject(Announcement.class);
                                                            switch (documentChange.getType()) {
                                                                case ADDED:
                                                                    announcementList.add(currentAnnouncement);
                                                                    break;
                                                                case MODIFIED:
                                                                    announcementList.add(currentAnnouncement);
                                                                    //TODO - search in friendList for existing object and replace it with new one - currentFriend
                                                                    break;
                                                                case REMOVED:
                                                                    announcementList.remove(currentAnnouncement);
                                                                    break;
                                                            }
                                                        }
                                                        Log.d("UserRepo", announcementList.toString());
                                                        allAnnouncements.postValue(announcementList);

                                                    } else {
                                                        Log.e(TAG, "onEvent: No changes received");
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Failed", "Document Not Found");
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, "addFriend: " + ex.getLocalizedMessage());
        }
    }

    public void getAllDeliveries() {
        try {
            DB.collection(COLLECTION_USERS)
                    .document(loggedInUserEmail)
                    .collection(COLLECTION_PACKAGES)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e(TAG, "onEvent: Unable to get document changes " + error);
                                return;
                            }

                            List<Delivery> deliveryList = new ArrayList<>();

                            if (snapshot != null) {
                                Log.d(TAG, "onEvent: Current Changes " + snapshot.getDocumentChanges());

                                for (DocumentChange documentChange : snapshot.getDocumentChanges()) {

                                    Delivery currentDelivery = documentChange.getDocument().toObject(Delivery.class);
                                    Log.d(TAG, "onEvent: currentUser : " + currentDelivery.toString());

                                    switch (documentChange.getType()) {
                                        case ADDED:
                                            deliveryList.add(currentDelivery);
                                            break;
                                        case MODIFIED:
                                            //TODO - search in friendList for existing object and replace it with new one - currentFriend
                                            break;
                                        case REMOVED:
                                            deliveryList.remove(currentDelivery);
                                            break;
                                    }
                                }

                                allDeliveries.postValue(deliveryList);

                            } else {
                                Log.e(TAG, "onEvent: No changes received");
                            }
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, "addFriend: 123" + ex.getLocalizedMessage());
        }
    }

    public void updateDelivery(Delivery updatedDelivery) {

        Map<String, Object> updatedInfo = new HashMap<>();
        updatedInfo.put("allowed", updatedDelivery.isAllowed());
        updatedInfo.put("rejected", updatedDelivery.isRejected());

        try {
            DB.collection(COLLECTION_USERS)
                    .document(loggedInUserEmail)
                    .collection(COLLECTION_PACKAGES)
                    .whereEqualTo("enteredAt", updatedDelivery.getEnteredAt())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().size() != 0) {
                                    DB.collection(COLLECTION_USERS)
                                            .document(loggedInUserEmail)
                                            .collection(COLLECTION_PACKAGES)
                                            .document(task.getResult().getDocuments().get(0).getId())
                                            .update(updatedInfo)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "onSuccess: Document successfully updated");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "onFailure: Unable to update document" + e.getLocalizedMessage());
                                                }
                                            });
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Failed", "Document Not Found");
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, "updateFriend: Exception occured " + ex.getLocalizedMessage());
        }
    }

    public void makeBooking(Booking newBooking){
        try{
            Map<String, Object> data = new HashMap<>();
            data.put("bookedAt", newBooking.getBookedAt());
            data.put("bookedFor", newBooking.getBookedFor());
            data.put("resident", newBooking.getResident());
            data.put("apartment", newBooking.getApartment());
            data.put("slot", newBooking.getSlot());

            DB.collection(COLLECTION_BUILDINGS)
                    .whereEqualTo(FIELD_ADDRESS, currentBuilding)
                    .get()
                    //.orderBy(FIELD_BUILDING_NAME, Query.Direction.ASCENDING)
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().size() != 0) {
                                    DB.collection(COLLECTION_BUILDINGS)
                                            .document(task.getResult().getDocuments().get(0).getId())
                                            .collection(newBooking.getAmenityName())
                                            .add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference reference) {
                                                    Log.d(TAG, "onSuccess: Document Added successfully with ID : " + reference.getId());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "onFailure: Error while creating document " + e.getLocalizedMessage() );
                                                }
                                            });
                                }
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, "addFriend: " + ex.getLocalizedMessage());
        }
    }

    public void getBookings(String amenityName) {
        try {
            DB.collection(COLLECTION_BUILDINGS)
                    .whereEqualTo(FIELD_ADDRESS, currentBuilding)
                    .get()
                    //.orderBy(FIELD_BUILDING_NAME, Query.Direction.ASCENDING)
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().size() != 0) {
                                    DB.collection(COLLECTION_BUILDINGS)
                                            .document(task.getResult().getDocuments().get(0).getId())
                                            .collection(amenityName)
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                                                    if (error != null) {
                                                        Log.e(TAG, "onEvent: Unable to get document changes " + error);
                                                        return;
                                                    }

                                                    List<Booking> bookingList = new ArrayList<>();

                                                    if (snapshot != null) {
                                                        Log.d(TAG, "onEvent: Current Changes " + snapshot.getDocumentChanges());

                                                        for (DocumentChange documentChange : snapshot.getDocumentChanges()) {

                                                            Booking currentBooking = documentChange.getDocument().toObject(Booking.class);
                                                            Log.d(TAG, "onEvent: currentUser : " + currentBuilding.toString());

                                                            switch (documentChange.getType()) {
                                                                case ADDED:
                                                                    bookingList.add(currentBooking);
                                                                    break;
                                                                case MODIFIED:
                                                                    //TODO - search in friendList for existing object and replace it with new one - currentFriend
                                                                    break;
                                                                case REMOVED:
                                                                    bookingList.remove(currentBooking);
                                                                    break;
                                                            }
                                                        }
                                                        Log.d("woah", "here");
                                                        allBookings.postValue(bookingList);

                                                    } else {
                                                        Log.e(TAG, "onEvent: No changes received");
                                                    }
                                                }
                                            });
                                }
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        } catch (Exception ex) {
            Log.e(TAG, "getAllFriends: Exception occured " + ex.getLocalizedMessage());
            Log.e(TAG, String.valueOf(ex.getStackTrace()));
        }
    }




}
