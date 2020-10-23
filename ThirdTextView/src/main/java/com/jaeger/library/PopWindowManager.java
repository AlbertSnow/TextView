package com.jaeger.library;

import android.content.Context;
import android.view.ViewTreeObserver;

public class PopWindowManager {
    private static final String TAG = "ClickableText";

    private CursorHandle mStartHandle;
    private CursorHandle mEndHandle;
    private OperateWindow mOperateWindow;

    private int mCursorHandleColor =  0xFF1379D6;
    private int mCursorHandleSizeInDp = 0;

    private boolean isHide = true;

    public void init(Context mContext) {
        mCursorHandleSizeInDp = TextLayoutUtil.dp2px(mContext,24);
    }

    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
        @Override
        public void onScrollChanged() {
            if (!isHide) {
                hide();
            }
        }
    };

    public void show(Context mContext, SelectionInfoEvent event) {
        hide();
        isHide = false;

        if (mStartHandle == null) mStartHandle = new CursorHandle(mContext, true, mCursorHandleColor, mCursorHandleSizeInDp);
        if (mEndHandle == null) mEndHandle = new CursorHandle(mContext, false, mCursorHandleColor, mCursorHandleSizeInDp);
        if (mOperateWindow == null) mOperateWindow = new OperateWindow(mContext);

        mEndHandle.show();
        mStartHandle.show();
        mOperateWindow.show(event);

        event.getTextView().getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);
    }

    public void hide() {
        isHide = true;
        if (mOperateWindow != null) {
            mOperateWindow.dismiss();
        }
        if (mStartHandle != null) {
            mStartHandle.dismiss();
        }
        if (mEndHandle != null) {
            mEndHandle.dismiss();
        }
    }

    public void destroy() {
        SelectionInfoEvent mSelectionInfoEvent = ClickableTextManager.getInstance().getSelectionInfo();
        mSelectionInfoEvent.getTextView().getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);

        hide();
        mStartHandle = null;
        mEndHandle = null;
        mOperateWindow = null;
    }

    public boolean isHide() {
        return isHide;
    }

    /**
     * Operate windows : copy, select all
     */
    public CursorHandle getCursorHandle(boolean isLeft) {
        if (mStartHandle.isLeft() == isLeft) {
            return mStartHandle;
        } else {
            return mEndHandle;
        }
    }

    public OperateWindow getOperateView() {
        return mOperateWindow;
    }

}
