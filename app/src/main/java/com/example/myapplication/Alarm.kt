package com.example.myapplication

import java.util.Calendar

data class Alarm(
    val id: Int,
    var time: String,
    var name: String,
    var periodicity: String,
    var isActive: Boolean,
    var ringTime: Calendar
)
