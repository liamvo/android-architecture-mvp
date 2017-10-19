package com.liveteamvn.archmvp.data.repository

import android.arch.lifecycle.LiveData
import com.liveteamvn.archmvp.base.repository.BaseRepository
import com.liveteamvn.archmvp.base.data.NetworkBoundResource
import com.liveteamvn.archmvp.base.data.Resource
import com.liveteamvn.archmvp.data.local.LiveCoreDatabase
import com.liveteamvn.archmvp.data.local.dao.FeedDao
import com.liveteamvn.archmvp.data.local.entity.FeedEntity
import com.liveteamvn.archmvp.data.remote.LiveCoreApiServices
import retrofit2.Call
import javax.inject.Inject

/**
 * Created by liam on 10/10/2017.
 */
class FeedRepository @Inject constructor(database: LiveCoreDatabase, services: LiveCoreApiServices) : BaseRepository<LiveCoreDatabase, FeedDao, LiveCoreApiServices>(database, services) {
    override fun provideDao(): FeedDao = db.liveCoreDao()

    fun loadFeeds(page: Int): LiveData<Resource<List<FeedEntity>>> {
        return object : NetworkBoundResource<List<FeedEntity>, List<FeedEntity>>() {
            override fun shouldFetch(data: List<FeedEntity>?): Boolean {
                return true
            }

            override fun saveCallResult(item: List<FeedEntity>) {
                dao.save(item)
            }

            override fun loadFromDb(): LiveData<List<FeedEntity>> = dao.loadFeeds()

            override fun createCall(): Call<List<FeedEntity>> = sv.getListFeed(page)
        }.asLiveData
    }
}