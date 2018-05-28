package com.abt.basic_sample;

import com.abt.basic.arch.mvvm.BaseFragment;
import com.abt.basic.arch.mvvm.ToolbarViewModel;

/**
 * @描述： @MainFragment
 * @作者： @黄卫旗
 * @创建时间： @2018/5/28
 */
public class MainFragment extends BaseFragment<MainViewModel, ToolbarViewModel> {

    /**
     * 返回实例
     */
    public static MainFragment newInstance() {
        return new MainFragment();
    }

}
