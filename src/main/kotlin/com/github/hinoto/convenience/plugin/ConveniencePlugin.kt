package com.github.hinoto.convenience.plugin

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class ConveniencePlugin : JavaPlugin() {
    val recipe = Recipe(this)

    override fun onEnable() {
        logger.info("Convenience Plugin Enabled")
        Bukkit.getPluginManager().registerEvents(Farmer(), this)
        Bukkit.getPluginManager().registerEvents(ConnectListener(), this)
        recipe.addRecipe()
    }

    override fun onDisable() {
        logger.info("Convenience Plugin Disabled")
    }

    inner class ConnectListener : Listener {
        @EventHandler
        fun onPlayerJoin(e: PlayerJoinEvent) {
            recipe.addRecipeToBook(e.player)
        }
    }
}