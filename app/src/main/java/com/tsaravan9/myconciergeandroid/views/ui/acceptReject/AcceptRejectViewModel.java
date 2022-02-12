package com.tsaravan9.myconciergeandroid.views.ui.acceptReject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AcceptRejectViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AcceptRejectViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}