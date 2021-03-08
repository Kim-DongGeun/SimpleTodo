package com.example.simpletodo

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class CompletedSwipeToDelete(val adapter: RecyclerViewAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if(direction == ItemTouchHelper.END){ // -> 완료
            return adapter.removeItem(viewHolder.adapterPosition)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val height : Int = viewHolder.itemView.height
        val width = height / 3
        var mBackground : ColorDrawable = ColorDrawable()

        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(dX > 0){ // ->
                mBackground.color = Color.parseColor("#3f51b5")
                mBackground.setBounds(viewHolder.itemView.left , viewHolder.itemView.top, viewHolder.itemView.left + dX.toInt(), viewHolder.itemView.bottom)
                mBackground.draw(c)

                val P : Paint = Paint()
                P.color = Color.parseColor("#FFFFFF")
                P.textSize = 20f * TypedValue.COMPLEX_UNIT_SP
                c.drawText("삭제", viewHolder.itemView.left.toFloat() + width, viewHolder.itemView.top.toFloat() + height * 0.6f, P)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}