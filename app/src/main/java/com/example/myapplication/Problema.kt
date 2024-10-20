package com.example.myapplication


data class Problema(
    val problema: String,
    val opciones: List<String>,
    val correcta: String
)

//Para futuras implementaciones de problemas para ahorcado, etc... se pueden crear mas clases