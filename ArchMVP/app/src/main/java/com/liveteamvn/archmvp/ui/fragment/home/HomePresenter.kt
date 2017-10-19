package com.liveteamvn.archmvp.ui.fragment.home

import android.arch.lifecycle.LiveData
import com.liveteamvn.archmvp.base.presenter.BasePresenter
import com.liveteamvn.archmvp.base.data.Resource
import com.liveteamvn.archmvp.data.local.entity.FeedEntity
import com.liveteamvn.archmvp.data.repository.FeedRepository
import javax.inject.Inject

/**
 * Created by liam on 11/10/2017.
 */
class HomePresenter @Inject constructor(repository: FeedRepository) : BasePresenter<FeedRepository, HomeView>(repository) {
    val feeds: LiveData<Resource<List<FeedEntity>>> = repository.loadFeeds(1)
}