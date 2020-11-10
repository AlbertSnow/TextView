package com.albert.snow.select;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

class CursorHandleWindow extends View {

    private PopupWindow mPopupWindow;
    private Paint mPaint;
    private int mCursorHandleSize;

    private int mCursorRadius = mCursorHandleSize / 2;
    private int mCursorWidthNoPadding = mCursorRadius * 2;
    private int mCursorHeightNoPadding = mCursorRadius * 2;
    private int mPadding = 25;
    private boolean isLeft;

    private SelectTextManager mHelper;

    public CursorHandleWindow(Context context, boolean isLeft, int handleColor, int handleSize) {
        super(context);

        mHelper = SelectTextManager.getInstance();
        initSize(handleSize);

        this.isLeft = isLeft;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(handleColor);

        mPopupWindow = new PopupWindow(this);
        mPopupWindow.setClippingEnabled(false);
        mPopupWindow.setWidth(mCursorWidthNoPadding + mPadding * 2);
        mPopupWindow.setHeight(mCursorHeightNoPadding + mPadding / 2);
        invalidate();
    }

    private void initSize(int handleSize) {
        mCursorHandleSize = handleSize;
        mCursorRadius = mCursorHandleSize / 2;
        mCursorWidthNoPadding = mCursorRadius * 2;
        mCursorHeightNoPadding = mCursorRadius * 2;
    }

    public boolean isLeft() {
        return isLeft;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mCursorRadius + mPadding, mCursorRadius, mCursorRadius, mPaint);
        if (isLeft) {
            canvas.drawRect(mCursorRadius + mPadding, 0, mCursorRadius * 2 + mPadding, mCursorRadius, mPaint);
        } else {
            canvas.drawRect(mPadding, 0, mCursorRadius + mPadding, mCursorRadius, mPaint);
        }
    }

    private int mDownEventX;
    private int mDownEventY;

    private int mBeforeDragStart;
    private int mBeforeDragEnd;

    private int[] mTempCoors = new int[2];

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBeforeDragStart = mHelper.getSelectionInfo().getTextIndexBegin();
                mBeforeDragEnd = mHelper.getSelectionInfo().getTextIndexEnd();
                mDownEventX = (int) event.getX();
                mDownEventY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHelper.getOperateView().show(mHelper.getSelectionInfo());
                break;
            case MotionEvent.ACTION_MOVE:
                mHelper.getOperateView().dismiss();
                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();
                update(rawX + mDownEventX - mCursorWidthNoPadding, rawY + mDownEventY - mCursorHeightNoPadding);
                break;
        }
        return true;
    }

    public void show() {
        SelectionInfoEvent event = SelectTextManager.getInstance().getSelectionInfo();
        int offset = isLeft() ? event.getTextIndexBegin() : event.getTextIndexEnd();

        Layout layout = event.getTextView().getLayout();
        int x = (int) layout.getPrimaryHorizontal(offset);
        int y = layout.getLineBottom(layout.getLineForOffset(offset));

        mHelper.getTextView().getLocationInWindow(mTempCoors);

        offset = isLeft ? mCursorWidthNoPadding : 0;
        mPopupWindow.showAtLocation(mHelper.getTextView(), Gravity.NO_GRAVITY, x - offset + getExtraX(), y + getExtraY());
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    private void changeDirection() {
        isLeft = !isLeft;
        invalidate();
    }

    private void update(int x, int y) {
        mHelper.getTextView().getLocationInWindow(mTempCoors);
        int oldOffset;
        if (isLeft) {
            oldOffset = mHelper.getSelectionInfo().getTextIndexBegin();
        } else {
            oldOffset = mHelper.getSelectionInfo().getTextIndexEnd();
        }

        x -= mTempCoors[0];
        y -= mTempCoors[1];

        int offset = TextLayoutUtil.getHysteresisOffset(mHelper.getTextView(), x, y, oldOffset);

        if (offset != oldOffset) {
            mHelper.unSelectTextView();
            if (isLeft) {
                if (offset > mBeforeDragEnd) {
                    CursorHandleWindow handle = mHelper.getCursorHandle(false);
                    changeDirection();
                    handle.changeDirection();
                    mBeforeDragStart = mBeforeDragEnd;
                    mHelper.selectTextView(mBeforeDragEnd, offset);
                    handle.updateCursorHandle();
                } else {
                    mHelper.selectTextView(offset, -1);
                }
            } else {
                if (offset < mBeforeDragStart) {
                    CursorHandleWindow handle = mHelper.getCursorHandle(true);
                    handle.changeDirection();
                    changeDirection();
                    mBeforeDragEnd = mBeforeDragStart;
                    mHelper.selectTextView(offset, mBeforeDragStart);
                    handle.updateCursorHandle();
                } else {
                    mHelper.selectTextView(mBeforeDragStart, offset);
                }
            }
            updateCursorHandle();
        }
    }

    private void updateCursorHandle() {
        mHelper.getTextView().getLocationInWindow(mTempCoors);
        Layout layout = mHelper.getTextView().getLayout();
        if (isLeft) {
            mPopupWindow.update((int) layout.getPrimaryHorizontal(mHelper.getSelectionInfo().getTextIndexBegin()) - mCursorWidthNoPadding + getExtraX(),
                    layout.getLineBottom(layout.getLineForOffset(mHelper.getSelectionInfo().getTextIndexBegin())) + getExtraY(), -1, -1);
        } else {
            mPopupWindow.update((int) layout.getPrimaryHorizontal(mHelper.getSelectionInfo().getTextIndexEnd()) + getExtraX(),
                    layout.getLineBottom(layout.getLineForOffset(mHelper.getSelectionInfo().getTextIndexEnd())) + getExtraY(), -1, -1);
        }
    }

    private int getExtraX() {
        return mTempCoors[0] - mPadding + mHelper.getTextView().getPaddingLeft();
    }

    private int getExtraY() {
        return mTempCoors[1] + mHelper.getTextView().getPaddingTop();
    }

}
