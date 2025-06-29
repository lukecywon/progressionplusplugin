package com.lukecywon.progressionPlus.annotations

/**
 * Annotation representing methods to be run on plugin start
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RunOnEnable(val priority: Int = 0)
