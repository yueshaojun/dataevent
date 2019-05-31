package com.adai.dataevent.event

import android.arch.lifecycle.Observer


abstract class EventObserver : Observer<Any> {
    abstract fun onEvent()
    override fun onChanged(t: Any?) {
        onEvent()
    }
}