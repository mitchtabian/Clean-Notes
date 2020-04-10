package com.codingwithmitch.cleannotes.core.framework

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codingwithmitch.cleannotes.core.business.state.StateMessageCallback
import com.codingwithmitch.cleannotes.core.util.TodoCallback
import com.codingwithmitch.cleannotes.core.util.printLogD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// threshold for when contents of collapsing toolbar is hidden
const val COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD = -75
const val CLICK_THRESHOLD = 150L // a click is considered 150ms or less
const val CLICK_COLOR_CHANGE_TIME = 100L

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.fadeIn() {
    val animationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    apply {
        visible()
        alpha = 0f
        animate()
            .alpha(1f)
            .setDuration(animationDuration.toLong())
            .setListener(null)
    }
}

fun View.fadeOut(todoCallback: TodoCallback? = null){
    val animationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    apply {
        animate()
            .alpha(0f)
            .setDuration(animationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    gone()
                    todoCallback?.execute()
                }
            })
    }
}

fun View.onSelectChangeColor(
    lifeCycleScope: CoroutineScope,
    clickColor: Int
) = CoroutineScope(lifeCycleScope.coroutineContext).launch {
        val intialColor = (background as ColorDrawable).color
        setBackgroundColor(
            ContextCompat.getColor(
                context,
                clickColor
            )
        )
        delay(CLICK_COLOR_CHANGE_TIME)
        setBackgroundColor(intialColor)
    }

fun TextView.handleTextViewOverflow(cutOffPercent: Int, content: String){
    val tv = this
    tv.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {
            tv.viewTreeObserver.removeOnGlobalLayoutListener(this)
            tv.measure(0,0)
            val updatedContent = TextUtils.handleTextOverflow(
                cutOffPercent = cutOffPercent,
                c = tv.measuredWidth.toFloat(),
                v = tv.width.toFloat(),
                originalString = content
            )
            tv.text = updatedContent
        }
    })
}


fun EditText.disableContentInteraction() {
    keyListener = null
    isFocusable = false
    isFocusableInTouchMode = false
    isCursorVisible = false
    setBackgroundResource(android.R.color.transparent)
    clearFocus()
}

fun EditText.enableContentInteraction() {
    keyListener = EditText(context).keyListener
    isFocusable = true
    isFocusableInTouchMode = true
    isCursorVisible = true
    setBackgroundResource(android.R.color.white)
    requestFocus()
    if(text != null){
        setSelection(text.length)
    }
}


fun SwipeRefreshLayout.startRefreshing() {
    isRefreshing = true
}

fun SwipeRefreshLayout.stopRefreshing() {
    isRefreshing = false
}


fun Activity.displayToast(
    @StringRes message:Int,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    stateMessageCallback.removeMessageFromStack()
}

fun Activity.displayToast(
    message:String,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    stateMessageCallback.removeMessageFromStack()
}












