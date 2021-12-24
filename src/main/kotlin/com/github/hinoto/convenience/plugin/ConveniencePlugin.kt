package com.github.hinoto.convenience.plugin

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ConveniencePlugin : JavaPlugin() {
    override fun onEnable() {
        logger.info("Convenience Plugin Enabled")
        Bukkit.getPluginManager().registerEvents(Farmer(), this)
    }

    override fun onDisable() {
        logger.info("Convenience Plugin Disabled")
    }
}