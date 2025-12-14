package com.example.souqseller.activities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDeleteCallback(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val deleteBackgroundColor = Color.parseColor("#D32F2F")
    private val clearPaint = Paint().apply { xfermode = null }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 40f
        isAntiAlias = true
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val itemView = viewHolder.itemView
        val background = Paint().apply { color = deleteBackgroundColor }

        // ارسم الخلفية الحمراء
        c.drawRect(
            itemView.right + dX,
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat(),
            background
        )

        // نص "حذف"
        c.drawText(
            "حذف",
            itemView.right - 120f,
            itemView.top + itemView.height / 2f + 15f,
            textPaint
        )

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
