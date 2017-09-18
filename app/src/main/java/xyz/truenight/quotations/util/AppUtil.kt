package xyz.truenight.quotations.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity

/**
 * Created by true
 * date: 17/09/2017
 * time: 14:30
 *
 * Copyright © Mikhail Frolov
 */
object AppUtil {

    fun getActivity(context: Context): Activity {
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return getActivity(context.baseContext)
        }
        throw IllegalStateException("Context $context NOT contains activity!")
    }

    fun getFragmentActivity(context: Context): FragmentActivity {
        if (context is FragmentActivity) {
            return context
        } else if (context is Activity) {
            throw IllegalStateException("Context $context NOT support-v4 Activity")
        } else if (context is ContextWrapper) {
            return getFragmentActivity(context.baseContext)
        }
        throw IllegalStateException("Context $context NOT contains activity!")
    }

    fun getAppCompatActivity(context: Context): AppCompatActivity {
        if (context is AppCompatActivity) {
            return context
        } else if (context is Activity) {
            throw IllegalStateException("Context $context NOT support-v4 Activity")
        } else if (context is ContextWrapper) {
            return getAppCompatActivity(context.baseContext)
        }
        throw IllegalStateException("Context $context NOT contains activity!")
    }
}