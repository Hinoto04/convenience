package com.github.hinoto.convenience.plugin

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.block.data.Directional
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.text.DecimalFormat

class FunctionItem : Listener {

    private val server = Bukkit.getServer()

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {

        val p = e.player

        //공기 중 우클릭 조건
        if(e.action == Action.RIGHT_CLICK_AIR) {

            //나침반 : 현재 위치 공유
            if(p.inventory.itemInMainHand.type == Material.COMPASS) {
                var str = "<" + e.player.name + "> "

                if(p.world.environment == World.Environment.NORMAL) {
                    str += "Overworld"
                } else if(p.world.environment == World.Environment.NETHER) {
                    str += "Nether"
                } else if(p.world.environment == World.Environment.THE_END) {
                    str += "The End"
                } else {
                    str += "Custom"
                }
                str += ' '
                str += p.location.blockX.toString() + ' ' +
                        p.location.blockY.toString() + ' ' +
                        p.location.blockZ.toString()

                server.broadcast(Component.text(str))
            }
            //시계 : 현재 시각 확인
            else if(p.inventory.itemInMainHand.type == Material.CLOCK) {
                var str = ""
                val format = DecimalFormat("00")
                var time = p.world.time

                str += format.format(((time/1000)+6)%24) + ':'
                time %=1000
                str += format.format(Math.floor(time/((50).toDouble()/(3).toDouble())))

                p.sendMessage(str)
            }
        }
    }
}