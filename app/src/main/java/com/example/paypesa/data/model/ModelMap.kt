package com.example.paypesa.data.model

import java.util.*

open class ModelMap {
    fun asMap(): Map<String, Any?> {
//        val props = this::class.memberProperties.associateBy { it.name }
//        return props.keys.associateWith { props[it] }
        return mapOf()
    }
}