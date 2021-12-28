package com.github.hinoto.convenience.plugin

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.block.data.Ageable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class Farmer : Listener {

    //작물 리스트
    private val checkList = arrayOf(
        arrayOf(Material.WHEAT, Material.WHEAT_SEEDS),
        arrayOf(Material.POTATOES, Material.POTATO),
        arrayOf(Material.CARROTS, Material.CARROT),
        arrayOf(Material.BEETROOTS, Material.BEETROOT_SEEDS),
        arrayOf(Material.COCOA, Material.COCOA_BEANS),
        arrayOf(Material.NETHER_WART, Material.NETHER_WART)
    )

    //수확 시 작동
    @EventHandler
    fun onBlockDropItem(e: BlockDropItemEvent) {
        for(m in checkList) {
            if(e.blockState.type == m[0]) {
                val inv = e.player.inventory
                if(inv.itemInOffHand.type == m[1]) {
                    inv.itemInOffHand.amount--
                    e.block.type = m[0]
                    //방향 바뀌는 문제로 인해, 블럭 자체를 복사해 나이만 수정
                    e.block.blockData = (e.blockState.blockData as Ageable).apply { age = 0; }
                }
            }
        }
    }

    //상호작용 이벤트 (퇴비통 사용 관련)
    @EventHandler
    fun onInteractBlock(e: PlayerInteractEvent) {
        if(e.action == Action.RIGHT_CLICK_BLOCK &&
                e.hasBlock() &&
                e.clickedBlock?.type == Material.COMPOSTER &&
                e.hasItem() &&
                e.item?.type == Material.POISONOUS_POTATO) {

        }
    }
}