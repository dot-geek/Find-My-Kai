package com.positivestuff.findmykai.ui.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutViewModel  extends ViewModel {

    private final MutableLiveData<String> mText;

    public AboutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Walnuts and pears you plant for your heirs.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}