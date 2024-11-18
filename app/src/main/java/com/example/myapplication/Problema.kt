package com.example.myapplication


data class Problema(
    val enunciado: String,
    val opciones: List<String>,
    val respuestaCorrecta: String
)

//Para futuras implementaciones de problemas para ahorcado, etc... se pueden crear mas clases