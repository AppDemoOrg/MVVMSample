package com.abt.basic.arch.mvvm;

import android.content.Context;
import android.databinding.BaseObservable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.abt.basic.R;
import com.abt.basic.app.BasicApplication;
import com.abt.basic.permission.PermissionHolder;
import com.orhanobut.logger.Logger;

/**
 * @描述：     @MVVM Activity基类
 * @作者：     @黄卫旗
 * @创建时间： @21/05/2018
 */
public abstract class BaseActivity<VM extends BaseObservable & IViewModel, V extends BaseFragment<VM, TM>, TM>
        extends AppCompatActivity {
    public String TAG = "";
    protected String SIMPLE_TAG;

    protected static final String FRAGMENT_TAG_VIEW_MODEL = "FRAGMENT_TAG_VIEW_MODEL";
    public static final int REQUEST_PERMISSIONS = 1;

    protected VM mViewModel;
    protected TM mToolbarModel;
    protected SensorManager manager;
    protected SensorListener listener = new SensorListener();
    private double heading;
    private LocationThread locationThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_common);
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //getWindow().setBackgroundDrawable(null);
        TAG = getClass().getCanonicalName();
        SIMPLE_TAG = getClass().getSimpleName();
        if (enableRequestPermissions()) {
            requestPermissions();
        } else {
            V fragment = findOrCreateViewFragment();
            mViewModel = findOrCreateViewModel();
            mToolbarModel = createToolbarViewModel();
            fragment.setViewModel(mViewModel);
            if (null != mToolbarModel) {
                fragment.setToolbarViewModel(mToolbarModel);
            }
        }
        Logger.e(SIMPLE_TAG, "onCreate");
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private V findOrCreateViewFragment() {
        V fragment = (V) getSupportFragmentManager().findFragmentById(R.id.fl_content);
        if (fragment == null) {
            fragment = createFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_content, fragment)
                    .commitAllowingStateLoss();
        }
        return fragment;
    }

    protected boolean enableRequestPermissions() {
        return false;
    }


    @SuppressWarnings("unchecked")
    @NonNull
    private VM findOrCreateViewModel() {
        ViewModelHolder<VM> retainedViewModel = (ViewModelHolder<VM>) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_TAG_VIEW_MODEL);
        if (retainedViewModel != null && retainedViewModel.getViewModel() != null) {
            // model已经保存，直接返回
            if (this instanceof INavigator) {
                retainedViewModel.getViewModel().setNavigator((INavigator) this);
            }
            return retainedViewModel.getViewModel();
        } else {
            // 还没有ViewModel，创建
            VM viewModel = createViewModel();
            if (this instanceof INavigator) {
                viewModel.setNavigator((INavigator) this);
            }
            // 通过FragmentManager绑定Activity生命周期
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ViewModelHolder.createContainer(viewModel), FRAGMENT_TAG_VIEW_MODEL)
                    .commit();
            return viewModel;
        }
    }

    /**
     * 创建Fragment
     */
    @NonNull
    protected abstract V createFragment();

    /**
     * 创建ViewModel
     */
    @NonNull
    protected abstract VM createViewModel();

    /**
     * 创建ToolbarViewModel
     *
     * @return
     */
    protected TM createToolbarViewModel() {
        return null;
    }

    /**
     * 申请权限
     */
    private final void requestPermissions() {
        PermissionHolder.requestPermissions(this,
                REQUEST_PERMISSIONS,
                getPermissions(),
                new PermissionHolder.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        V fragment = findOrCreateViewFragment();
                        mViewModel = findOrCreateViewModel();
                        mToolbarModel = createToolbarViewModel();
                        if (null != mToolbarModel) {
                            fragment.setToolbarViewModel(mToolbarModel);
                        }
                        fragment.setViewModel(mViewModel);
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        showGotoSettingPermission();
                    }
                });
    }

    protected void showGotoSettingPermission() {

    }

    protected String[] getPermissions() {
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionHolder.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onResume() {
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        manager.registerListener(listener, sensor,
                SensorManager.SENSOR_DELAY_GAME);
        locationThread = new LocationThread();
        locationThread.start();
        super.onResume();
        Logger.e(SIMPLE_TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.e(SIMPLE_TAG, "onStop");
    }

    @Override
    protected void onPause() {
        manager.unregisterListener(listener);
        locationThread.stopLocation();
        super.onPause();
        Logger.e(SIMPLE_TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e(SIMPLE_TAG, "onDestroy");
    }

    public double getHeading() {
        return heading;
    }

    private final class SensorListener implements SensorEventListener {

        public void onSensorChanged(SensorEvent event) {
            heading = event.values[0];// 存放了方向值 90
            BasicApplication.heading = heading;
//            Logger.e("SensorListener",  BasicApplication.heading+"");
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //LocationingManager.getInstance().getLngAndLat();
            //ToastUtil.showToast(BasicApplication.latitude + " " + BasicApplication.longitude);
        }
    };

    private class LocationThread extends Thread {
        private boolean flag = true;

        @Override
        public void run() {
            super.run();
            while (flag) {
                try {
                    handler.sendEmptyMessage(0);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopLocation() {
            flag = false;
        }
    }

}
