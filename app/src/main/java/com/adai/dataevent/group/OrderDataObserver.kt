package com.adai.dataevent.group

import android.arch.lifecycle.Observer
import java.lang.IllegalStateException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Suppress("UNCHECKED_CAST")
abstract class OrderDataObserver<T> : Observer<Any> {
    private var mType: Type

    init {
        val type = javaClass.genericSuperclass as? ParameterizedType
                ?: throw IllegalStateException("don't have parametrizedType!")
        mType = type.actualTypeArguments[0]
    }

    abstract fun onReceived(t: T?): Boolean
    override fun onChanged(t: Any?) {
        if (t?.javaClass == mType) {
            onReceived(t = t as? T)
        }
    }
}