package com.albert.snow.textview

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mTvTest: TextView? = null
    private var mContentView: View? = null

    lateinit var mPopWindow: PopupWindow
    lateinit var popContentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        mContentView = this.window.decorView.findViewById(android.R.id.content)
        mTvTest = findViewById(R.id.main_second_tv)
        addClickableSpan(mTvTest!!)
        addClickableSpan(findViewById(R.id.main_custom_tv))

        initPopContentView()
        initPopWindow()
    }

    private fun initPopContentView() {
        popContentView = LayoutInflater.from(baseContext).inflate(R.layout.pop_item, null, false)

        popContentView.findViewById<View>(R.id.main_update_btn)?.setOnClickListener {
            mPopWindow.update(-0, 50, -1, -1, true)
        }

        popContentView.findViewById<View>(R.id.main_show_at_btn)?.setOnClickListener {
        }
    }

    private fun initPopWindow() {
        mPopWindow = PopupWindow(
            popContentView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun addClickableSpan(textView: TextView) {
        val span = object : ClickableSpan() {
            override fun onClick(widget: View) {
            }
        }

        val text = SpannableStringBuilder(textView.text)
        text.setSpan(span, 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = text
    }

    override fun onBackPressed() {
        if (mPopWindow.isShowing) {
            mPopWindow.dismiss()
        }
        var x = 0;
        var y = 50;
        mPopWindow.showAtLocation(mTvTest, Gravity.CENTER, x, y)
    }

    fun popLeft(view: View) {
        if (mPopWindow.isShowing) {
            mPopWindow.dismiss()
        }
        var x = 0;
        var y = 400;
        mPopWindow.showAtLocation(mTvTest, Gravity.TOP or Gravity.LEFT, x, y)
    }

    fun popRight(view: View) {
        if (mPopWindow.isShowing) {
            mPopWindow.dismiss()
        }
        var x = 0;
        var y = 50;
        mPopWindow.showAtLocation(mTvTest, Gravity.TOP or Gravity.RIGHT, x, y)
    }

    fun popGravityNone(view: View) {
        if (mPopWindow.isShowing) {
            mPopWindow.dismiss()
        }
        var x = 0;
        var y = 50;
        mPopWindow.showAtLocation(mTvTest, Gravity.NO_GRAVITY, x, y)
    }


}