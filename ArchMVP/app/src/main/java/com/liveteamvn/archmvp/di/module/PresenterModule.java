package com.liveteamvn.archmvp.di.module;

import com.liveteamvn.archmvp.base.presenter.IPresenter;
import com.liveteamvn.archmvp.base.presenter.IPresenterFactory;
import com.liveteamvn.archmvp.base.presenter.LiveCorePresenterFactory;
import com.liveteamvn.archmvp.di.key.PresenterKey;
import com.liveteamvn.archmvp.ui.fragment.home.HomePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by liam on 09/10/2017.
 */
@Module
public abstract class PresenterModule {

    @Binds
    @IntoMap
    @PresenterKey(HomePresenter.class)
    abstract IPresenter bindHomePresenter(HomePresenter homePresenter);

    @Binds
    abstract IPresenterFactory bindsPresenterFactory(LiveCorePresenterFactory liveCorePresenterFactory);
}
