package com.github.razertexz

import android.content.Context

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin

import com.discord.models.presence.Presence
import com.discord.api.activity.Activity
import com.discord.api.activity.ActivityType
import com.discord.api.presence.ClientStatus
import com.discord.api.presence.ClientStatuses

import de.robv.android.xposed.XC_MethodHook

@AliucordPlugin(requiresRestart = false)
internal class YAFNP : Plugin() {
    override fun start(context: Context) {
        patcher.patch(Presence::class.java, arrayOf(ClientStatus::class.java, ClientStatuses::class.java, List::class.java), object : XC_MethodHook() {
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                val activities = param.args[2] as List<Activity>
                for (activity in activities) {
                    if (activity.p() == ActivityType.CUSTOM_STATUS) {
                        logger.infoToast(activity.toString())
                        break
                    }
                }
            }
        })
    }

    override fun stop(context: Context) = patcher.unpatchAll()
}