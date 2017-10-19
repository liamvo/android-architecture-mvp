package com.liveteamvn.archmvp.di.module;

import com.liveteamvn.archmvp.ui.activity.HomeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by liam on 09/10/2017.
 */

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract HomeActivity homeActivity();

}
