package com.bluesky.study.ui.gallery;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bluesky.study.bean.Sence;
import com.bluesky.study.utils.PreferenceUtils;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleryViewModel extends ViewModel {

    private  MutableLiveData<List<Sence>> mSences;
    private  MutableLiveData<Integer> mSencesCount;

    public GalleryViewModel() {
        mSencesCount = new MutableLiveData<>();
        mSences = new MutableLiveData<>();
    }

    public void getAllSences() {
        int sence_count = PreferenceUtils.getInt("sence_count", 0);
        List<Sence> list = new ArrayList<>();
        for (int i = 0; i < sence_count; i++) {
            String key = "sence_" + (i + 1);
            String sence_path = PreferenceUtils.getString(key, "");
            Uri uri = Uri.fromFile(new File(sence_path));
            Sence sence = new Sence(uri.getLastPathSegment(), uri.getPath());
            list.add(sence);
        }
        mSences.setValue(list);
        mSencesCount.setValue(sence_count);
    }


    public MutableLiveData<List<Sence>> getSences() {
        if (mSences==null){
            mSences=new MutableLiveData<>();
        }
        if (mSences.getValue()==null){
            getAllSences();
        }
        return mSences;
    }

    public void setSences(MutableLiveData<List<Sence>> sences) {
        mSences = sences;
    }

    public MutableLiveData<Integer> getSencesCount() {
        return mSencesCount;
    }

    public void setSencesCount(MutableLiveData<Integer> sencesCount) {
        mSencesCount = sencesCount;
    }

    public boolean addSence(String filePath) {
        int senceCount = PreferenceUtils.getInt("sence_count", 0);
        String key = "sence_" + (senceCount + 1);
        Sence senceNew = new Sence(new File(filePath).getName(), filePath);

        //检查Sence是否重复,Sence类必须重写equals方法
        if (Objects.requireNonNull(mSences.getValue()).contains(senceNew)) {
            return false;
        }
        PreferenceUtils.putString(key, filePath);
        PreferenceUtils.putInt("sence_count", senceCount + 1);
        mSences.getValue().add(senceNew);
        mSencesCount.setValue(senceCount);
        return true;
    }
}