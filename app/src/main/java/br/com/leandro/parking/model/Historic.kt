package br.com.leandro.parking.model

import java.io.Serializable

data class Historic(
    val time: String,
    val paid: Boolean,
    val left: Boolean,
    val plate: String,
    val reservation: String
) : Serializable
