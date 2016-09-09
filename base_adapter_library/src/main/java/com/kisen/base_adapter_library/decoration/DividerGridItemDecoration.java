package com.kisen.base_adapter_library.decoration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.kisen.base_adapter_library.R;


/**
 * 宫格模式和瀑布流模式
 *
 * @author Huangwy
 * @TIME 2016/8/4 9:56
 * <p/>
 * <p/>
 * StaggeredGridLayoutManager未测试
 */
public class DividerGridItemDecoration extends BaseDividerItemDecoration {

    private Drawable mDivider;

    private int mIntrinsicHeight;

    private int mIntrinsicWidth;

    private int mRecyclerViewLeft = 0;
    private int mRecyclerViewTop = 0;
    private int mRecyclerViewRight = 0;
    private int mRecyclerViewBottom = 0;

    public DividerGridItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider_bg);
        mIntrinsicHeight = mDivider.getIntrinsicHeight();
        mIntrinsicWidth = mDivider.getIntrinsicWidth();
    }

    /**
     * 设置 decoration 样式
     *
     * @param divider drawable 可自定义渐变色
     * @return
     */
    public DividerGridItemDecoration setDivider(Drawable divider) {
        this.mDivider = divider;
        return this;
    }

    /**
     * 设置decoration 间隔
     *
     * @param dividerHeight item间的垂直间隔
     * @param dividerWidth  item间的水平间隔
     * @return
     */
    public DividerGridItemDecoration setDividerSpace(int dividerHeight, int dividerWidth) {
        mIntrinsicHeight = (int) (dividerHeight * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        mIntrinsicWidth = (int) (dividerWidth * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
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
    public DividerGridItemDecoration setRecyclerViewPadding(int left, int top, int right, int bottom) {
        this.mRecyclerViewLeft = (int) (left * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        this.mRecyclerViewTop = (int) (top * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        this.mRecyclerViewRight = (int) (right * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        this.mRecyclerViewBottom = (int) (bottom * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f));
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    /**
     * 水平间距
     *
     * @param c      画笔
     * @param parent 父容器
     */
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mIntrinsicWidth;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mIntrinsicHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 垂直间距
     *
     * @param c      画笔
     * @param parent 父容器
     */
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mIntrinsicWidth;

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 是否是第一列
     *
     * @param parent     父容器
     * @param pos        所在位置
     * @param spanCount  行数/行数
     * @param childCount item数量
     * @return
     */
    private boolean isFirstColum(RecyclerView parent, int pos, int spanCount,
                                 int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 1)// 如果是第一列，则需要绘制左边
                {
                    return true;
                }
            } else {
                // 如果是第一列，则需要绘制左边
                if (pos < spanCount)
                    return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 1)// 如果是第一列，则需要绘制左边
                {
                    return true;
                }
            } else {
                if (pos < spanCount)// 如果是第一列，则需要绘制左边
                    return true;
            }
        }
        return false;
    }

    /**
     * 是否是最后一列
     *
     * @param parent     父容器
     * @param pos        所在位置
     * @param spanCount  行数/行数
     * @param childCount item数量
     * @return
     */
    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    /**
     * 是否是第一行
     *
     * @param parent     父容器
     * @param pos        所在位置
     * @param spanCount  行数/行数
     * @param childCount item数量
     * @return
     */
    private boolean isFirstRaw(RecyclerView parent, int pos, int spanCount,
                               int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                if (pos < spanCount)
                    return true;
            } else {
                if ((pos + 1) % spanCount == 1)
                    return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是第一行，则需要绘制底部
                if (pos < spanCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是第一行，则需要绘制底部
                if ((pos + 1) % spanCount == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是最后一行
     *
     * @param parent     父容器
     * @param pos        所在位置
     * @param spanCount  行数/行数
     * @param childCount item数量
     * @return
     */
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();

        boolean firstColum = isFirstColum(parent, itemPosition, spanCount, childCount);
        boolean firstRaw = isFirstRaw(parent, itemPosition, spanCount, childCount);
        int left = firstColum ? mRecyclerViewLeft : 0;
        int top = firstRaw ? mRecyclerViewTop : 0;

        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
        {
            outRect.set(left, top, mIntrinsicWidth, mRecyclerViewBottom);
        } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
        {
            outRect.set(left, top, mRecyclerViewRight, mIntrinsicHeight);
        } else {
            outRect.set(left, top, mIntrinsicWidth, mIntrinsicHeight);
        }
    }
}
