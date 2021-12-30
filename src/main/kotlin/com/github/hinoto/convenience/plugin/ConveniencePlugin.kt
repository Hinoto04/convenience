package com.github.hinoto.convenience.plugin

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ConveniencePlugin : JavaPlugin() {

    val recipe = Recipe(this)

    override fun onEnable() {
        logger.info("Convenience Plugin Enabled")

        val pm = Bukkit.getPluginManager()

        //농사 관련 편의성 개선
        pm.registerEvents(Farmer(), this)

        //레시피 관련 편의성 개선
        pm.registerEvents(recipe.RecipeAdder(), this)
        recipe.addRecipe()

        //기능성 아이템 관련 편의성 개선
        pm.registerEvents(FunctionItem(), this)

        //최적화 관련 편의성
        Optimization(this).init()
    }

    override fun onDisable() {
        logger.info("Convenience Plugin Disabled")
    }
}