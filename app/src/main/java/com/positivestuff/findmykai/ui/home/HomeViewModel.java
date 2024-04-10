package com.positivestuff.findmykai.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final List<String> mTags;

    public HomeViewModel() {
        mTags = new ArrayList<>();
    }

    public List<String> getTags() {
        return mTags;
    }

    public void addTag(String newTag) {
        mTags.add(newTag);
    }

    public void clearTags() {
        mTags.clear();
    }
}