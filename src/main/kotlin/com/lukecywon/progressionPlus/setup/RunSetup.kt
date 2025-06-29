package com.lukecywon.progressionPlus.setup

import com.lukecywon.progressionPlus.annotations.RunOnEnable
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import java.lang.reflect.Modifier

/**
 * Class that runs all methods that are to be run onEnable
 * Any class annotated with [RunOnEnable]
 */
class RunSetup(private val plugin: JavaPlugin) {
    /**
     * Call runSetupMethods on initialization
     */
    init {
        runSetupMethods()
    }

    /**
     * Uses classpath scanning to run all methods annotated with [RunOnEnable]
     */
    private fun runSetupMethods() {
        val methods = Reflections("com.lukecywon.progressionPlus", Scanners.MethodsAnnotated)
            .getMethodsAnnotatedWith(RunOnEnable::class.java)
            .sortedByDescending { it.getAnnotation(RunOnEnable::class.java).priority }

        println(methods)

        methods.forEach { method ->
            println(method.name)
            if (Modifier.isStatic(method.modifiers)) return@forEach
            val parentClass = method.declaringClass

            try {
                val instance = parentClass.getDeclaredConstructor().newInstance()
                method.isAccessible = true

                if (method.parameterCount == 1 && method.parameterTypes[0] == JavaPlugin::class.java) {
                    method.invoke(instance, plugin)
                } else {
                    method.invoke(instance)
                }
            } catch (e: Exception) {
                plugin.logger.warning("Failed to run setup method: ${method.name} in ${parentClass.name}")
                e.printStackTrace()
            }

        }
    }
}