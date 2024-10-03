package com.randev.kmmgmaps.network.data

/**
 * @author Raihan Arman
 * @date 02/10/24
 */
data class Coordinate(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    override fun toString(): String {
        return "$latitude,$longitude"
    }
}