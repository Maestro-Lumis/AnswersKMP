package com.example.answers

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform