package com.xcode.lockcapture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.xcode.lockcapture.album.LocalImageManager;
import com.xcode.lockcapture.common.IFragment;
import com.xcode.lockcapture.common.XLFragmentPagerAdapter;
import com.xcode.lockcapture.fragment.AboutMe;
import com.xcode.lockcapture.fragment.AlbumExplore;
import com.xcode.lockcapture.fragment.AppIntroduce;
import com.xcode.lockcapture.fragment.CaptureStatus;
import com.xcode.lockcapture.tool.DisplayUtil;
import com.xcode.lockcapture.widget.XLTabLayout;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private XLTabLayout mTabLayout;
    private IFragment mIFragment;
    private XLFragmentPagerAdapter<Fragment> mFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayUtil.init(this);
        mViewPager = (ViewPager) findViewById(R.id.main_fragment_container);
        mFragmentPagerAdapter = new XLFragmentPagerAdapter<Fragment>(getSupportFragmentManager()) {
            @Override
            protected Fragment getFragmentItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new CaptureStatus();
                        break;
                    case 1:
                        fragment = new AlbumExplore();
                        break;
                    case 2:
                        fragment = new AppIntroduce();
                        break;
                    case 3:
                        fragment = new AboutMe();
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String result = null;

                switch (position) {
                    case 0:
                        result = "拍照";
                        break;
                    case 1:
                        result = "相册";
                        break;
                    case 2:
                        result = "说明书";
                        break;
                    case 3:
                        result = "关于";
                        break;
                }
                return result;
            }
        };
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mTabLayout = (XLTabLayout) findViewById(R.id.main_tabView);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIFragment = ((IFragment) mFragmentPagerAdapter.getMyFragment(position));

                if (mIFragment != null)
                    mIFragment.OnEnter();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AlbumExplore.REQUEST_CODE_VIEW_ALBUM: {
                if (LocalImageManager.GetInstance().RefreshImageList())
                    mIFragment.OnEnter();
            }
            break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
