package com.github.razertexz

import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.View
import android.app.Activity

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.Utils

import com.discord.app.AppActivity

import de.robv.android.xposed.XC_MethodHook

@AliucordPlugin(requiresRestart = false)
class ASS : Plugin() {
    override fun start(context: Context) {
        patcher.patch(Activity::class.java, "dispatchTouchEvent", arrayOf(MotionEvent::class.java), object : XC_MethodHook() {
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                val event = param.args[0] as MotionEvent
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val view = findDeepestView((param.thisObject as Activity).window.decorView.rootView, event.rawX.toInt(), event.rawY.toInt()) ?: return
                    val id = if (view.id == View.NO_ID) {
                        "none"
                    } else {
                        view.resources.getResourceEntryName(view.id)
                    }

                    Utils.showToast("Found view named ${view.javaClass.name} with ID $id")
                }
            }
        })
    }

    override fun stop(context: Context) = patcher.unpatchAll()

    private fun findDeepestView(view: View, x: Int, y: Int): View? {
        if (view.visibility != View.VISIBLE) return null

        val location = IntArray(2)
        view.getLocationOnScreen(location)

        val left = location[0]
        val top = location[1]
        val right = left + view.width
        val bottom = top + view.height

        if (x < left || x > right || y < top || y > bottom) {
            return null
        }

        if (view is ViewGroup) {
            var i = view.childCount - 1
            while (i >= 0) {
                val deeperView = findDeepestView(view.getChildAt(i), x, y)
                if (deeperView != null) {
                    return deeperView
                }

                i--
            }
        }

        return view
    }
}