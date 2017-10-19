package com.liveteamvn.archmvp.base.data

import com.google.gson.annotations.SerializedName

/**
 * Created by liam on 10/16/2017.
 */
class StatusRes(@SerializedName("message") override val message: String?, @SerializedName("code") var code: Int?) : Exception(message)