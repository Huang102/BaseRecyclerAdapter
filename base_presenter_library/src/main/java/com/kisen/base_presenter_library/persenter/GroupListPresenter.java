package com.kisen.base_presenter_library.persenter;

import android.content.Context;


import com.kisen.base_adapter_library.BaseViewHolder;
import com.kisen.base_presenter_library.IData;
import com.kisen.base_presenter_library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter 容器，无Data，只存在Presenter
 * 用于一个列表页面出现多个布局样式，多个Presenter时，作为Presenter容器
 * 只对Presenter进行操作
 *
 * @author Huangwy
 * @TIME 2016/8/17 13:54
 */
public class GroupListPresenter extends AbsListPresenter {

    public GroupListPresenter(Context context) {
        super(context);
        presenters = new ArrayList();
    }

    @Override
    protected AbsListPresenter createPresenterWithData(IData data) {
        GroupListPresenter presenter = new GroupListPresenter(context);
        presenter.setData(data);
        return presenter;
    }

    public void setPresenters(List<AbsListPresenter> presenters) {
        this.presenters.addAll(presenters);
    }

    public void addPresenter(AbsListPresenter presenter) {
        this.presenters.add(presenter);
    }

    @Override
    public int getViewResId() {
        return R.layout.view_blank;
    }

    @Override
    public void setViewData(Context context, BaseViewHolder helper) {
    }

    @Override
    public int getType() {
        return TYPE_VIEW_BLANK;
    }

    @Override
    public void clear() {
        super.clear();
        if (presenters != null)
            presenters.clear();
    }
}
