package com.lukecywon.progressionPlus.commands

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.time.ZoneId

class EndTimeCommand : CustomCommand {
    override val name: String = "endtime"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val config = ProgressionPlus.getPlugin().config
        val unlockTimeStr = config.getString("end_unlock_time", "2099-01-01T00:00")
        val unlockTime = try {
            LocalDateTime.parse(unlockTimeStr)
        } catch (e: Exception) {
            LocalDateTime.MAX
        }

        val now = LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"))
        val accessible = now.isAfter(unlockTime)

        sender.sendMessage("§6[End Access Time Check]")
        sender.sendMessage("§eCurrent time: §f$now")
        sender.sendMessage("§eUnlock time: §f$unlockTime")
        sender.sendMessage("§eStatus: §${if (accessible) "aUNLOCKED" else "cLOCKED"}")

        return true
    }
}
