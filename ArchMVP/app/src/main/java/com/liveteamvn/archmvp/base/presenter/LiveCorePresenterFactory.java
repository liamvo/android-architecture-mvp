package com.liveteamvn.archmvp.base.presenter;

import com.liveteamvn.archmvp.base.presenter.IPresenter;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Created by liam on 09/10/2017.
 */
@Singleton
public class LiveCorePresenterFactory implements IPresenterFactory{
    private final Map<Class<? extends IPresenter>, Provider<IPresenter>> creators;

    @Inject
    public LiveCorePresenterFactory(Map<Class<? extends IPresenter>, Provider<IPresenter>> creators) {
        this.creators = creators;
    }

    @Override
    public <T extends IPresenter> T create(Class<T> modelClass) {
        Provider<? extends IPresenter> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class<? extends IPresenter>, Provider<IPresenter>> entry : creators.entrySet()) {
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
