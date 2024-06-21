package dev.rafi.ezwhitelist.velocity.tasks

import com.velocitypowered.api.scheduler.ScheduledTask
import dev.rafi.ezwhitelist.common.services.ConfigService
import dev.rafi.ezwhitelist.common.services.ServerService
import dev.rafi.ezwhitelist.velocity.EzWhiteListVelocity
import java.time.Duration

class SaveTask {
    companion object {
        fun startSaveTask(interval: Long): ScheduledTask? {
            if (ConfigService.SAVE_INSTANT) {
                return null
            }
            return EzWhiteListVelocity.proxyServer.scheduler.buildTask(EzWhiteListVelocity.inst, Runnable {
                ServerService.serverList.forEach {
                    if (it.editCount <= 0) return@forEach
                    ServerService.upsertServer(it)
                    it.editCount = 0
                }
            })
                .repeat(Duration.ofSeconds(interval))
                .schedule()
        }
    }
}