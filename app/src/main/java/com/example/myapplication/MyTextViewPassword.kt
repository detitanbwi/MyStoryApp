package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

class MyTextViewPassword : AppCompatEditText {
    private lateinit var borderTextBox: Drawable
    private lateinit var borderTextBoxLostFocus: Drawable
    private lateinit var TextBoxIcon: Drawable
    private lateinit var passwordVisibilityIcon: Drawable
    private lateinit var passwordHiddenIcon: Drawable

    private var isPasswordVisible: Boolean = false
    private var isPasswordError = false

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
        setCompoundDrawablesWithIntrinsicBounds(
            tintDrawable(TextBoxIcon, Color.parseColor("#D5D5D5")),
            null,
            passwordVisibilityIcon,
            null
        )
        this.compoundDrawablePadding = 16
        passwordVisibilityIcon = ContextCompat.getDrawable(context, R.drawable.ic_visibility) as Drawable
        passwordHiddenIcon = ContextCompat.getDrawable(context, R.drawable.ic_visibility_off) as Drawable
        error = null

        // Menambahkan listener untuk ikon mata
        passwordVisibilityIcon.setBounds(0, 0, passwordVisibilityIcon.intrinsicWidth, passwordVisibilityIcon.intrinsicHeight)
        setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_UP) {
                    // Ketika ikon mata ditekan, panggil togglePasswordVisibility()
                    togglePasswordVisibility()
                }
                return false
            }
        })

        this.background = borderTextBoxLostFocus
    }


    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        val passwordIcon: Drawable = if (!isPasswordVisible) {
            passwordHiddenIcon
        } else {
            passwordVisibilityIcon
        }
        if(focused) {
            this.background = borderTextBox
            setCompoundDrawablesWithIntrinsicBounds(
                tintDrawable(TextBoxIcon, Color.parseColor("#C600FD")),
                null,
                passwordIcon,
                null
            )
        }else{
            this.background = borderTextBoxLostFocus
            setCompoundDrawablesWithIntrinsicBounds(
                tintDrawable(TextBoxIcon, Color.parseColor("#D5D5D5")),
                null,
                passwordIcon,
                null
            )

        }

    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.toString().length < 8) {
            error = "Kata sandi minimal 8 karakter"
            isPasswordError = true
        } else {
            error = null
            isPasswordError = false
        }
    }

    
    private fun tintDrawable(drawable: Drawable, color: Int): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible

        inputType = if (isPasswordVisible) {
            // Show password
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            // Hide password
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        // You need to call setInputType for the changes to take effect
        setRawInputType(inputType)

        // Update the drawables as you were doing
        if (isPasswordVisible) {
            setCompoundDrawablesWithIntrinsicBounds(
                tintDrawable(TextBoxIcon, Color.parseColor("#C600FD")),
                null,
                passwordHiddenIcon,
                null
            )
        } else {
            setCompoundDrawablesWithIntrinsicBounds(
                tintDrawable(TextBoxIcon, Color.parseColor("#C600FD")),
                null,
                passwordVisibilityIcon,
                null
            )
        }
    }

    private fun init() {
        borderTextBox = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        TextBoxIcon = ContextCompat.getDrawable(context, R.drawable.ic_lock) as Drawable
        borderTextBoxLostFocus = ContextCompat.getDrawable(context, R.drawable.bg_edit_text_lost_focus) as Drawable
        passwordVisibilityIcon = ContextCompat.getDrawable(context, R.drawable.ic_visibility) as Drawable
        passwordHiddenIcon = ContextCompat.getDrawable(context, R.drawable.ic_visibility_off) as Drawable
        this.hint = "Kata Sandi"
    }
}