package br.com.leandro.parking.model

import java.util.*

data class Entrance(
    val reservation: String?,
    val plate: String?,
    val entered_at: Date?,
    val errors: Plate?
)

data class Plate(
    val plate: List<String>
)