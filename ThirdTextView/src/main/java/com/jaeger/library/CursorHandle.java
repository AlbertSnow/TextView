package com.jaeger.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

class CursorHandle extends View {

    private PopupWindow mPopupWindow;
    private Paint mPaint;
    private int mCursorHandleSize;

    private int mCircleRadius = mCursorHandleSize / 2;
    private int mWidth = mCircleRadius * 2;
    private int mHeight = mCircleRadius * 2;
    private int mPadding = 25;
    private boolean isLeft;

    private Manager mHelper;

    public CursorHandle(Context context, boolean isLeft, int handleColor, int handleSize) {
        super(context);

        mHelper = Manager.getInstance();
        initSize(handleSize);

        this.isLeft = isLeft;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(handleColor);

        mPopupWindow = new PopupWindow(this);
        mPopupWindow.setClippingEnabled(false);
        mPopupWindow.setWidth(mWidth + mPadding * 2);
        mPopupWindow.setHeight(mHeight + mPadding / 2);
        invalidate();
    }

    private void initSize(int handleSize) {
        mCursorHandleSize = handleSize;
        mCircleRadius = mCursorHandleSize / 2;
        mWidth = mCircleRadius * 2;
        mHeight = mCircleRadius * 2;
    }

    public boolean isLeft() {
        return isLeft;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mCircleRadius + mPadding, mCircleRadius, mCircleRadius, mPaint);
        if (isLeft) {
            canvas.drawRect(mCircleRadius + mPadding, 0, mCircleRadius * 2 + mPadding, mCircleRadius, mPaint);
        } else {
            canvas.drawRect(mPadding, 0, mCircleRadius + mPadding, mCircleRadius, mPaint);
        }
    }

    private int mAdjustX;
    private int mAdjustY;

    private int mBeforeDragStart;
    private int mBeforeDragEnd;

    private int[] mTempCoors = new int[2];

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBeforeDragStart = mHelper.getSelectionInfo().getTextIndexBegin();
                mBeforeDragEnd = mHelper.getSelectionInfo().getTextIndexEnd();
                mAdjustX = (int) event.getX();
                mAdjustY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHelper.getOperateView().show(mHelper.getTextView(), mHelper.getSelectionInfo());
                break;
            case MotionEvent.ACTION_MOVE:
                mHelper.getOperateView().dismiss();
                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();
                update(rawX + mAdjustX - mWidth, rawY + mAdjustY - mHeight);
                break;
        }
        return true;
    }

    public void show(int x, int y) {
        mHelper.getTextView().getLocationInWindow(mTempCoors);
        int offset = isLeft ? mWidth : 0;
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

        y -= mTempCoors[1];

        int offset = TextLayoutUtil.getHysteresisOffset(mHelper.getTextView(), x, y, oldOffset);

        if (offset != oldOffset) {
            mHelper.resetSelectionInfo();
            if (isLeft) {
                if (offset > mBeforeDragEnd) {
                    CursorHandle handle = mHelper.getCursorHandle(false);
                    changeDirection();
                    handle.changeDirection();
                    mBeforeDragStart = mBeforeDragEnd;
                    mHelper.selectText(mBeforeDragEnd, offset);
                    handle.updateCursorHandle();
                } else {
                    mHelper.selectText(offset, -1);
                }
            } else {
                if (offset < mBeforeDragStart) {
                    CursorHandle handle = mHelper.getCursorHandle(true);
                    handle.changeDirection();
                    changeDirection();
                    mBeforeDragEnd = mBeforeDragStart;
                    mHelper.selectText(offset, mBeforeDragStart);
                    handle.updateCursorHandle();
                } else {
                    mHelper.selectText(mBeforeDragStart, offset);
                }
            }
            updateCursorHandle();
        }
    }

    private void updateCursorHandle() {
        mHelper.getTextView().getLocationInWindow(mTempCoors);
        Layout layout = mHelper.getTextView().getLayout();
        if (isLeft) {
            mPopupWindow.update((int) layout.getPrimaryHorizontal(mHelper.getSelectionInfo().getTextIndexBegin()) - mWidth + getExtraX(),
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
