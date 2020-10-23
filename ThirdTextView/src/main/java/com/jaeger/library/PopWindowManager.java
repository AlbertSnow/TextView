package com.jaeger.library;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class PopWindowManager {
    private static final String TAG = "ClickableT";

    private CursorHandle mStartHandle;
    private CursorHandle mEndHandle;
    private OperateWindow mOperateWindow;

    private static final int DEFAULT_SHOW_DURATION = 100;

    private int mCursorHandleColor =  0xFF1379D6;
    private int mCursorHandleSizeInDp = 0;

    private boolean isHide = true;

    public void init(Context mContext) {
        mCursorHandleSizeInDp = TextLayoutUtil.dp2px(mContext,24);
    }


    private boolean isHideWhenScroll = true;


    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            if (isHideWhenScroll) {
                isHideWhenScroll = false;
                postShowSelectView(DEFAULT_SHOW_DURATION);
            }
            return true;
        }
    };
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
        @Override
        public void onScrollChanged() {
            if (!isHideWhenScroll && !isHide) {
                isHideWhenScroll = true;
                hide();
            }
        }
    };


    public void show(Context mContext, SelectionInfoEvent event) {
        TextView mTextView = event.getTextView();
        CharSequence charSequence = mTextView.getText();
        if (charSequence == null || event.getTextIndexBegin() >= mTextView.getText().length()) {
            Log.e(TAG, "Failure:  click not fit condition");
            return;
        }

        hide();
        isHide = false;

        if (mStartHandle == null) mStartHandle = new CursorHandle(mContext, true, mCursorHandleColor, mCursorHandleSizeInDp);
        if (mEndHandle == null) mEndHandle = new CursorHandle(mContext, false, mCursorHandleColor, mCursorHandleSizeInDp);
        if (mOperateWindow == null) mOperateWindow = new OperateWindow(mContext);

        Manager.getInstance().selectText(event.getTextIndexBegin(), event.getTextIndexEnd());
        showCursorHandle(mStartHandle);
        showCursorHandle(mEndHandle);
        mOperateWindow.show(mTextView, event);

        event.getTextView().getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListener);
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
        SelectionInfoEvent mSelectionInfoEvent = Manager.getInstance().getSelectionInfo();
        mSelectionInfoEvent.getTextView().getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
        mSelectionInfoEvent.getTextView().getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListener);

        hide();
        mStartHandle = null;
        mEndHandle = null;
        mOperateWindow = null;
    }

    public boolean isHide() {
        return isHide;
    }

    private void postShowSelectView(int duration) {
        View mTextView = Manager.getInstance().getTextView();
        mTextView.removeCallbacks(mShowSelectViewRunnable);
        if (duration <= 0) {
            mShowSelectViewRunnable.run();
        } else {
            mTextView.postDelayed(mShowSelectViewRunnable, duration);
        }
    }

    private final Runnable mShowSelectViewRunnable = new Runnable() {

        @Override
        public void run() {
            if (isHide) return;
            if (mOperateWindow != null) {
                SelectionInfoEvent event = Manager.getInstance().getSelectionInfo();
                mOperateWindow.show(event.getTextView(), event);
            }
            if (mStartHandle != null) {
                showCursorHandle(mStartHandle);
            }
            if (mEndHandle != null) {
                showCursorHandle(mEndHandle);
            }
        }
    };

    private void showCursorHandle(CursorHandle cursorHandle) {
        SelectionInfoEvent event = Manager.getInstance().getSelectionInfo();
        Layout layout = event.getTextView().getLayout();
        int offset = cursorHandle.isLeft() ? event.getTextIndexBegin() : event.getTextIndexEnd();
        cursorHandle.show((int) layout.getPrimaryHorizontal(offset), layout.getLineBottom(layout.getLineForOffset(offset)));
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
