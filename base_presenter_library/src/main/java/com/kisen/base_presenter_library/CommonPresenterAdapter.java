package com.kisen.base_presenter_library;

import android.content.Context;
import android.view.ViewGroup;

import com.kisen.base_adapter_library.BaseViewHolder;
import com.kisen.base_adapter_library.RBaseAdapter;
import com.kisen.base_presenter_library.persenter.AbsListPresenter;

import java.util.List;

/**
 * @author Huangwy
 * @TIME 2016/8/16 15:50
 */
public class CommonPresenterAdapter extends RBaseAdapter<AbsListPresenter> {

    protected int position;

    public CommonPresenterAdapter(Context context) {
        this(context, 0);
    }

    public CommonPresenterAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public CommonPresenterAdapter(Context context, int layoutResId, List<AbsListPresenter> list) {
        super(context, layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, AbsListPresenter item) {
        item.setViewData(context, helper);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (mData.get(position).getViewResId() != 0)
            mLayoutResId = mData.get(position).getViewResId();
        return createBaseViewHolder(parent, mLayoutResId);
    }

    @Override
    protected int getDefItemViewType(int position) {
        this.position = position;
        return mData.get(position).getType();
    }
}
