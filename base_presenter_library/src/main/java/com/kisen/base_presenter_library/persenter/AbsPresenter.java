package com.kisen.base_presenter_library.persenter;

import android.content.Context;

import com.kisen.base_presenter_library.IData;

/**
 * 对应普通数据类
 *
 * @author Huangwy
 * @TIME 2016/8/15 10:39
 */
public abstract class AbsPresenter<D extends IData> implements IPresenter {

    protected Context context;

    protected D data;

    public AbsPresenter(Context context) {
        this.context = context;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    /**
     * 清空持有的数据
     */
    public void clear(){
        data = null;
    }

}
