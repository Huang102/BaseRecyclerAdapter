package com.kisen.base_adapter_library;

import android.content.Context;

import java.util.List;

/**
 * @author Huangwy
 * @TIME 2016/7/12 18:00
 */
public abstract class RBaseAdapter<T> extends BaseQuickAdapter<T> {

    protected Context context;

    public RBaseAdapter(Context context, int layoutResId, List<T> data) {
        super(layoutResId, data);
        this.context = context;
    }

}
