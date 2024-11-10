package com.bkimp.life.ui.tutorial;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TutorialViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TutorialViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notification fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}