package com.liveteamvn.archmvp

import android.app.Activity
import android.content.Context
import android.support.multidex.MultiDexApplication
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.facebook.stetho.Stetho
import com.liveteamvn.archmvp.di.ext.AppInjector
import com.liveteamvn.archmvp.helper.HawkHelper
import com.liveteamvn.archmvp.helper.KeyStoreHelper
import com.liveteamvn.archmvp.helper.KeyStoreHelper.checkKeystore
import com.orhanobut.hawk.Hawk
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

        Utils.init(this)

        checkKeystore(this, object : KeyStoreHelper.IOnUpdateKeyStore {
            override fun onBeforeUpdate() {
                //Key encrypt and decrypt will be refresh
            }

            override fun onUpdateSuccess() {
                onSuccess()
                Hawk.deleteAll()
            }

            override fun onSuccess() {
                ToastUtils.showShort("Keystore update success")
                HawkHelper.init(this@ArchMVPApp)
            }

            override fun onError() {
                ToastUtils.showShort("Keystore update error")
            }

        })
        AppInjector.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingInjector
    }
}