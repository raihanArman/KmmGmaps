package com.randev.kmmgmaps.network.data

/**
 * @author Raihan Arman
 * @date 01/10/24
 */
data class Place(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val coordinate: Coordinate = Coordinate()
)