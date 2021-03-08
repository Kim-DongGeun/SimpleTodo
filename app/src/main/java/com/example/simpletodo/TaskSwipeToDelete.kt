package com.example.simpletodo

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class TaskSwipeToDelete(val adapter: RecyclerViewAdapter) : ItemTouchHelper.Callback() {
    // 어떤 동작을 수행할지 정의
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.START or ItemTouchHelper.END)
    }
    // 드래그 했을 때
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if(direction == ItemTouchHelper.START) { // <- 미루기
            return adapter.delayItem(viewHolder.adapterPosition)
        }
        else if(direction == ItemTouchHelper.END){ // -> 완료
            return adapter.completeItem(viewHolder.adapterPosition)
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
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
            if(dX < 0){ // <-
                mBackground.color = Color.parseColor("#388E3C")
                mBackground.setBounds(viewHolder.itemView.right + dX.toInt(), viewHolder.itemView.top, viewHolder.itemView.right, viewHolder.itemView.bottom)
                mBackground.draw(c)

                val P : Paint = Paint()
                P.color = Color.parseColor("#FFFFFF")
                P.textSize = 20f * TypedValue.COMPLEX_UNIT_SP
                c.drawText("미루기", viewHolder.itemView.right.toFloat() - 3 * width, viewHolder.itemView.top.toFloat() + height * 0.6f, P)
            }
            else{
                mBackground.color = Color.parseColor("#D32F2F")
                mBackground.setBounds(viewHolder.itemView.left , viewHolder.itemView.top, viewHolder.itemView.left + dX.toInt(), viewHolder.itemView.bottom)
                mBackground.draw(c)

                val P : Paint = Paint()
                P.color = Color.parseColor("#FFFFFF")
                P.textSize = 20f * TypedValue.COMPLEX_UNIT_SP
                c.drawText("완료", viewHolder.itemView.left.toFloat() + width, viewHolder.itemView.top.toFloat() + height * 0.6f, P)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}