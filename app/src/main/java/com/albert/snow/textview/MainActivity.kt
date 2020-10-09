package com.albert.snow.textview

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jaeger.library.OnSelectListener
import com.jaeger.library.SelectableTextHelper

class MainActivity : AppCompatActivity() {

    private var mTvTest: TextView? = null
    private var mSelectableTextHelper: SelectableTextHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        mTvTest = findViewById(R.id.main_second_tv) as TextView
        //mTvTest.setTextIsSelectable(true);

        //mTvTest.setTextIsSelectable(true);
        mSelectableTextHelper = SelectableTextHelper.Builder(mTvTest)
                .setSelectedColor(Color.parseColor("#FF0000"))
                .setCursorHandleSizeInDp(20f)
                .setCursorHandleColor(Color.parseColor("#0000FF"))
                .build()

        mSelectableTextHelper?.setSelectListener(OnSelectListener { })


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