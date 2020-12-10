package com.tgsbesar.myapplication.menu_laboratorium;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PdfViewModelLab extends ViewModel {
    private MutableLiveData<String> mText;

    public PdfViewModelLab() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pdf fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
