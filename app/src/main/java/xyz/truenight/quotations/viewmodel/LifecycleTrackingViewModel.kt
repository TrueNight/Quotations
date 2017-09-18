package xyz.truenight.quotations.viewmodel

import android.arch.lifecycle.*
import android.arch.lifecycle.Lifecycle.State.DESTROYED
import android.arch.lifecycle.Lifecycle.State.STARTED

/**
 * Created by true
 * date: 16/09/2017
 * time: 23:17
 *
 * Copyright © Mikhail Frolov
 */

abstract class LifecycleTrackingViewModel : ViewModel() {


    private var active: Boolean = false
    private var mActiveCount: Int = 0

    fun registerLifecycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(object : GenericLifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (source.lifecycle.currentState == DESTROYED) {
                    owner.lifecycle.removeObserver(this)
                    return
                }

                activeStateChanged(isActiveState(source.lifecycle.currentState))
            }
        })
    }

    private fun activeStateChanged(newActive: Boolean) {
        if (newActive == active) {
            return
        }
        active = newActive
        val wasInactive = this.mActiveCount == 0
        this.mActiveCount += if (active) 1 else -1
        if (wasInactive && active) {
            onActive()
        }
        if (this.mActiveCount == 0 && !active) {
            onInactive()
        }
    }

    private fun isActiveState(state: Lifecycle.State): Boolean {
        return state.isAtLeast(STARTED)
    }

    abstract fun onActive()

    abstract fun onInactive()
}
