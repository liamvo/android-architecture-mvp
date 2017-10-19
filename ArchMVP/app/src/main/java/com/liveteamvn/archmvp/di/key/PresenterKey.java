package com.liveteamvn.archmvp.di.key;

import com.liveteamvn.archmvp.base.presenter.IPresenter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MapKey
public @interface PresenterKey {
    Class<? extends IPresenter> value();
}