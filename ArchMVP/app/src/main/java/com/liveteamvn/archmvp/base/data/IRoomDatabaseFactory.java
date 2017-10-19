package com.liveteamvn.archmvp.base.data;

import android.arch.persistence.room.RoomDatabase;

import com.liveteamvn.archmvp.base.presenter.IPresenter;

/**
 * Created by liam on 10/10/2017.
 */

public interface IRoomDatabaseFactory {
    <T extends RoomDatabase> T create(Class<T> modelClass);
}
