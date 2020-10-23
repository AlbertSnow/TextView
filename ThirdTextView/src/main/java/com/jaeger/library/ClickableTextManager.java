package com.jaeger.library;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

public class ClickableTextManager {
    private static final String TAG = "ClickableText";

    private static final ClickableTextManager instance = new ClickableTextManager();

    private Application mContext;

    private SelectionInfoEvent mSelectEvent = null;
    private PopWindowManager popWindowManager = new PopWindowManager();

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
        resetSelectionInfo();
        popWindowManager.hide();
    }

    public void destroy() {
        resetSelectionInfo();
        popWindowManager.destroy();
    }

    public void resetSelectionInfo() {
        mSelectEvent.setText("");
        mSelectEvent.getTextView().removeSelectBackground();
    }

    public void selectText(int startPos, int endPos) {
        mSelectEvent.update(startPos, endPos);
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

}
