package com.albert.snow.select;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.lang.reflect.Field;

public class SelectableTextView extends AppCompatTextView {

    private final static int DEFAULT_SELECTION_LENGTH = 1;

    private int mTouchX;
    private int mTouchY;
    private Spannable mSpannable;
    private BackgroundColorSpan mSpan;

    public SelectableTextView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SelectableTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectableTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        setTextIsSelectable(false);
        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int[] indexArray = TextLayoutUtil.getSelectWordIndexArray(mTouchX, mTouchY, SelectableTextView.this);

                if (indexArray == null) {
                    Log.e(SelectTextManager.TAG, "index Array null, return");
                    return true;
                }

                int startOffset = indexArray[0];
                int endOffset = indexArray[1];

                if (startOffset == -1 || endOffset == -1) {
                    return true;
                }

                SelectTextManager.getInstance().show(
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
                SelectTextManager.getInstance().hide();
            }
        });

        addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                SelectTextManager.getInstance().destroy();
            }
        });

//        Typeface tf = getDefaultFontType(context, )
//        File fontFile = new File("/system/fonts/VivoFont.ttf");
//        Log.i("testFont", "size: " + fontFile.length());

//        final Typeface tf = Typeface.createFromFile("/system/fonts/VivoFont.ttf");
        final Typeface tf = Typeface.createFromFile("/system/fonts/Padauk.ttf");
//        final Typeface tf = Typeface.createFromFile("/system/fonts/Roboto-Italic.ttf");
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setTypeface(tf);
                Toast.makeText(context, "Hello change", Toast.LENGTH_LONG).show();
            }
        }, 1000 * 2);
        setTypeface(tf);
    }

//    public static void getDefaultFontType(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
//        try {
//            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);
//
//            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
//            defaultFontTypefaceField.setAccessible(true);
//            defaultFontTypefaceField.set(null, customFontTypeface);
//            defaultFontTypefaceField.get(null);
//
//        } catch (Exception e) {
//
//        }
//    }

    public static void setDefaultFontType(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {

        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!(text instanceof SpannableStringBuilder) && !TextUtils.isEmpty(text)) {
            super.setText(new SpannableStringBuilder(text));
        } else {
            super.setText(text, type);
        }
    }

    public void removeSelectBackground() {
        if (mSpannable != null && mSpan != null) {
            mSpannable.removeSpan(mSpan);
            mSpan = null;
        }
    }

    public void selectText(int textIndexBegin, int textIndexEnd) {
        CharSequence charSequence = getText();
        if (TextUtils.isEmpty(charSequence) || !(charSequence instanceof Spannable)) {
            return;
        } else {
            mSpannable = (Spannable) charSequence;
        }
        if (mSpan == null) {
            int mSelectedColor = 0xFF04BA69;
            mSpan = new BackgroundColorSpan(mSelectedColor);
        }
        mSpannable.setSpan(mSpan, textIndexBegin, textIndexEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

}
