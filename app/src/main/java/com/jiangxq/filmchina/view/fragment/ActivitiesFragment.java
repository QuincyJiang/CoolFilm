package com.jiangxq.filmchina.view.fragment;

import com.jiangxq.filmchina.base.BaseArticalListFragment;
import com.jiangxq.filmchina.base.BaseModel;
import com.jiangxq.filmchina.model.ActivitiesModel;

/**
 * Created by jiangxq170307 on 2017/9/19.
 */

public class ActivitiesFragment extends BaseArticalListFragment {
    @Override
    public BaseModel initModel() {
        return new ActivitiesModel(this);
    }
}