package com.jaeger.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class SelectableTextView extends AppCompatTextView {

    private final static int DEFAULT_SELECTION_LENGTH = 1;

    private boolean isHideWhenScroll;
    private boolean isHide = true;

    private int mSelectedColor;
    private int mTouchX;
    private int mTouchY;


    public SelectableTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public SelectableTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectableTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setText(getText(), TextView.BufferType.SPANNABLE);
        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int startOffset = TextLayoutUtil.getPreciseOffset(SelectableTextView.this, mTouchX, mTouchY);
                int endOffset = startOffset + DEFAULT_SELECTION_LENGTH;

                if (startOffset == -1 || endOffset == -1) {
                    return true;
                }

                Manager.getInstance().onReceive(
                        new SelectionInfoEvent(true, mTouchX, mTouchY,
                                startOffset, endOffset, "", SelectableTextView.this));
                return true;
            }
        });

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTouchX = (int) event.getX();
                mTouchY = (int) event.getY();
                return false;
            }
        });

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Manager.getInstance().hide();
            }
        });
        addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Manager.getInstance().destroy();
            }
        });
    }


}