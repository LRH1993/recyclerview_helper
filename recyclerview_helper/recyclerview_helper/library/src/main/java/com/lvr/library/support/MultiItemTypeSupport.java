package com.lvr.library.support;

/**
 * Created by lvr on 2017/5/24.
 */

public interface MultiItemTypeSupport<T>
{
    int getLayoutId(int itemType);

    int getItemViewType(int position, T t);
}
