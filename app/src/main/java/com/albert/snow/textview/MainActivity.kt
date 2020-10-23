package com.albert.snow.textview

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mTvTest: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        mTvTest = findViewById(R.id.main_second_tv) as TextView
        addClickableSpan(mTvTest!!)
        addClickableSpan(findViewById(R.id.main_custom_tv))
    }

    private fun addClickableSpan(textView: TextView) {
        val span = object: ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(widget.context, "Hello Clickable", Toast.LENGTH_SHORT).show()
            }
        }

        val text = SpannableStringBuilder(textView.text)
        text.setSpan(span, 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = text
    }

}