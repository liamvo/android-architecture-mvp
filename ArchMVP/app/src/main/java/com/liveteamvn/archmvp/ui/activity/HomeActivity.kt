package com.liveteamvn.archmvp.ui.activity

import android.support.design.widget.BottomNavigationView
import com.liveteamvn.archmvp.R
import com.liveteamvn.archmvp.base.activity.BaseActivity
import com.liveteamvn.archmvp.base.fragment.IFragment
import com.liveteamvn.archmvp.base.presenter.IPresenter
import com.liveteamvn.archmvp.di.ext.Injectable
import com.liveteamvn.archmvp.ui.fragment.dashboard.DashboardFragment
import com.liveteamvn.archmvp.ui.fragment.home.HomeFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<IPresenter>(), Injectable {
    override fun isUseEventBus(): Boolean = false

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                fragState.showStack(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                fragState.showStack(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                fragState.showStack(2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun setupView() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun getPresenterClass(): Class<IPresenter>? = null

    override fun getLayoutRes(): Int = R.layout.activity_home

    override fun getRootFragmentStacks(): ArrayList<IFragment> = arrayListOf(HomeFragment(), DashboardFragment(), DashboardFragment())

    override fun getStackActive(): Int = 0
}
