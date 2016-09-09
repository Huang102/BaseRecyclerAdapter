package com.kisen.base_adapter_library.decoration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import com.kisen.base_adapter_library.R;

/**
 * @author Huangwy
 * @TIME 2016/8/4 9:59
 */
public class DividerItemDecoration extends BaseDividerItemDecoration {

    private int mIntrinsicHeight;

    private int mIntrinsicWidth;

    private int mRecyclerViewLeft = 0;
    private int mRecyclerViewTop = 0;
    private int mRecyclerViewRight = 0;
    private int mRecyclerViewBottom = 0;

    private int mDividerMarginLeft = 0;
    private int mDividerMarginTop = 0;
    private int mDividerMarginRight = 0;
    private int mDividerMarginBottom = 0;

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;

    private Context context;

    public DividerItemDecoration(Context context, int orientation) {
        this.context = context;
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider_bg);
        mIntrinsicHeight = mDivider.getIntrinsicHeight();
        mIntrinsicWidth = mDivider.getIntrinsicWidth();
        setOrientation(orientation);
    }

    /**
     * 设置 decoration 样式
     *
     * @param divider drawable 可自定义渐变色
     * @return
     */
    public DividerItemDecoration setDivider(int divider) {
        this.mDivider = ContextCompat.getDrawable(context, divider);
        return this;
    }

    /**
     * 设置decoration 间隔
     *
     * @param dividerSpace item间的间隔
     * @return
     */
    public DividerItemDecoration setDividerSpace(int dividerSpace) {
        mIntrinsicHeight = (int) (dividerSpace * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        mIntrinsicWidth = (int) (dividerSpace * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        return this;
    }

    /**
     * 设置 divider 边距
     *
     * @param left   左边距
     * @param top    上边距
     * @param right  右边距
     * @param bottom 下边距
     * @return
     */
    public DividerItemDecoration setDividerMargin(int left, int top, int right, int bottom) {
        this.mDividerMarginLeft = (int) (left * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        this.mDividerMarginTop = (int) (top * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        this.mDividerMarginRight = (int) (right * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        this.mDividerMarginBottom = (int) (bottom * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        return this;
    }

    /**
     * 设置内容距离RecyclerView容器的边距
     *
     * @param left   左边距
     * @param top    上边距
     * @param right  右边距
     * @param bottom 下边距
     * @return
     */
    @Override
    public DividerItemDecoration setRecyclerViewPadding(int left, int top, int right, int bottom) {
        this.mRecyclerViewLeft = (int) (left * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        this.mRecyclerViewTop = (int) (top * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        this.mRecyclerViewRight = (int) (right * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        this.mRecyclerViewBottom = (int) (bottom * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        return this;
    }

    /**
     * 设置列表方向
     *
     * @param orientation
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /**
     * 垂直间距
     *
     * @param c      画笔
     * @param parent 父容器
     */
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + mDividerMarginLeft;
        final int right = parent.getWidth() - parent.getPaddingRight() - mDividerMarginRight;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mIntrinsicHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 水平间距
     *
     * @param c      画笔
     * @param parent 父容器
     */
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop() + mDividerMarginTop;
        final int bottom = parent.getHeight() - parent.getPaddingBottom() - mDividerMarginBottom;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mIntrinsicHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int childCount = parent.getAdapter().getItemCount();
        boolean isFirstItem = itemPosition == 0;
        boolean isLastItem = childCount == itemPosition + 1;
        if (mOrientation == VERTICAL_LIST) {
            int top = isFirstItem ? mRecyclerViewTop : 0;
            outRect.set(mRecyclerViewLeft, top, mRecyclerViewRight, isLastItem ? mRecyclerViewBottom : mIntrinsicHeight);
        } else {
            int left = isFirstItem ? mRecyclerViewLeft : 0;
            outRect.set(left, mRecyclerViewTop, isLastItem ? mRecyclerViewRight : mIntrinsicWidth, mRecyclerViewBottom);
        }
    }
}
