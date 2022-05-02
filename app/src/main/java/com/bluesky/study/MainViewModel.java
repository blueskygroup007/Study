package com.bluesky.study;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bluesky.study.bean.Sence;

/**
 * @author BlueSky
 * @date 2022/3/26
 * Description:
 */
public class MainViewModel extends ViewModel {
    //屏幕旋转状态
    private MutableLiveData<Integer> orientation;
    private MutableLiveData<Sence> sence;
    //强制旋转标志位
    private MutableLiveData<Integer> orientationForce;

    //屏幕旋转相关标志位

    public MainViewModel() {
        sence = new MutableLiveData<>();
        orientation = new MediatorLiveData<>();
        orientationForce = new MediatorLiveData<>();
        orientationForce.setValue(0);
    }

    public MutableLiveData<Integer> getOrientation() {
        return orientation;
    }

    public MutableLiveData<Integer> getOrientationForce() {
        return orientationForce;
    }

    public MutableLiveData<Sence> getSence() {
        return sence;
    }

}
