package com.liveteamvn.archmvp.ui.fragment.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.liveteamvn.archmvp.R
import com.liveteamvn.archmvp.base.fragment.BaseFragment
import com.liveteamvn.archmvp.base.data.Status
import com.liveteamvn.archmvp.data.local.entity.FeedEntity
import com.liveteamvn.archmvp.di.ext.Injectable
import kotlinx.android.synthetic.main.fragment_home.*
import net.idik.lib.slimadapter.SlimAdapter
import org.apache.commons.lang3.StringEscapeUtils

/**
 * Created by liam on 11/10/2017.
 */
class HomeFragment : BaseFragment<HomePresenter>(), Injectable, HomeView {
    override fun isUseEventBus(): Boolean = false

    private val adapter: SlimAdapter by lazy {
        SlimAdapter.create().register<FeedEntity>(R.layout.item_feed) { feed, injector ->
            Glide.with(this).load(feed.imageUrl).apply(RequestOptions().placeholder(R.drawable.ic_launcher_background)).into(injector.findViewById(R.id.imvFeed))
            injector.findViewById<TextView>(R.id.tvTitle).text = StringEscapeUtils.unescapeHtml3(feed.title)
            injector.findViewById<TextView>(R.id.tvShortDescription).text = StringEscapeUtils.unescapeHtml3(feed.shortDescription)
            injector.findViewById<TextView>(R.id.tvTime).text = feed.dateTime
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun getPresenterClass(): Class<HomePresenter> = HomePresenter::class.java

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.attachTo(recycler)
        presenter?.feeds?.observe(this, Observer { data ->
            run {
                fun handleData() {
                    when {
                        data?.data == null -> {
//                            Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show()
                        }
                        data.data.isEmpty() -> {
                            Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            adapter.updateData(data.data)
                        }
                    }
                }
                when (data?.status) {
                    Status.LOADING -> {
                        Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show()
                        handleData()
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                        handleData()
                    }
                    else -> {
                        handleData()
                    }
                }
            }
        })
    }
}