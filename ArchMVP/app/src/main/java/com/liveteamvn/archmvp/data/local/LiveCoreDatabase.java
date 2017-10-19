package com.liveteamvn.archmvp.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.liveteamvn.archmvp.data.local.dao.FeedDao;
import com.liveteamvn.archmvp.data.local.entity.FeedEntity;

/**
 * Created by liam on 10/10/2017.
 */

@Database(entities = {FeedEntity.class}, version = 1)
public abstract class LiveCoreDatabase extends RoomDatabase {
    public abstract FeedDao liveCoreDao();
}