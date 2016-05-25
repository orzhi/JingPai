package com.xcode.lockcapture.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import java.lang.ref.WeakReference;

/**
 * ViewPager通用适配器，提供了弱引用缓存，提供fragment的复用
 * 使用方式{@link #getFragmentItem(int)}代替{@link #getItem(int)}即可
 * <p/>
 * 使用查看指定位置的Fragment{@link #getMyFragment(int)}
 * 如果不确定找指定的Fragment是否存在,使用此方法{@link #getItem(int)} (int)}
 * 适配器自动托管弱引用管理
 * Created by 陈俊杰 on 2016/1/4.
 */
public abstract class XLFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private SparseArray<WeakReference<T>> mFragmentList;

    /**
     * 如果在activity，使用getSupportFragmentManager();
     * 如果在fragment，使用 getChildFragmentManager();
     *
     * @param fm
     */
    public XLFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public final T getItem(int position) {
        return getFragmentEnsureExist(position);
    }

    protected abstract T getFragmentItem(int position);

    /**
     * 获取对应的Fragment
     *
     * @param position
     * @return
     */
    public T getMyFragment(int position) {
        if (mFragmentList == null)
            return null;

        if (mFragmentList.get(position) == null)
            return null;

        return mFragmentList.get(position).get();
    }

    /**
     * 如果不确定找指定的Fragment是否存在,使用此方法
     * 如果找不到fragment，会创建一个并放在弱引用内存中
     *
     * @param position
     * @return
     */
    private T getFragmentEnsureExist(int position) {
        if (mFragmentList == null) {
            int tabCount = getCount();
            mFragmentList = new SparseArray<>(tabCount > 0 ? tabCount : 6);
        }

        if (mFragmentList.get(position) == null || mFragmentList.get(position).get() == null) {
            T fragment = getFragmentItem(position);
            mFragmentList.put(position, new WeakReference<>(fragment));
            return fragment;
        }

        return mFragmentList.get(position).get();
    }
}
