package xyz.truenight.quotations

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import xyz.truenight.quotations.util.Storage

/**
 * Created by true
 * date: 17/09/2017
 * time: 14:23
 *
 * Copyright © Mikhail Frolov
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        Storage.init(this)
    }
}