package com.github.razertexz

import android.content.Context

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin

@AliucordPlugin(requiresRestart = false)
internal class Main : Plugin() {
    private val simpleAst = SimpleASTAPI()

    override fun start(ctx: Context) {}
    override fun stop(ctx: Context) {}
}