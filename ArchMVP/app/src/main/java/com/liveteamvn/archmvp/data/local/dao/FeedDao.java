package com.liveteamvn.archmvp.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.liveteamvn.archmvp.data.local.entity.FeedEntity;

import java.util.List;

/**
 * Created by liam on 10/10/2017.
 */
@Dao
public interface FeedDao {
    @Query("SELECT * FROM feed")
    LiveData<List<FeedEntity>> loadFeeds();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(List<FeedEntity> feedEntities);

    @Query("SELECT * FROM feed WHERE href=:href")
    LiveData<FeedEntity> getFeed(String href);
}
