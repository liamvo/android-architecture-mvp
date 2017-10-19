package com.liveteamvn.archmvp.base.presenter;

import com.liveteamvn.archmvp.base.presenter.IPresenter;

/**
 * Created by liam on 10/10/2017.
 */

public interface IPresenterFactory {
    <T extends IPresenter> T create(Class<T> modelClass);
}
