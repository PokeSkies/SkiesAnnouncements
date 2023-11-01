package com.pokeskies.skiesannouncements.config

class MainConfig(
    var debug: Boolean = false
) {
    override fun toString(): String {
        return "MainConfig(debug=$debug)"
    }
}