package com.example.tanimate.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.tanimate.R

class PasswordEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        passEditText()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        passEditText()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        passEditText()
    }

    private fun passEditText() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //LoginActivity.isPassError(false)

                if (!text.isNullOrEmpty() && text.length < 8) {
                    error = context.getString(R.string.error_pas)
                }
            }

            override fun afterTextChanged(text: Editable?) {
            }
        })
    }
}