package com.github.razertexz

import android.content.Context

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin

import com.discord.api.presence.Presence
import com.discord.api.activity.Activity
import com.discord.api.activity.ActivityType

import de.robv.android.xposed.XC_MethodHook

@AliucordPlugin(requiresRestart = false)
internal class YAFNP : Plugin() {
    override fun start(context: Context) {
        val map = HashMap<Long, String>()

        patcher.patch(Presence::class.java.declaredConstructors[0], object : XC_MethodHook() {
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                val activities = param.args[1] as List<Activity>
                val userId = param.args[4] as Long? ?: return

                for (activity in activities) {
                    if (activity.p() == ActivityType.CUSTOM_STATUS) {
                        val state = activity.l()
                        if (state != null) {
                            val hiddenText = extractHiddenText(state)
                            if (hiddenText.isNotEmpty()) {
                                map[userId] = hiddenText
                            }
                        }

                        break
                    }
                }
            }
        })
    }

    override fun stop(context: Context) = patcher.unpatchAll()
}