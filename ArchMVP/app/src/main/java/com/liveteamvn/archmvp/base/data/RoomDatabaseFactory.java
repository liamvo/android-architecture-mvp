package com.liveteamvn.archmvp.base.data;

import android.arch.persistence.room.RoomDatabase;

import com.liveteamvn.archmvp.base.presenter.IPresenter;
import com.liveteamvn.archmvp.base.presenter.IPresenterFactory;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Created by liam on 09/10/2017.
 */
@Singleton
public class RoomDatabaseFactory implements IRoomDatabaseFactory {
    private final Map<Class<? extends RoomDatabase>, Provider<RoomDatabase>> creators;

    @Inject
    public RoomDatabaseFactory(Map<Class<? extends RoomDatabase>, Provider<RoomDatabase>> creators) {
        this.creators = creators;
    }

    @Override
    public <T extends RoomDatabase> T create(Class<T> modelClass) {
        Provider<? extends RoomDatabase> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class<? extends RoomDatabase>, Provider<RoomDatabase>> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }
        try {
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
