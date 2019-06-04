package com.adai.dataevent.group

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.adai.dataevent.event.EventObserver
import kotlinx.coroutines.*

@ObsoleteCoroutinesApi
class OrderLiveData : MutableLiveData<Any>() {
    var observer: OrderDataObserver<Any>? = null
    var next: OrderLiveData? = null
    internal var eventObserver: EventObserver? = null
    private var version = -1
    private val realObserver = Observer<Any> { t ->
        if (observer == null) {
            throw IllegalStateException("use method start() you must set observer!")
        }
        if (version == 0) {
            return@Observer
        }
        val h = receiveAsync(t)
        runBlocking {
            if (h.await()) {
                if (next == null) eventObserver?.onEvent() else next!!.subscribe()
            }
        }

    }

    private fun receiveAsync(t: Any?) = GlobalScope.async(newSingleThreadContext("receive")) {
        observer!!.onReceived(t)
    }

    internal fun subscribe() {
        observeForever(realObserver)
    }

    fun clear() {
        removeObserver(realObserver)
    }

    override fun setValue(value: Any?) {
        version++
        super.setValue(value)
    }
}