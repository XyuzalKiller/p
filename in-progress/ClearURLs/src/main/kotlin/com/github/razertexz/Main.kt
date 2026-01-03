package com.github.razertexz

import android.content.Context

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.Utils
import com.aliucord.Http

import com.discord.utilities.uri.UriHandler

import kotlin.jvm.functions.Function0

import de.robv.android.xposed.XC_MethodHook

@AliucordPlugin(requiresRestart = false)
internal class Main : Plugin() {
    private class Provider(
        @JvmField val urlPattern: String,
        @JvmField val rules: Array<String>?,
        @JvmField val rawRules: Array<String>?,
        @JvmField val exceptions: Array<String>?,
        @JvmField val redirections: Array<String>?
    )

    override fun start(ctx: Context) {
        val rules = Http.simpleJsonGet("https://rules2.clearurls.xyz/data.minify.json", HashMap<String, Provider>::class.java)

        patcher.patch(UriHandler::class.java, "handle", arrayOf(Context::class.java, String::class.java, Boolean::class.java, Boolean::class.java, Function0::class.java), object : XC_MethodHook(-10000) {
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                val originalUrl = param.args[1] as String
                Utils.showToast(rules["adtech"].urlPattern)
            }
        })
    }

    override fun stop(ctx: Context) = patcher.unpatchAll()
}