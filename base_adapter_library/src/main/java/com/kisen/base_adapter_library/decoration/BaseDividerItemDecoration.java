package com.kisen.base_adapter_library.decoration;

import android.support.v7.widget.RecyclerView;

/**
 * @author Huangwy
 * @TIME 2016/8/4 11:47
 */
public abstract class BaseDividerItemDecoration extends RecyclerView.ItemDecoration {

    public abstract BaseDividerItemDecoration setRecyclerViewPadding(int left, int top, int right, int bottom);

}
