package com.github.razertexz

import android.content.Context

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin

import com.discord.stores.StoreStream
import com.discord.models.presence.Presence
import com.discord.utilities.icon.IconUtils

import de.robv.android.xposed.XC_MethodHook

@AliucordPlugin(requiresRestart = false)
internal class YAFNP : Plugin() {
    override fun start(context: Context) {
        patcher.patch(IconUtils::class.java, "getForUser", arrayOf(Long::class.javaObjectType, String::class.java, Int::class.javaObjectType, Boolean::class.java, Int::class.javaObjectType), object : XC_MethodHook() {
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                val presence = getPresence(param.args[0] as Long?) ?: return
            }
        })

        patcher.patch(IconUtils::class.java, "getForUserBanner", arrayOf(Long::class.java, String::class.java, Int::class.javaObjectType, Boolean::class.java), object : XC_MethodHook() {
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                val presence = getPresence(param.args[0] as Long) ?: return
            }
        })
    }

    override fun stop(context: Context) = patcher.unpatchAll()

    private fun getPresence(userId: Long?): Presence? = if (userId != null) (StoreStream.getPresences().getPresences() as MutableMap<Long, Presence>)[userId] else null
}