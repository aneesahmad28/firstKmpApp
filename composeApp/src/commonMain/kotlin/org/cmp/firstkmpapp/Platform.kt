package org.cmp.firstkmpapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform