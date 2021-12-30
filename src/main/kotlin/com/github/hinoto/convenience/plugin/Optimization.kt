package com.github.hinoto.convenience.plugin

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class Optimization(private val plugin: JavaPlugin) {

    private val server = Bukkit.getServer()

    private val removeWarning = object: BukkitRunnable() {
        override fun run() {
            server.broadcast(Component.text("1분 뒤 모든 떨어진 아이템이 사라집니다."))
        }
    }

    private val itemRemove = object: BukkitRunnable() {
        override fun run() {
            var count = 0
            for(world in server.worlds) {
                for(e in world.entities) {
                    if(e.type == EntityType.DROPPED_ITEM) {
                        count += (e as Item).itemStack.amount
                        e.remove()
                    }
                }
            }
            server.broadcast(Component.text(count.toString() + "개의 아이템이 사라졌습니다."))
        }
    }

    fun init() {
        removeWarning.runTaskTimer(plugin, 20L*60L*4L, 20L*60L*5L)
        itemRemove.runTaskTimer(plugin, 20L*60L*5L, 20L*60L*5L)
    }

}