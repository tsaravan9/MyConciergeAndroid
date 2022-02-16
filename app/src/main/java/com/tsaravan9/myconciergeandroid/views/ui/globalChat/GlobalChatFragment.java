package com.tsaravan9.myconciergeandroid.views.ui.globalChat;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.FragmentAcceptRejectBinding;
import com.tsaravan9.myconciergeandroid.databinding.FragmentGlobalChatBinding;
import com.tsaravan9.myconciergeandroid.models.Text;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.GlobalChatActivity;
import com.tsaravan9.myconciergeandroid.views.ui.acceptReject.AcceptRejectViewModel;

import java.util.ArrayList;
import java.util.List;

public class GlobalChatFragment extends Fragment implements View.OnClickListener {

    private FragmentGlobalChatBinding binding;
    private UsersViewModel usersViewModel;
    private List<Text> texts2 = new ArrayList<>();
    private Activity context;
    private User loggedInUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGlobalChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = this.getActivity();
        usersViewModel = UsersViewModel.getInstance(context.getApplication());
        loggedInUser = usersViewModel.getUserRepository().loggedInUser;
//        usersDBRepository = usersViewModel.getUserRepository();

        usersViewModel.getAllTexts();
        usersViewModel.allTexts.observe(getViewLifecycleOwner(), new Observer<List<Text>>() {
            @Override
            public void onChanged(List<Text> texts) {
                //update RecyclerView Later
                //Toast.makeText(MainActivity.this, "On Changed", Toast.LENGTH_SHORT).show();
                binding.tvChat.setText("");
                if (texts != null) {
//                    setTexts(texts);
                    Log.d("countHere", "" + texts.size());
                    texts2 = texts;
                    prepareChat();
                    binding.pbLoading.setVisibility(View.GONE);
                    binding.llcGlobalChat.setVisibility(View.VISIBLE);
                }
            }
        });
        this.binding.btnSend.setOnClickListener(this);
//        this.prepareChat();

        return root;
    }

    private void validate() throws Exception {
        Boolean validData = true;
        String text = "";

        if (this.binding.etSendMsg.getText().toString().isEmpty()) {
            validData = false;
            this.binding.etSendMsg.setError("Text Cannot Be Empty");
        } else {
            text = this.binding.etSendMsg.getText().toString();
        }

        if (validData) {
            Text newText = new Text(loggedInUser.getFirstname() + " " + loggedInUser.getLastname(),
                    text, System.currentTimeMillis());
            this.usersViewModel.addTextToChat(newText);
            Toast.makeText(this.getContext(), "Message Sent!", Toast.LENGTH_SHORT).show();
            this.binding.etSendMsg.setText("");
        } else {
            Toast.makeText(this.getContext(), "Please provide correct inputs", Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareChat() {
        if (texts2 != null) {
            this.sort(this.texts2);
            String chat = "\n";
            for (Text text : texts2) {
                chat = prepareText(chat, text);
            }
            this.binding.tvChat.setText(chat);
        }
    }

    private void sort(List<Text> texts) {
        texts.sort((o1, o2)
                -> o1.getSentAt().compareTo(
                o2.getSentAt()));
    }

    private String prepareText(String chat, Text text) {
        chat = chat + "\n" + text.getSender() + "\n";
        int size = text.getMessage().length();
        for (int i = 0; i < size; i++) {
            if (i == 30) {
                chat = chat + "\n";
            }
            chat = chat + text.getMessage().charAt(i);
        }
        return chat + "\n";
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btn_send: {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}