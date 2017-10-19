package com.liveteamvn.archmvp.base.presenter

/**
 * Created by liam on 09/10/2017.
 */

interface IPresenter {
    fun attachView(view: Any)
    fun detachView()
}

