package com.h.itservices.ui.QustAns_Page;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class qansewrPageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public qansewrPageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is QA fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}