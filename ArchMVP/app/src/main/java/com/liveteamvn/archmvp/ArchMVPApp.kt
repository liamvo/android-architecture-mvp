package com.liveteamvn.archmvp

import android.app.Activity
import android.content.Context
import android.support.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.liveteamvn.archmvp.di.ext.AppInjector
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


/**
 * Created by liam on 09/10/2017.
 */
class ArchMVPApp : MultiDexApplication(), HasActivityInjector {
    @Inject
    lateinit var activityDispatchingInjector: DispatchingAndroidInjector<Activity>

    companion object {
        fun getRefWatcher(context: Context): RefWatcher? {
            val application = context.applicationContext as ArchMVPApp
            return application.refWatcher
        }
    }

    private var refWatcher: RefWatcher? = null

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        refWatcher = LeakCanary.install(this)
        Stetho.initializeWithDefaults(this)

        AppInjector.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingInjector
    }
}