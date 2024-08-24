package com.randev.kmmgmaps

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform