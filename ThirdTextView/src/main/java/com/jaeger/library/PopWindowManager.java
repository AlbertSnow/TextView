package com.jaeger.library;

import android.content.Context;
import android.view.ViewTreeObserver;

public class PopWindowManager {
    private static final String TAG = "ClickableText";

    private CursorHandleWindow mStartHandle;
    private CursorHandleWindow mEndHandle;
    private SelectPopWindow mSelectPopWindow;

    private int mCursorHandleColor =  0xFF1379D6;
    private int mCursorHandleSizeInDp = 0;

    private boolean isShow = true;

    public void init(Context mContext) {
        mCursorHandleSizeInDp = TextLayoutUtil.dp2px(mContext,24);
    }

    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
        @Override
        public void onScrollChanged() {
            if (isShow) {
                hide();
            }
        }
    };

    public void show(Context mContext, SelectionInfoEvent event) {
        hide();
        isShow = true;

        if (mStartHandle == null) mStartHandle = new CursorHandleWindow(mContext, true, mCursorHandleColor, mCursorHandleSizeInDp);
        if (mEndHandle == null) mEndHandle = new CursorHandleWindow(mContext, false, mCursorHandleColor, mCursorHandleSizeInDp);
        if (mSelectPopWindow == null) mSelectPopWindow = new SelectPopWindow(mContext);

        mEndHandle.show();
        mStartHandle.show();
        mSelectPopWindow.show(event);

        event.getTextView().getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);
    }

    public void hide() {
        isShow = false;
        if (mSelectPopWindow != null) {
            mSelectPopWindow.dismiss();
        }
        if (mStartHandle != null) {
            mStartHandle.dismiss();
        }
        if (mEndHandle != null) {
            mEndHandle.dismiss();
        }
    }

    public void destroy() {
        SelectionInfoEvent mSelectionInfoEvent = SelectTextManager.getInstance().getSelectionInfo();
        mSelectionInfoEvent.getTextView().getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);

        hide();
        mStartHandle = null;
        mEndHandle = null;
        mSelectPopWindow = null;
    }

    public boolean isShow() {
        return isShow;
    }

    /**
     * Operate windows : copy, select all
     */
    public CursorHandleWindow getCursorHandle(boolean isLeft) {
        if (mStartHandle.isLeft() == isLeft) {
            return mStartHandle;
        } else {
            return mEndHandle;
        }
    }

    public SelectPopWindow getOperateView() {
        return mSelectPopWindow;
    }

}
