package com.jaeger.library

import android.text.TextUtils
import android.widget.TextView

/**
 * Created by Jaeger on 16/8/30.
 *
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
data class SelectionInfoEvent(var isLongClick: Boolean = true,
                              var touchX: Int = 0, var touchY: Int = 0,
                              var textIndexBegin: Int = 0, var textIndexEnd: Int = 0,
                              var text: String? = "", var textView: TextView) {
    
    fun update(startPos: Int, endPos: Int) {
        if (startPos != -1) {
            textIndexBegin = startPos
        }

        if (endPos != -1) {
            textIndexEnd = endPos
        }

        if (textIndexBegin > textIndexEnd) {
            val temp: Int = textIndexBegin
            textIndexBegin = textIndexEnd
            textIndexEnd = temp
        }

        if (!TextUtils.isEmpty(textView.text)) {
            text = textView.text.subSequence(textIndexBegin, textIndexEnd)
                .toString()
        }
    }
    
}