package com.liveteamvn.archmvp.base.data

import com.google.gson.annotations.SerializedName

/**
 * Created by liam on 10/16/2017.
 */

class BaseRes<T>(@SerializedName("data") var data: T?, @SerializedName("status") var status: StatusRes?)