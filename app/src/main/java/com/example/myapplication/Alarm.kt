package com.example.myapplication

import java.util.Calendar

data class Alarm(
    val time: String,
    val name: String,
    val periodicity: String,
    var isActive: Boolean,
    val ringTime: Calendar
)
