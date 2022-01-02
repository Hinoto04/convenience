package com.github.hinoto.convenience.plugin

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.text.DecimalFormat
import kotlin.math.floor


class FunctionItem(private val plugin: JavaPlugin) : Listener {

    private val server = Bukkit.getServer()
    var inv: Inventory = Bukkit.createInventory(null, InventoryType.CHEST, Component.text("공유 상자"))

    private val autoSave = object: BukkitRunnable() {
        override fun run() {
            save()
        }
    }

    fun init() {
        shareChestSetup()
        autoSave.runTaskTimer(plugin, 20L*60L*5L, 20L*60L*5L)
    }

    private fun shareChestSetup() {
        if(!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdir()
        }

        val path = plugin.dataFolder.path + File.separator + "data.yml"

        val data = File(path)
        val dataConfig = YamlConfiguration.loadConfiguration(data)

        if(!data.exists()) {
            plugin.saveResource("data.yml", false)
        } else {
            try {
                dataConfig.save(data)
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }

        val shareChest = dataConfig.getConfigurationSection("ShareChest")
        if(shareChest != null) {
            var invClone = inv.storageContents.clone()
            for((pos, key) in shareChest.getKeys(false).withIndex()) {
//                server.broadcast(Component.text(
//                    ItemStack.deserialize(shareChest.getConfigurationSection(key)!!.getValues(true)).toString()
//                ))
                invClone[pos] =
                    ItemStack.deserialize(shareChest.getConfigurationSection(key)!!.getValues(true))
            }
            inv.apply {
                storageContents = invClone
            }
        }
    }

    fun save() {
        val path = plugin.dataFolder.path + File.separator + "data.yml"

        val data = File(path)
        val dataConfig = YamlConfiguration.loadConfiguration(data)

        if(!data.exists()) {
            plugin.saveResource("data.yml", false)
        } else {
            try {
                dataConfig.save(data)
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
        dataConfig.set("ShareChest", null)
        inv.contents?.let {
            for((pos, i) in it.withIndex()) {
                if(i != null) {
                    //server.broadcast(Component.text("$pos "+i.serialize()))
                    dataConfig.set("ShareChest.i$pos", i.serialize())
                }
            }
            try {
                dataConfig.save(data);
            }catch (e: Exception){
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {

        val p = e.player
        val item = p.inventory.itemInMainHand

        //나침반 : 현재 위치 공유
        if(item.type == Material.COMPASS &&
            e.action == Action.RIGHT_CLICK_AIR &&
            !p.hasCooldown(Material.COMPASS)) {
            var str = "<" + e.player.name + "> "

            str += when (p.world.environment) {
                World.Environment.NORMAL -> {
                    "Overworld"
                }
                World.Environment.NETHER -> {
                    "Nether"
                }
                World.Environment.THE_END -> {
                    "The End"
                }
                else -> {
                    "Custom"
                }
            }
            str += ' '
            str += p.location.blockX.toString() + ' ' +
                    p.location.blockY.toString() + ' ' +
                    p.location.blockZ.toString()

            server.broadcast(Component.text(str))
            p.setCooldown(Material.COMPASS, 60)
        }

        //시계 : 현재 시각 확인
        if(item.type == Material.CLOCK &&
            (e.action == Action.RIGHT_CLICK_AIR ||
            e.action == Action.RIGHT_CLICK_BLOCK) &&
            !p.hasCooldown(Material.CLOCK)) {
            var str = ""
            val format = DecimalFormat("00")
            var time = p.world.time

            str += format.format(((time/1000)+6)%24) + ':'
            time %=1000
            str += format.format(floor(time/((50).toDouble()/(3).toDouble())))

            p.sendMessage(str)
            p.setCooldown(Material.CLOCK, 60)
        }

        //제작대 : 설치 하지 않고 즉시 실행
        if(item.type == Material.CRAFTING_TABLE &&
            !p.isSneaking &&
            !p.hasCooldown(Material.CRAFTING_TABLE)) {
            e.isCancelled = true
            val inv = Bukkit.createInventory(null, InventoryType.WORKBENCH, Component.text("내 손 안의 제작대"))
            p.openInventory(inv)
        }

        //상자 카트 : 서버 전체 인원이 공유하는 상자 열기
        if(item.type == Material.CHEST_MINECART &&
            !p.isSneaking) {
            e.isCancelled = true
            p.openInventory(inv)
        }

        //공유 상자 테스트용
//        if(item.type == Material.CHEST_MINECART) {
//            save()
//        } else if(item.type == Material.FURNACE_MINECART) {
//            shareChestSetup()
//        }
    }

    //인벤토리 탈출 시 아이템 드랍
    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        server.broadcast(Component.text(e.inventory.type.toString()))
        server.broadcast(Component.text(e.inventory.holder.toString()))
    }
}