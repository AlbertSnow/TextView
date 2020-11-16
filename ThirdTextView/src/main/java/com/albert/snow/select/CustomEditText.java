package com.albert.snow.select;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class CustomEditText extends AppCompatEditText {

    public CustomEditText(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {

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


}
