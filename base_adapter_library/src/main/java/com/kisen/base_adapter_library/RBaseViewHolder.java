package com.kisen.base_adapter_library;

import android.view.View;

/**
 * 可创建ViewHolder
 *
 * @author Huangwy
 * @TIME 2016/8/3 15:19
 */
public class RBaseViewHolder extends BaseViewHolder {

    public RBaseViewHolder(View view) {
        super(view);
    }

    public RBaseViewHolder setOnClickListener(View.OnClickListener listener){
        getConvertView().setOnClickListener(listener);
        return this;
    }
}
