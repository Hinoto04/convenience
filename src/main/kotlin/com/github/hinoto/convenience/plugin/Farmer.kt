package com.github.hinoto.convenience.plugin

import org.bukkit.Material
import org.bukkit.block.data.Ageable
import org.bukkit.block.data.Directional
import org.bukkit.block.data.type.Cocoa
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDropItemEvent

class Farmer : Listener {

    //방향 상관없는 작물
    private val checkList = arrayOf(
        arrayOf(Material.WHEAT, Material.WHEAT_SEEDS),
        arrayOf(Material.POTATOES, Material.POTATO),
        arrayOf(Material.CARROTS, Material.CARROT),
        arrayOf(Material.BEETROOTS, Material.BEETROOT_SEEDS),
        arrayOf(Material.COCOA, Material.COCOA_BEANS),
        arrayOf(Material.NETHER_WART, Material.NETHER_WART)
    )



    //블럭 파괴 이벤트
    @EventHandler
    fun onBlockDropItem(e: BlockDropItemEvent) {
        for(m in checkList) {
            if(e.blockState.type == m[0]) {
                val inv = e.player.inventory
                if(inv.itemInOffHand.type == m[1]) {
                    inv.itemInOffHand.amount--
                    e.block.type = m[0]
                    e.block.blockData = (e.blockState.blockData as Ageable).apply { age = 0; }
                }
            }
        }
    }


}