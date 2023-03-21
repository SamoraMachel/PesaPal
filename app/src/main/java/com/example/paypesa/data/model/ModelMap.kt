package com.example.paypesa.data.model

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

open class ModelMap {
    fun asMap(): Map<String, Any?> {
        val props = this::class.memberProperties.associateBy { it.name }
        return props.keys.associateWith { props[it] }
    }
}

fun <T : Any> toMap(obj: T): Map<String, Any?> {
    return (obj::class as KClass<T>).memberProperties.associate { prop ->
        prop.name to prop.get(obj)?.let { value ->
            if (value::class.isData) {
                toMap(value)
            } else {
                value
            }
        }
    }
}