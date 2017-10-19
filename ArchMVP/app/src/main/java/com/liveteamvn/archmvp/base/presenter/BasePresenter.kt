package com.liveteamvn.archmvp.base.presenter

import com.liveteamvn.archmvp.base.view.IView
import com.liveteamvn.archmvp.base.repository.IRepository

/**
 * Created by liam on 11/10/2017.
 */
abstract class BasePresenter<R : IRepository, V : IView>(repository: R) : IPresenter {
    private var view: V? = null

    @Suppress("UNCHECKED_CAST")
    override fun attachView(view: Any) {
        try {
            this.view = view as V
        } catch (ex: ClassCastException) {

        }
    }

    override fun detachView() {
        view = null
    }
}