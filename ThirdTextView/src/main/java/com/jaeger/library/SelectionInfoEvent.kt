package com.jaeger.library

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
                              var text: String? = "", var textView: TextView)