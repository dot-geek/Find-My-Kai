package com.positivestuff.findmykai.ui.surpriseme;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SurpriseMeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SurpriseMeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}