package com.liveteamvn.archmvp.base.activity

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.liveteamvn.archmvp.R
import com.liveteamvn.archmvp.base.fragment.IFragment
import com.liveteamvn.archmvp.base.presenter.IPresenter
import com.liveteamvn.archmvp.base.presenter.IPresenterFactory
import com.liveteamvn.archmvp.base.fragment.FragmentState
import com.liveteamvn.archmvp.base.fragment.IFragmentState
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


/**
 * Created by liam on 09/10/2017.
 */
abstract class BaseActivity<P : IPresenter> : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject lateinit var fragmentAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var presenterFactory: IPresenterFactory
    var presenter: P? = null

    abstract fun getPresenterClass(): Class<P>?

    abstract fun isUseEventBus(): Boolean

    val fragState: IFragmentState by lazy {
        FragmentState(supportFragmentManager, R.id.fragContainer)
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        getPresenterClass()?.let {
            presenter = presenterFactory.create(it)
        }
        fragState.setStacksRootFragment(getRootFragmentStacks())
        fragState.showStack(getStackActive())
        setupView()
    }

    abstract fun setupView()

    override fun onStart() {
        super.onStart()
        presenter?.attachView(this)
        if (isUseEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        presenter?.detachView()
        if (isUseEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    abstract fun getRootFragmentStacks(): ArrayList<IFragment>
    abstract fun getStackActive(): Int
    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentAndroidInjector
    }

    override fun onBackPressed() {
        when {
            !fragState.isRootFragment -> {
                fragState.popFragment(1)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    fun getEventBus(): EventBus = EventBus.getDefault()
}