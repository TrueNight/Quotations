package xyz.truenight.quotations.model

import com.google.gson.annotations.SerializedName

/**
 * Created by true
 * date: 17/09/2017
 * time: 16:24
 *
 * Copyright © Mikhail Frolov
 */

data class Tools(@SerializedName("subscribed_count") val count: Int,
                 @SerializedName("subscribed_list") val ticks: Ticks?)

//{"subscribed_count":2, "subscribed_list":{"ticks":[{"s":"EURGBP","b":"0.79575","bf":2,"a":"0.79593","af":0,"spr":"1.8"},
//    {"s":"EURUSD","b":"1.12835","bf":0,"a":"1.12848","af":1,"spr":"1.3"}]}}