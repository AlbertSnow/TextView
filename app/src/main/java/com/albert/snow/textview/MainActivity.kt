package com.albert.snow.textview

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
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
    }
}