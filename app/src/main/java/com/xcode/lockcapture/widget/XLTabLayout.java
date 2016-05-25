package com.xcode.lockcapture.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcode.lockcapture.R;
import com.xcode.lockcapture.tool.DisplayUtil;
import com.xcode.lockcapture.tool.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈俊杰 on 2015/8/15.
 * ViewPager指示器
 * 使用方式{@link #setupWithViewPager(ViewPager)}
 * 采用固定大小（6个tab内最佳），如果扩展支持6个以上滑动的tab，参考 http://blog.csdn.net/lmj623565791/article/details/42160391
 */
public class XLTabLayout extends LinearLayout {

    /**
     * 指示器的画笔
     */
    private Paint mPaint;


    /**
     * tab选项卡的宽度
     */
    private int mTabWidth;

    /**
     * 默认颜色
     */
    private int mNormalColor;

    /**
     * 选中颜色
     */
    private int mSelectedColor;

    /**
     * 指示器颜色
     */
    private int mIndicatorColor;

    /**
     * 文本的尺寸
     */
    private int mTextSize;

    /**
     * 指示器绘图位置
     */
    private int mIndicatorHeight;
    private int mIndicatorVerticalPos = 0;

    /**
     * 指示器已经移动的位置
     */
    private int mIndicatorTravelOffset;

    /**
     * 关联的内容页面
     */
    private ViewPager mViewPager;

    public XLTabLayout(Context context) {
        this(context, null);
    }

    public XLTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.XLTabLayout);
        mNormalColor = attrArray.getColor(R.styleable.XLTabLayout_text_normal_color, getResources().getColor(R.color.gray_light));
        mSelectedColor = attrArray.getColor(R.styleable.XLTabLayout_text_selected_color, getResources().getColor(R.color.white_light));
        mIndicatorColor = attrArray.getColor(R.styleable.XLTabLayout_tab_indicator_color, mSelectedColor);
        mTextSize = attrArray.getInt(R.styleable.XLTabLayout_text_size, 14);
        attrArray.recycle();
        mIndicatorHeight = DisplayUtil.dip2px(2);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 绑定tab选项卡的数据
     *
     * @param tabs 标题栏数据
     */
    private void bindData(List<String> tabs) {
        removeAllViews();
        mTabWidth = DisplayUtil.getSreenWidth() / tabs.size();

        LayoutParams lp = new LayoutParams(mTabWidth, LayoutParams.MATCH_PARENT);
        TypedArray typedArray = null;
        boolean isShowRipple = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

        if (isShowRipple) {
            typedArray = UIUtil.generateRippleTypedArray(getContext());
        }
        for (String title : tabs) {
            TextView tv = new TextView(getContext());
            tv.setText(title);

            if (typedArray != null) {
                UIUtil.setRippleBg(tv, typedArray);
            } else {
                tv.setBackgroundResource(R.drawable.selector_transparent_gray_circle);
            }
            tv.setClickable(true);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
            tv.setTextColor(mNormalColor);
            addView(tv, lp);
        }
        if (typedArray != null) {
            typedArray.recycle();
        }

        setItemClickEvent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIndicatorVerticalPos == 0) {
            //+1是为了避免底部有空隙存在
            mIndicatorVerticalPos = getHeight() + 1 - mIndicatorHeight / 2;
        }

        //画指示器
        mPaint.setColor(mIndicatorColor);
        mPaint.setStrokeWidth(mIndicatorHeight);
        canvas.drawLine(mIndicatorTravelOffset, mIndicatorVerticalPos, mTabWidth + mIndicatorTravelOffset, mIndicatorVerticalPos, mPaint);

        canvas.save();

    }

    /**
     * 设置关联的ViewPager
     *
     * @param viewPager
     */
    public void setupWithViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        PagerAdapter mPagerAdapter = mViewPager.getAdapter();

        final int adapterCount = mPagerAdapter.getCount();
        List<String> dataList = new ArrayList<>(adapterCount);

        for (int i = 0; i < adapterCount; i++) {
            dataList.add(mPagerAdapter.getPageTitle(i).toString());
        }

        bindData(dataList);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (isEnabled()) {
                    highLightTextView(position);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        int currentPos = viewPager.getCurrentItem();
        mViewPager.setCurrentItem(currentPos);

        highLightTextView(currentPos);
    }

    /**
     * 重置TAB文本颜色
     */
    private void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(mNormalColor);
            }
        }
    }

    /**
     * 高亮某个Tab的文本
     *
     * @param pos
     */
    private void highLightTextView(int pos) {
        resetTextViewColor();
        View view = getChildAt(pos);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(mSelectedColor);
        }
    }

    /**
     * 设置Tab的点击事件
     */
    private void setItemClickEvent() {
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    /**
     * 指示器跟随手指进行滚动
     *
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        mIndicatorTravelOffset = (int) (mTabWidth * (offset + position));
        invalidate();
    }
}
