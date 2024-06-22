package com.example.homework1

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteCallback(context: Context, private val adapter: TodoAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteBackground = ColorDrawable(ContextCompat.getColor(context, R.color.Red))
    private val checkBackground = ColorDrawable(ContextCompat.getColor(context, R.color.Green))

    private val checkIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.check_white)!!
    private val deleteIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.delete_white)!!

    private val intrinsicWidthDelete = deleteIcon.intrinsicWidth
    private val intrinsicHeightDelete = deleteIcon.intrinsicHeight
    private val intrinsicWidthCheck = checkIcon.intrinsicWidth
    private val intrinsicHeightCheck = checkIcon.intrinsicHeight

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder.bindingAdapterPosition == adapter.itemCount - 1) {
            // Не разрешаем свайп последнего элемента
            0
        } else {
            val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            makeMovementFlags(0, swipeFlags)
        }
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // Мы не обрабатываем движение
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Удаление элемента из адаптера
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            adapter.deleteItem(position)
        } else if (direction == ItemTouchHelper.RIGHT) {
            adapter.checkItem(position)
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
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        if (dX > 0) { // Swiping to the right
            // Draw the green check background
            checkBackground.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt(),
                itemView.bottom
            )
            checkBackground.draw(c)

            // Calculate position of check icon
            val checkIconTop = itemView.top + (itemHeight - intrinsicHeightCheck) / 2
            val checkIconMargin = (itemHeight - intrinsicHeightCheck) / 2
            val checkIconLeft = itemView.left + checkIconMargin + dX.toInt() - 3 * intrinsicWidthCheck
            val checkIconRight = itemView.left + checkIconMargin + dX.toInt() - 2 * intrinsicWidthCheck
            val checkIconBottom = checkIconTop + intrinsicHeightCheck

            // Draw the check icon
            checkIcon.setBounds(checkIconLeft, checkIconTop, checkIconRight, checkIconBottom)
            checkIcon.draw(c)

        } else { // Swiping to the left
            // Draw the red delete background
            deleteBackground.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            deleteBackground.draw(c)

            // Calculate position of delete icon
            val deleteIconTop = itemView.top + (itemHeight - intrinsicHeightDelete) / 2
            val deleteIconMargin = (itemHeight - intrinsicHeightDelete) / 2
            val deleteIconLeft = itemView.right + dX.toInt() + deleteIconMargin
            val deleteIconRight = itemView.right + dX.toInt() + deleteIconMargin + intrinsicWidthDelete
            val deleteIconBottom = deleteIconTop + intrinsicHeightDelete

            // Draw the delete icon
            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon.draw(c)
        }





        // Draw the red delete background
        /*background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        // Calculate position of delete icon
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right + dX.toInt() + deleteIconMargin
        val deleteIconRight = itemView.right + dX.toInt() + deleteIconMargin + intrinsicWidth
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Draw the delete icon
        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(c)*/

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}