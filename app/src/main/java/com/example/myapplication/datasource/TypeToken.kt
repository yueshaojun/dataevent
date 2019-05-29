package com.example.myapplication.datasource

import java.lang.IllegalStateException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

open class TypeToken<T> {
    private var mTypeToken: Type

    init {
        val type = javaClass.genericSuperclass as? ParameterizedType ?:
        throw IllegalStateException("don't have parametrizedType!")
        mTypeToken = type.actualTypeArguments[0]
    }
    fun getType():Type{
        return mTypeToken
    }
}