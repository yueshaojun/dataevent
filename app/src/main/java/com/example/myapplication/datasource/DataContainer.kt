package com.example.myapplication.datasource

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData

object DataContainer {
    private val container = mutableMapOf<String, MutableLiveData<Any>>()
    fun addData(key: String, data: MutableLiveData<Any>) {
        if (container.containsKey(key)) {
            container[key]
        }
        container[key] = data
    }

    fun addData(key: String, data: Any) {
        if (container.containsKey(key)) {
            container[key]?.postValue(data)
        } else {
            MutableLiveData<Any>().also {
                container[key] = it
                it.postValue(data)
            }
        }
    }

    fun <T> registerDataObserver(key: String, lifecycleOwner: LifecycleOwner?, observer: DataObserver<T>) {
        val realObserver: DataObserver<T>
        val data = container[key]
                ?: throw IllegalArgumentException("this key has never been registered or it has been removed!")
        if (observer is ImmediateDataObserver<T>) {
            realObserver = object : DataObserver<T>() {
                override fun onReceived(t: T?) {
                    removeData(key)
                    observer.onReceived(t)
                }
            }
        } else {
            realObserver = observer
        }
        if (lifecycleOwner == null) {
            data.observeForever(realObserver)
        } else {
            data.observe(lifecycleOwner, realObserver)
        }
    }

    fun getData(key: String): Any? {
        val data = container[key] ?: return null
        return data.value
    }

    fun removeData(key: String) {
        container.remove(key)
    }
}