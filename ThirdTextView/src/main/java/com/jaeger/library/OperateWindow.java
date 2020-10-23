package com.jaeger.library;

import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.albert.snow.thirdtextview.R;

class OperateWindow {

    private PopupWindow mWindow;
    private int[] mTempCoors = new int[2];

    private int mWidth;
    private int mHeight;
    private Context mContext;

    public OperateWindow(final Context context) {
        mContext = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_operate_windows, null);
        contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mWidth = contentView.getMeasuredWidth();
        mHeight = contentView.getMeasuredHeight();
        mWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        mWindow.setClippingEnabled(false);
    }

    public void show(SelectionInfoEvent mSelectionInfoEvent) {
        TextView mTextView = mSelectionInfoEvent.getTextView();
        mTextView.getLocationInWindow(mTempCoors);
        Layout layout = mTextView.getLayout();
        int posX = (int) layout.getPrimaryHorizontal(mSelectionInfoEvent.getTextIndexBegin()) + mTempCoors[0];
        int posY = layout.getLineTop(layout.getLineForOffset(mSelectionInfoEvent.getTextIndexBegin())) + mTempCoors[1] - mHeight - 16;
        if (posX <= 0) posX = 16;
        if (posY < 0) posY = 16;
        if (posX + mWidth > TextLayoutUtil.getScreenWidth(mContext)) {
            posX = TextLayoutUtil.getScreenWidth(mContext) - mWidth - 16;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWindow.setElevation(8f);
        }
        mWindow.showAtLocation(mTextView, Gravity.NO_GRAVITY, posX, posY);
    }

    public void dismiss() {
        mWindow.dismiss();
    }

    public boolean isShowing() {
        return mWindow.isShowing();
    }

}
