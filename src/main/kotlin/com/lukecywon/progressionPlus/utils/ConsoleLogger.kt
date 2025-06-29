package com.lukecywon.progressionPlus.utils

object ConsoleLogger {
    private const val PREFIX_STRING = "[ProgressionPlus] "

    fun log(msg: String) {
        println(PREFIX_STRING + msg)
    }
}