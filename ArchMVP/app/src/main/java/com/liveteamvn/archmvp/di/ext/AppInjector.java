package com.liveteamvn.archmvp.di.ext;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.liveteamvn.archmvp.ArchMVPApp;
import com.liveteamvn.archmvp.di.component.DaggerAppComponent;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by liam on 09/10/2017.
 */

public class AppInjector {

    private AppInjector() {
    }

    public static void init(ArchMVPApp archMVPApp) {
        DaggerAppComponent.builder().application(archMVPApp)
                .build().inject(archMVPApp);
        archMVPApp
                .registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                        handleActivity(activity);
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {

                    }

                    @Override
                    public void onActivityResumed(Activity activity) {

                    }

                    @Override
                    public void onActivityPaused(Activity activity) {

                    }

                    @Override
                    public void onActivityStopped(Activity activity) {

                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {

                    }
                });
    }

    private static void handleActivity(Activity activity) {
        if (activity instanceof Injectable) {
            AndroidInjection.inject(activity);
            if (activity instanceof HasSupportFragmentInjector && activity instanceof AppCompatActivity) {
                ((AppCompatActivity) activity).getSupportFragmentManager()
                        .registerFragmentLifecycleCallbacks(
                                new FragmentManager.FragmentLifecycleCallbacks() {
                                    @Override
                                    public void onFragmentPreCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                                        super.onFragmentPreCreated(fm, f, savedInstanceState);
                                        if (f instanceof Injectable) {
                                            AndroidSupportInjection.inject(f);
                                        }
                                    }

                                }, true);
            }
        }
    }

}
