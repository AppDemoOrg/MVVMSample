package com.abt.basic_sample;

import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.abt.basic.arch.mvvm.BaseActivity;
import com.abt.basic.arch.mvvm.BaseFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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
