package com.github.razertexz

import androidx.recyclerview.widget.RecyclerView
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View

import com.discord.widgets.chat.list.entries.*
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapter
import com.discord.widgets.chat.list.actions.WidgetChatListActions
import com.discord.models.message.Message
import com.discord.stores.StoreStream

import com.lytefast.flexinput.R

import kotlin.math.abs

class SwipeHelper {
    private lateinit var recyclerView: RecyclerView
    private lateinit var replyIcon: Drawable
    private lateinit var editIcon: Drawable

    private val widgetChatListActions = WidgetChatListActions()
    private val storeChannels = StoreStream.getChannels()
    private val myId = StoreStream.getUsers().me.id

    private val onItemTouchListener = object : RecyclerView.OnItemTouchListener {
        private lateinit var msg: Message
        private var selectedView: View? = null
        private var initialX = 0.0f
        private var initialY = 0.0f

        private val replyThreshold = 0.25f
        private val editThreshold = 0.5f

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    val child = rv.findChildViewUnder(e.x, e.y) ?: return false
                    val pos = rv.getChildViewHolder(child).adapterPosition
                    if (pos == RecyclerView.NO_POSITION) return false

                    val entry = (rv.adapter as WidgetChatListAdapter).getData().getList()[pos]
                    msg = when (entry) {
                        is MessageEntry -> entry.message
                        is AttachmentEntry -> entry.message
                        is StickerEntry -> entry.message
                        is EmbedEntry -> entry.message
                        else -> return false
                    }

                    selectedView = child
                    initialX = e.x
                    initialY = e.y
                }

                MotionEvent.ACTION_MOVE -> {
                    if (selectedView == null || rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) return false

                    val diffX = e.x - initialX
                    val diffY = e.y - initialY
                    if (diffX < 0.0f && abs(diffX) > abs(diffY)) {
                        rv.requestDisallowInterceptTouchEvent(true)
                        return true
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    selectedView = null
                }
            }

            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            selectedView!!.let { view ->
                when (e.actionMasked) {
                    MotionEvent.ACTION_MOVE -> {
                        val diffX = e.x - initialX
                        if (diffX < 0.0f)
                            view.translationX = diffX
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val absTranslationX = abs(view.translationX)
                        val viewWidth = view.width

                        if (absTranslationX > view.width * replyThreshold)
                            WidgetChatListActions.`access$replyMessage`(widgetChatListActions, msg, storeChannels.getChannel(msg.channelId))
                        else if (absTranslationX > view.width * editThreshold && msg.author.id == myId)
                            WidgetChatListActions.`access$editMessage`(widgetChatListActions, msg)

                        view.translationX = 0.0f
                        selectedView = null
                    }
                }
            }
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    }

    fun attachToRecyclerView(rv: RecyclerView) {
        if (this::recyclerView.isInitialized) {
            if (recyclerView == rv) return

            recyclerView.removeOnItemTouchListener(onItemTouchListener)
        }

        recyclerView = rv.apply {
            replyIcon = context.getDrawable(R.e.ic_reply_24dp)!!.mutate()
            editIcon = context.getDrawable(R.e.ic_edit_24dp)!!.mutate()

            addOnItemTouchListener(onItemTouchListener)
        }
    }
}
