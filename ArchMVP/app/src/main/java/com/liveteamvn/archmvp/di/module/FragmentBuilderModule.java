package com.liveteamvn.archmvp.di.module;

import com.liveteamvn.archmvp.ui.fragment.home.HomeFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by liam on 09/10/2017.
 */

@Module
public abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();
}
