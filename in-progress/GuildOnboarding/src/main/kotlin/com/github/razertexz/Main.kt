package com.github.razertexz

import android.content.Context

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*
import com.aliucord.Http
import com.aliucord.Utils
import com.aliucord.utils.GsonUtils

import com.google.gson.stream.JsonReader

import java.io.InputStreamReader

@AliucordPlugin(requiresRestart = false)
class Main : Plugin() {
    private data class Onboarding(
        val prompts: List<OnboardingPrompt>
    ) {
        private data class OnboardingPrompt(
            val options: List<OnboardingPromptOption>,
            val title: String,
            val single_select: Boolean,
            val required: Boolean
        ) {
            private data class OnboardingPromptOption(
                val title: String,
                val description: String?
            )
        }
    }

    override fun start(ctx: Context) {
        Utils.threadPool.execute {
            val data = GsonUtils.gsonRestApi.d<Onboarding>(JsonReader(InputStreamReader(Http.Request.newDiscordRNRequest("https://discord.com/api/v10/guilds/1359064958872195143/onboarding", "GET").execute().stream())), Onboarding::class.java)
            logger.warn(data.toString())
        }
    }

    override fun stop(ctx: Context) = patcher.unpatchAll()
}
