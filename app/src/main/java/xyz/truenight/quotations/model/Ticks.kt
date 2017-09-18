package xyz.truenight.quotations.model

import com.google.gson.annotations.SerializedName

/**
 * Created by true
 * date: 17/09/2017
 * time: 16:34
 *
 * Copyright © Mikhail Frolov
 */

data class Ticks(@SerializedName("ticks") val items: List<Quotation>)