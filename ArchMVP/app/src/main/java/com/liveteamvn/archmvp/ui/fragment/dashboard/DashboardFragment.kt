package com.liveteamvn.archmvp.ui.fragment.dashboard

import android.support.v4.app.Fragment
import com.liveteamvn.archmvp.R
import com.liveteamvn.archmvp.base.fragment.BaseFragment
import com.liveteamvn.archmvp.base.presenter.IPresenter


/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : BaseFragment<IPresenter>() {
    override fun getPresenterClass(): Class<IPresenter>? = null

    override fun isUseEventBus(): Boolean = false

    override fun getLayoutRes(): Int = R.layout.fragment_dashboard

}
