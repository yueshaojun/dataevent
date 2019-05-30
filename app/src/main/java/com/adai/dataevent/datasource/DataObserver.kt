package com.adai.dataevent.datasource

import android.arch.lifecycle.Observer
import java.lang.IllegalStateException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * only directed subClass has been supported now
 */
@Suppress("UNCHECKED_CAST")
abstract class DataObserver<T> : Observer<Any> {
    private var mType: Type

    init {
        val type = javaClass.genericSuperclass as? ParameterizedType
                ?: throw IllegalStateException("don't have parametrizedType!")
        mType = type.actualTypeArguments[0]
    }

    abstract fun onReceived(t: T?)
    override fun onChanged(t: Any?) {
        if (t?.javaClass == mType) {
            onReceived(t = t as? T)
        }
    }
}