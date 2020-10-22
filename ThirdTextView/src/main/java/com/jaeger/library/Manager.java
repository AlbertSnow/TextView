package com.jaeger.library;

import android.app.Application;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.widget.TextView;

public class Manager {
    private static final Manager instance = new Manager();


    private int mSelectedColor =  0xFFAFE1F4;

    private Application mContext;

    private SelectionInfoEvent mSelectEvent = null;
    private PopWindowManager popWindowManager = new PopWindowManager();
    private BackgroundColorSpan mSpan;

    public static Manager getInstance() {
        return instance;
    }

    private Manager() {}

    public void init(Application context) {
        mContext = context;
        popWindowManager.init(context);
    }

    public SelectionInfoEvent getSelectionInfo() {
        return mSelectEvent;
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(SelectionInfoEvent event) {
        mSelectEvent = event;
        popWindowManager.show(mContext, event);
    }

    public void hide() {
        popWindowManager.hide();
        resetSelectionInfo();
    }

    public void resetSelectionInfo() {
        mSelectEvent.setText(null);
//        if (mSpannable != null && mSpan != null) {
//            mSpannable.removeSpan(mSpan);
//            mSpan = null;
//        }
    }


    public void selectText(int startPos, int endPos) {
        if (startPos != -1) {
            mSelectEvent.setTextIndexBegin(startPos);
        }
        if (endPos != -1) {
            mSelectEvent.setTextIndexEnd(endPos);
        }

        if (mSelectEvent.getTextIndexBegin() > mSelectEvent.getTextIndexEnd()) {
            int temp = mSelectEvent.getTextIndexBegin();
            mSelectEvent.setTextIndexBegin(mSelectEvent.getTextIndexEnd());
            mSelectEvent.setTextIndexEnd(temp);
        }

        CharSequence charSequence = mSelectEvent.getTextView().getText();
        if (charSequence == null) {
            return;
        }


        Spannable spannable;
        if (charSequence instanceof Spannable) {
            spannable = (Spannable) charSequence;
        } else {
            spannable = new SpannableStringBuilder(charSequence);
        }

        if (mSpan == null) {
            mSpan = new BackgroundColorSpan(mSelectedColor);
        }

        mSelectEvent.setText(
                spannable.subSequence(mSelectEvent.getTextIndexBegin(), mSelectEvent.getTextIndexEnd()).toString());
        spannable.setSpan(mSpan, mSelectEvent.getTextIndexBegin(), mSelectEvent.getTextIndexEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

//        mSelectEvent.getTextView().setText(spannable);
    }

    public void destroy() {
        resetSelectionInfo();
        popWindowManager.destroy();
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
