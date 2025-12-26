package com.github.razertexz

import android.content.Context

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.Utils

import com.discord.utilities.uri.UriHandler

import kotlin.jvm.functions.Function0

import de.robv.android.xposed.XC_MethodHook

@AliucordPlugin(requiresRestart = false)
class Main : Plugin() {
    override fun start(ctx: Context) {
        patcher.patch(UriHandler::class.java, "handle", arrayOf(Context::class.java, String::class.java, Boolean::class.java, Boolean::class.java, Function0::class.java), object : XC_MethodHook(-10000) {
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                clearUrl(param.args[1] as String)
            }
        })
    }

    override fun stop(ctx: Context) = patcher.unpatchAll()

    private fun clearUrl(url: String): String {
        Utils.showToast(url)

        return ""
    }
}