package com.adai.dataevent.datasource

import android.arch.lifecycle.*
import com.adai.dataevent.DataLifecycle
import com.adai.dataevent.event.Event
import com.adai.dataevent.event.EventObserver
import com.adai.dataevent.group.OrderDataObserver
import com.adai.dataevent.group.OrderLiveData
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
object DataContainer {
    private val container = mutableMapOf<String, OrderLiveData>()
    private val groupContainer = mutableMapOf<String, OrderLiveData>()
    private val groupTails = mutableMapOf<String, OrderLiveData>()
    fun addData(key: String, data: OrderLiveData) {
        if (container.containsKey(key)) {
            container[key]
        }
        container[key] = data
    }

    fun addData(key: String, data: Any) {
        if (container.containsKey(key)) {
            container[key]?.postValue(data)
        } else {
            OrderLiveData().also {
                container[key] = it
                it.postValue(data)
            }
        }
    }

    fun addToGroup(groupKey: String,
                   key: String,
                   data: Any,
                   observer: OrderDataObserver<Any>) {
        if (groupContainer.containsKey(groupKey)) {
            val nextData = OrderLiveData().also {
                it.postValue(data)
                it.observer = observer
                container[key] = it
            }
            addToTail(groupKey, nextData)
        } else {
            OrderLiveData().also {
                it.postValue(data)
                it.observer = observer
                container[key] = it
                groupContainer[groupKey] = it
                addToTail(groupKey, it)
            }
        }
    }

    private fun addToTail(key: String, nextData: OrderLiveData) {
        if (groupTails.containsKey(key)) {
            val tail = groupTails[key]
            tail?.next = nextData
        }
        groupTails[key] = nextData
    }

    fun addEvent(key: String) {
        addData(key, Event())
    }

    private fun registerObserver(key: String, lifecycleOwner: LifecycleOwner?, observer: Observer<Any>) {
        val data = container[key]
                ?: throw IllegalArgumentException("this key has never been registered or it has been removed!")
        if (lifecycleOwner == null) {
            data.observeForever(observer)
        } else {
            data.observe(lifecycleOwner, observer)
        }
    }

    fun registerGroupObserver(groupKey: String, observer: EventObserver) {
        groupContainer[groupKey]?.also {
            it.subscribe()
        }
        groupTails[groupKey]?.also {
            it.eventObserver = observer
        }
    }


    private fun registerObserverSticky(key: String, lifecycleOwner: LifecycleOwner?, observer: Observer<Any>) {
        lifecycleOwner?.lifecycle?.addObserver(object : DataLifecycle {
            override fun onCreate() {
                val data = container[key]
                        ?: throw IllegalArgumentException("this key has never been registered or it has been removed!")
                data.observe(lifecycleOwner, observer)
                data.postValue(data.value)
            }

            override fun onDestroy() {
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        })
    }

    fun registerEventObserver(key: String, lifecycleOwner: LifecycleOwner?, observer: EventObserver) {
        registerObserver(key, lifecycleOwner, observer)
    }

    fun <T> registerDataObserver(key: String, lifecycleOwner: LifecycleOwner?, observer: DataObserver<T>) {
        registerObserver(key, lifecycleOwner, observer)
    }

    fun registerStickyEventObserver(key: String, lifecycleOwner: LifecycleOwner?, observer: EventObserver) {
        registerObserverSticky(key, lifecycleOwner, observer)
    }

    fun <T> registerStickyDataObserver(key: String, lifecycleOwner: LifecycleOwner?, observer: DataObserver<T>) {
        registerObserverSticky(key, lifecycleOwner, observer)
    }

    fun getData(key: String): MutableLiveData<Any>? {
        return container[key] ?: return null
    }

    fun <T> getDataValue(key: String): T? {
        return container[key]?.value as? T
    }

    fun removeData(key: String) {
        container.remove(key)
    }
}