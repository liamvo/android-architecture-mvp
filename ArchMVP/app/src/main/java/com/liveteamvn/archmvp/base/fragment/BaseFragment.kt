package com.liveteamvn.archmvp.base.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liveteamvn.archmvp.ArchMVPApp
import com.liveteamvn.archmvp.base.activity.BaseActivity
import com.liveteamvn.archmvp.base.presenter.IPresenter
import com.liveteamvn.archmvp.base.presenter.IPresenterFactory
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


/**
 * Created by liam on 09/10/2017.
 */
abstract class BaseFragment<P : IPresenter> : IFragment() {
    @Inject lateinit var presenterFactory: IPresenterFactory
    var presenter: P? = null

    val fragState: IFragmentState by lazy {
        (activity as BaseActivity<*>).fragState
    }

    abstract fun getPresenterClass(): Class<P>?

    abstract fun isUseEventBus(): Boolean

    @LayoutRes
    abstract fun getLayoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPresenterClass()?.let {
            presenter = presenterFactory.create(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(getLayoutRes(), container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.attachView(this)
        if (isUseEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
        if (isUseEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val refWatcher = ArchMVPApp.getRefWatcher(activity)
        refWatcher?.watch(this)
    }

    fun getEventBus(): EventBus = EventBus.getDefault()
}