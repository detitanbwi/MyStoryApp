package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat


class MyTextViewName : AppCompatEditText {
    private lateinit var borderTextBox: Drawable
    private lateinit var borderTextBoxLostFocus: Drawable
    private lateinit var textBoxIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
        setInitialDrawables()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        setInitialDrawables()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        setInitialDrawables()
    }

    private fun setInitialDrawables() {
        setButtonDrawables(startOfTheText = textBoxIcon)
        this.compoundDrawablePadding = 16
        this.background = borderTextBoxLostFocus
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if(focused) {
            this.background = borderTextBox
            setButtonDrawables(startOfTheText = tintDrawable(textBoxIcon, Color.parseColor("#C600FD")))
        }else{
            this.background = borderTextBoxLostFocus
            setButtonDrawables(startOfTheText = tintDrawable(textBoxIcon, Color.parseColor("#D5D5D5")))

        }
    }

    private fun tintDrawable(drawable: Drawable, color: Int): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun init() {
        borderTextBox = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        textBoxIcon = ContextCompat.getDrawable(context, R.drawable.ic_person) as Drawable
        borderTextBoxLostFocus = ContextCompat.getDrawable(context, R.drawable.bg_edit_text_lost_focus) as Drawable
        setButtonDrawables(startOfTheText = textBoxIcon)
        this.background = borderTextBoxLostFocus
        this.hint = "Nama Lengkap"
    }
}