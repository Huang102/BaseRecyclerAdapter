package com.kisen.base_adapter_library.animation;

import android.animation.Animator;
import android.view.View;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public interface  BaseAnimation {

    Animator[] getAnimators(View view);

}
