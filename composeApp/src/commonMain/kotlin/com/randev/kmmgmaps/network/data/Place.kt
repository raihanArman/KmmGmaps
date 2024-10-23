package com.randev.kmmgmaps.network.data

/**
 * @author Raihan Arman
 * @date 01/10/24
 */
data class Place(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val coordinate: Coordinate = Coordinate(),
    val distance: Int = -1
) {
    companion object {
        val Empty: Place = Place()
    }

    fun distanceOnKm(): String {
        val kilo = distance.toDouble() / 1000
        val kiloString = kilo.toString().replace(".", ",")

        val kiloStringSplit = kiloString.split(",")
        val kiloBeforeComma = kiloStringSplit[0]
        val kiloAfterComma = (kiloStringSplit.getOrNull(1) ?: "0")
            .take(1)

        return "$kiloBeforeComma,$kiloAfterComma km"
    }
}