package com.github.hinoto.convenience.plugin

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ConveniencePlugin : JavaPlugin() {

    val recipe = Recipe(this)

    override fun onEnable() {
        logger.info("Convenience Plugin Enabled")

        //농사 관련 편의성 개선
        Bukkit.getPluginManager().registerEvents(Farmer(), this)

        //레시피 관련 편의성 개선
        Bukkit.getPluginManager().registerEvents(recipe.RecipeAdder(), this)
        recipe.addRecipe()
    }

    override fun onDisable() {
        logger.info("Convenience Plugin Disabled")
    }
}