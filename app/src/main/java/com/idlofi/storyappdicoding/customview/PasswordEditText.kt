package com.idlofi.storyappdicoding.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.idlofi.storyappdicoding.R

class PasswordEditText : AppCompatEditText {
    constructor(context: Context): super(context){
        init()
    }
    constructor(context: Context, attrs:AttributeSet): super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttrs: Int): super(context, attrs, defStyleAttrs){
        init()
    }

    private fun init(){
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                error = if (p0!!.isNotEmpty() && p0.toString().length < 8) context.getString(R.string.password_invalid) else null
            }

        })
    }
}
