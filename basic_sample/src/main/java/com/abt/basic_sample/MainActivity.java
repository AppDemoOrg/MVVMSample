package com.abt.basic_sample;

import android.databinding.BaseObservable;
import android.support.annotation.NonNull;

import com.abt.basic.arch.mvvm.BaseActivity;
import com.abt.basic.arch.mvvm.BaseFragment;

public class MainActivity extends BaseActivity {

    @NonNull
    @Override
    protected BaseFragment createFragment() {
        return MainFragment.newInstance();
    }

    @NonNull
    @Override
    protected BaseObservable createViewModel() {
        return new MainViewModel();
    }
}
