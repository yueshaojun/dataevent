package com.adai.dataevent.group

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer

class OrderLiveData : MutableLiveData<Any>() {
    var observer: OrderDataObserver<Any>? = null
    var next: OrderLiveData? = null
    private var barrier: Boolean = false
    fun invoke() {
        barrier = false
        observeForever(object : Observer<Any> {
            override fun onChanged(t: Any?) {
                return run {
                    if (observer == null) {
                        throw IllegalStateException("use method start() you must set observer!")
                    }
                    if (barrier) {
                        return@onChanged
                    }
                    val h = observer!!.onReceived(t)
                    barrier = true
                    if (h) {
                        next?.invoke()
                    }
                }
            }
        })
    }
}