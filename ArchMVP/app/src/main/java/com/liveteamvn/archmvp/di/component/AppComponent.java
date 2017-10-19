package com.liveteamvn.archmvp.di.component;

import android.app.Application;

import com.liveteamvn.archmvp.ArchMVPApp;
import com.liveteamvn.archmvp.di.module.ActivityBuilderModule;
import com.liveteamvn.archmvp.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by liam on 09/10/2017.
 */
@Singleton
@Component(modules = {
        AppModule.class,
        AndroidInjectionModule.class,
        ActivityBuilderModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(ArchMVPApp archMVPApp);
}
