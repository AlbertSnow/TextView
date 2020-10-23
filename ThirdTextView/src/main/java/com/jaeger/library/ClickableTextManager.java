package com.jaeger.library;

import android.app.Application;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.TextView;

public class ClickableTextManager {
    private static final String TAG = "ClickableText";

    private static final ClickableTextManager instance = new ClickableTextManager();


    private int mSelectedColor =  0xFF04BA69;

    private Application mContext;

    private SelectionInfoEvent mSelectEvent = null;
    private PopWindowManager popWindowManager = new PopWindowManager();
    private Spannable mSpannable;
    private BackgroundColorSpan mSpan;

    public static ClickableTextManager getInstance() {
        return instance;
    }

    private ClickableTextManager() {}

    public void init(Application context) {
        mContext = context;
        popWindowManager.init(context);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(SelectionInfoEvent event) {
        TextView textView = event.getTextView();
        CharSequence charSequence = textView.getText();
        if (TextUtils.isEmpty(charSequence) || event.getTextIndexBegin() >= charSequence.length()) {
            Log.e(TAG, "Failure:  click not fit condition");
            return;
        }

        mSelectEvent = event;
        selectText(event.getTextIndexBegin(), event.getTextIndexEnd());
        popWindowManager.show(mContext, event);
    }

    public void hide() {
        popWindowManager.hide();
        resetSelectionInfo();
    }

    public void destroy() {
        resetSelectionInfo();
        popWindowManager.destroy();
    }

    public void resetSelectionInfo() {
        mSelectEvent.setText(null);
        if (mSpannable != null && mSpan != null) {
            mSpannable.removeSpan(mSpan);
            mSpan = null;
        }
    }


    public void selectText(int startPos, int endPos) {
        mSelectEvent.update(startPos, endPos);

        CharSequence charSequence = mSelectEvent.getTextView().getText();
        if (TextUtils.isEmpty(charSequence) || !(charSequence instanceof Spannable)) {
            return;
        } else {
            mSpannable = (Spannable) charSequence;
        }
        if (mSpan == null) {
            mSpan = new BackgroundColorSpan(mSelectedColor);
        }

        mSpannable.setSpan(mSpan, mSelectEvent.getTextIndexBegin(), mSelectEvent.getTextIndexEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    public SelectionInfoEvent getSelectionInfo() {
        return mSelectEvent;
    }

    public TextView getTextView() {
        return mSelectEvent.getTextView();
    }

    public OperateWindow getOperateView() {
        return popWindowManager.getOperateView();
    }

    public CursorHandle getCursorHandle(boolean isLeft) {
        return popWindowManager.getCursorHandle(isLeft);
    }

    public PopWindowManager getPopWindowManager() {
        return popWindowManager;
    }

}
