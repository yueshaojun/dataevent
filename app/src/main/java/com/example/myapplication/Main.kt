package com.example.myapplication

import com.example.myapplication.datasource.TypeToken

fun main(args: Array<String>) {
    val type = object : TypeToken<Test>(){}.getType()
    println("rawType = $type")
}