package com.jiangxq.filmchina.view.fragment;

import com.jiangxq.filmchina.base.BaseArticalListFragment;
import com.jiangxq.filmchina.base.BaseModel;
import com.jiangxq.filmchina.model.OriginalModel;

/**
 * Created by jiangxq170307 on 2017/9/19.
 */

public class OriginalFragment extends BaseArticalListFragment {
    @Override
    public BaseModel initModel() {
        return new OriginalModel(this);
    }
}