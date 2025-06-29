package com.lukecywon.progressionPlus.setup

import com.lukecywon.progressionPlus.utils.annotations.InjectPlugin
import com.lukecywon.progressionPlus.utils.annotations.RunOnEnable
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections

/**
 * Class containing the functionality of auto dependency injection
 * of the plugin instance for any fields with the [InjectPlugin] annotation.
 */
class PluginInjector {
    @RunOnEnable(999)
    fun injectPlugin(plugin: JavaPlugin) {
        val reflections = Reflections("com.lukecywon.progressionPlus")
        val classes = reflections.getSubTypesOf(Any::class.java)

        classes.forEach { cls ->
            try {
                val instance = cls.getDeclaredConstructor().newInstance()
                inject(instance, plugin)
            } catch (e: Exception) {
                plugin.logger.warning("Injection failed for ${cls.name}: ${e.message}")
            }
        }
    }

    private fun inject(target: Any, plugin: JavaPlugin) {
        target::class.java.declaredFields
            .filter { it.isAnnotationPresent(InjectPlugin::class.java) }
            .forEach { field ->
                field.isAccessible = true
                if (field.type.isAssignableFrom(JavaPlugin::class.java)) {
                    field.set(target, plugin)
                }
            }
    }
}