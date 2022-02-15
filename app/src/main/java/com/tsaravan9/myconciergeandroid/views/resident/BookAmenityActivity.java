package com.tsaravan9.myconciergeandroid.views.resident;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.tsaravan9.myconciergeandroid.databinding.ActivityBookAmenityBinding;
import com.tsaravan9.myconciergeandroid.models.Booking;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BookAmenityActivity extends AppCompatActivity {

    private ActivityBookAmenityBinding binding;
    private String currentAmenity;
    private UsersViewModel usersViewModel;
    private UsersDBRepository usersDBRepository;
    private HashMap<String, HashSet<Integer>> availableSlots = new HashMap<>();
    private String date;
    private String loggedInUserEmail;
    private User resident;
    private SharedPreferences prefs;
    private String earliestSlot;
    private String currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityBookAmenityBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();
        date = dtf.format(localDate);
        prefs = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);
        loggedInUserEmail = prefs.getString("USER_EMAIL", "");

        usersViewModel = UsersViewModel.getInstance(this.getApplication());
        usersDBRepository = usersViewModel.getUserRepository();
        book();


    }

    private void prepareAvailableSlots(List<Booking> bookings){
        HashSet<Integer> slots = new HashSet<>();
        Integer slot = 6;
        for (int i = 1; i<=8; i++){
            slots.add(slot);
            slot = slot + 2;
        }
        Log.d("available1", slots.toString());
        for (Booking booking : bookings){
            if (date.equals(booking.getBookedFor())){
                slots.remove(booking.getSlot());
            }
        }

        ArrayList<Integer> temp = new ArrayList<>();
        for (Integer i : slots){
            if (i < Integer.parseInt(earliestSlot)){
                temp.add(i);
            }
        }
        slots.removeAll(temp);
        availableSlots.put(date, slots);
    }

    private void makeBooking(LocalDate date, Integer slot) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        String bookedFor = dtf.format(date);
        Booking booking = new Booking(currentAmenity, slot, resident.getFirstname() + " " + resident.getLastname(),
                resident.getApartment(), System.currentTimeMillis(), bookedFor);
        usersViewModel.makeBooking(booking);
    }

    private void book(){
        usersViewModel.searchUserByEmail(loggedInUserEmail);
        this.usersDBRepository.userFromDB.observe(BookAmenityActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                resident = user;
                usersDBRepository.currentBuilding = resident.getAddress();
                //temporary hardcoding
                usersViewModel.getBookings("Pool Room");
                usersViewModel.allBookings.observe(BookAmenityActivity.this, new Observer<List<Booking>>() {
                    @Override
                    public void onChanged(List<Booking> bookings) {
                        prepareEarliest(false);
                        prepareAvailableSlots(bookings);
                        try {
                            test("Pool Room");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //temporary method
    private void test(String currentAmenity2) throws Exception {
        currentAmenity = currentAmenity2;
        Log.d("available2", availableSlots.toString());
        HashSet<Integer> set = availableSlots.get("2022/02/15");
        Log.d("available3", set.toString());
        //chosen slot - hardcoded right now
        if (set.contains(12)) {
            for (Integer obj : set) {
                if (obj.equals(12)){
                    LocalDate localDate = LocalDate.now();
                    makeBooking(localDate, obj);
                }
            }
        }
    }

    //flag is temporary for the sake of test
    private void prepareEarliest(Boolean flag){
        if (flag){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDate localDate = LocalDate.now();
            currentTime = dtf.format(localDate);
            int currentSlot = Integer.parseInt(currentTime.substring(0,2));
            if (!(currentSlot >= 8 && currentSlot < 10)){
                if (currentSlot%2 == 0){
                    earliestSlot = (currentSlot + 2) + "";
                }
                else{
                    earliestSlot = (currentSlot + 1) + "";
                }
            }
        }
        else{
            //temporary - manual
            earliestSlot = 12 + "";
        }

    }




}