package com.github.hinoto.convenience.plugin

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.data.Ageable
import org.bukkit.block.data.Levelled
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class Farmer : Listener {

    private val random = java.util.Random()
    private fun rand(from: Int, to: Int) : Int {
        return random.nextInt(to - from) + from
    }

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

    //퇴비통 투입 가능 리스트
    private val canCompost = listOf(
        Material.POISONOUS_POTATO,
    )

    private val compostChance = 0.3

    fun tryCompost(item: ItemStack, composter: Block) {
        if(item.type !in canCompost) {
            return
        }
        item.amount--
        if(rand(0, 100)<compostChance*100) {
            composter.world.playSound(
                composter.location,
                Sound.BLOCK_COMPOSTER_FILL,
                1.0f,
                1.0f)
        } else {
            composter.world.playSound(
                composter.location,
                Sound.BLOCK_COMPOSTER_FILL_SUCCESS,
                1.0f,
                1.0f)
            val data: Levelled = composter.blockData.clone() as Levelled
            data.level++
            composter.blockData = data
        }
    }

    //퇴비통 사용 시 작동
    @EventHandler
    fun onComposterInteract(e: PlayerInteractEvent) {
        if(e.action == Action.RIGHT_CLICK_BLOCK &&
                e.clickedBlock != null &&
                e.item != null &&
                e.clickedBlock!!.type == Material.COMPOSTER) {
            val composter: Block = e.clickedBlock!!
            tryCompost(e.item!!, composter)
        }
    }

    @EventHandler
    fun onHopperToComposter(e: InventoryMoveItemEvent) {
        if(e.source.type.name.equals("HOPPER", true) &&
                e.destination.type.name.equals("COMPOSTER", true)) {
            val composter = e.source.location!!.clone().apply {y-=1}.block
            tryCompost(e.item, composter)
        }
    }
}