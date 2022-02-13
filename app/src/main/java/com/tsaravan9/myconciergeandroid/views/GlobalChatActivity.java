package com.tsaravan9.myconciergeandroid.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityGlobalChatBinding;
import com.tsaravan9.myconciergeandroid.databinding.ActivityPostAnnouncementsBinding;
import com.tsaravan9.myconciergeandroid.models.Announcement;
import com.tsaravan9.myconciergeandroid.models.Building;
import com.tsaravan9.myconciergeandroid.models.Text;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalChatActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityGlobalChatBinding binding;
    private UsersViewModel usersViewModel;
    private User matchedUser;
    private List<Text> texts2 = new ArrayList<>();
    private UsersDBRepository usersDBRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityGlobalChatBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        usersViewModel = UsersViewModel.getInstance(this.getApplication());
        usersDBRepository = usersViewModel.getUserRepository();
        this.searchUserByEmail();

        usersViewModel.getAllTexts();
        usersViewModel.allTexts.observe(this, new Observer<List<Text>>() {
            @Override
            public void onChanged(List<Text> texts) {
                //update RecyclerView Later
                //Toast.makeText(MainActivity.this, "On Changed", Toast.LENGTH_SHORT).show();
                if (texts != null){
//                    setTexts(texts);
                    Log.d("countHere",""+texts.size());
                    if (texts2 == null){
                        texts2 = texts;
                    }
                    else{
                        texts2.addAll(texts);
                    }
                    prepareChat();
                }
            }
        });
        this.binding.btnSend.setOnClickListener(this);
//        this.prepareChat();
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btn_send:{
                    try {
                        this.validate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private void validate() throws Exception {
        Boolean validData = true;
        String text = "";

        if (this.binding.editText.getText().toString().isEmpty()){
            validData = false;
            this.binding.editText.setError("Text Cannot Be Empty");
        }
        else{
            text = this.binding.editText.getText().toString();
        }

        if (validData){
            Text newText = new Text(matchedUser.getFirstname() + " " + matchedUser.getLastname(),
                    text, System.currentTimeMillis());
            this.usersViewModel.addTextToChat(newText);
            Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
            Log.d("chatttt", this.binding.tvChat.getText().toString());
//            texts2.add(newText);
//            String chat = this.prepareText(this.binding.tvChat.getText().toString(), newText);
//            this.binding.tvChat.setText(chat);
            this.binding.editText.setText("");
        }
        else{
            Toast.makeText(this, "Please provide correct inputs", Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareChat(){
        if (texts2 != null){
            this.sort(this.texts2);
            String chat = "\n";
            for (Text text : texts2){
                chat = prepareText(chat, text);
            }
            this.binding.tvChat.setText(chat);
        }
    }

    private void sort(List<Text> texts){
        texts.sort((o1, o2)
                -> o1.getSentAt().compareTo(
                o2.getSentAt()));
    }

    private String prepareText(String chat, Text text){
        chat = chat + "\n" + text.getSender() + "\n";
        int size = text.getMessage().length();
        for (int i = 0; i<size; i++){
            if (i == 30){
                chat = chat + "\n";
            }
            chat = chat + text.getMessage().charAt(i);
        }
        return chat + "\n";
    }

    private void searchUserByEmail(){
        usersViewModel.searchUserByEmail(usersDBRepository.loggedInUserEmail);
        this.usersDBRepository.userFromDB.observe(GlobalChatActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                matchedUser = user;
            }
        });
    }

    private void setTexts(List<Text> texts){
        this.texts2 = texts;
    }


}