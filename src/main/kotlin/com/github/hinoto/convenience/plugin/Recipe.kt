package com.github.hinoto.convenience.plugin

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Tag
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class Recipe(private val plugin: JavaPlugin) {
    private val enableRecipes = listOf(
        getPackedIce(),
        getSaddle(),
        getNameTag(),
        getEnchantedGoldenApple(),
    )
    private val enableItem = listOf(
        "packed_ice",
        "saddle",
        "name_tag",
        "enchanted_golden_apple",
    )

    fun addRecipe() {
        for(recipe in enableRecipes) {
            Bukkit.getServer().addRecipe(recipe)
        }
    }

    fun addRecipeToBook(player: Player) {
        val recipeList: List<NamespacedKey> = emptyList()
        for(i in enableItem) {
            recipeList.plus(NamespacedKey(this.plugin, i))
        }
        player.discoverRecipes(recipeList)
    }

    private fun getPackedIce(): ShapedRecipe {
        val item = ItemStack(Material.PACKED_ICE)
        val key = NamespacedKey(this.plugin, "packed_ice")
        val recipe = ShapedRecipe(key, item)
        recipe.shape(" B ","IWI","III");
        recipe.setIngredient('B', Material.WATER_BUCKET)
        recipe.setIngredient('I', Material.ICE)
        recipe.setIngredient('W', RecipeChoice.MaterialChoice(Tag.WOOL))
        return recipe
    }

    private fun getSaddle(): ShapedRecipe {
        val item = ItemStack(Material.SADDLE)
        val key = NamespacedKey(this.plugin, "saddle")
        val recipe = ShapedRecipe(key, item)
        recipe.shape("LLL","LSL")
        recipe.setIngredient('L', Material.LEATHER)
        recipe.setIngredient('S', Material.STRING)
        return recipe
    }

    private fun getNameTag(): ShapedRecipe {
        val item = ItemStack(Material.NAME_TAG)
        val key = NamespacedKey(this.plugin, "name_tag")
        val recipe = ShapedRecipe(key, item)
        recipe.shape("SPP")
        recipe.setIngredient('S', Material.STRING)
        recipe.setIngredient('P', Material.PAPER)
        return recipe
    }

    private fun getEnchantedGoldenApple(): ShapedRecipe {
        val item = ItemStack(Material.ENCHANTED_GOLDEN_APPLE)
        val key = NamespacedKey(this.plugin, "enchanted_golden_apple")
        val recipe = ShapedRecipe(key, item)
        recipe.shape("GGG", "GAG", "GGG")
        recipe.setIngredient('G', Material.GOLD_BLOCK)
        recipe.setIngredient('A', Material.APPLE)
        return recipe
    }
}