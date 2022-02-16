package com.tsaravan9.myconciergeandroid.views.resident;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityBookAmenityBinding;
import com.tsaravan9.myconciergeandroid.models.Booking;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.ui.home.HomeFragment;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BookAmenityActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

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
    private int pos;
    private int sendSlot;
    private String amenity;
    private String[] arrlist = {"6:00 AM-8:00 AM","8:00 AM-10:00 AM","10:00 AM-12:00 PM","12:00 PM-02:00 PM", "02:00 PM-04:00 PM", "04:00 PM-06:00 PM", "06:00 PM-08:00 PM", "08:00 PM-10:00 PM"};

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

        this.binding.calendarView.setOnDateChangeListener(this);
        this.binding.bookBtn.setOnClickListener(this);
        this.binding.spinner.setOnItemSelectedListener(this);

        amenity = getIntent().getExtras().getString("AMENITY_NAME");

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

    private void makeBooking(String date, Integer slot) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        String bookedFor = date;
        Booking booking = new Booking(currentAmenity, slot, resident.getFirstname() + " " + resident.getLastname(),
                resident.getApartment(), System.currentTimeMillis(), bookedFor);
        usersViewModel.makeBooking(booking);
        Intent intent = new Intent(BookAmenityActivity.this, QRActivity.class);
        Long bookedAt = booking.getBookedAt();
        Date date2 = new Date(bookedAt);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        String str = booking.getAmenityName() + booking.getSlot() + booking.getBookedFor() + booking.getResident() + booking.getApartment() + format.format(date2);
        intent.putExtra("INFO", str);
        intent.putExtra("AMENITY_NAME", amenity);
        intent.putExtra("AMENITY_DATE", date);
        intent.putExtra("AMENITY_TIME", pos);
        startActivity(intent);
    }

    private void book(){
        usersViewModel.searchUserByEmail(loggedInUserEmail);
        this.usersDBRepository.userFromDB.observe(BookAmenityActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                resident = user;
                usersDBRepository.currentBuilding = resident.getAddress();
                //temporary hardcoding
                usersViewModel.getBookings(amenity);
                usersViewModel.allBookings.observe(BookAmenityActivity.this, new Observer<List<Booking>>() {
                    @Override
                    public void onChanged(List<Booking> bookings) {
                        prepareEarliest(false);
                        prepareAvailableSlots(bookings);
                        try {
                            test(amenity);
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
                    makeBooking(localDate.toString(), obj);
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

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        String prepareDate = (year+"/"+month+1+"/"+dayOfMonth);
        date = prepareDate;

        if(availableSlots.containsKey(prepareDate)) {
            HashSet<Integer> newSlots = availableSlots.get(prepareDate);
            Log.d("integer", newSlots.toString());
        } else {
            Log.d("integer", "haha");
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrlist );
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.bookBtn: {
                    try {
                        makeBooking(date, sendSlot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("a", position + "");
        pos = position;
        switch (pos) {
            case 0: {
                sendSlot = 6;
                break;
            }
            case 1: {
                sendSlot = 8;
                break;
            }
            case 2: {
                sendSlot = 10;
                break;
            }
            case 3: {
                sendSlot = 12;
                break;
            }
            case 4: {
                sendSlot = 14;
                break;
            }
            case 5: {
                sendSlot = 16;
                break;
            }
            case 6: {
                sendSlot = 18;
                break;
            }
            case 7: {
                sendSlot = 20;
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}