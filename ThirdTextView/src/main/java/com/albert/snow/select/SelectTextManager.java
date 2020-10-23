package com.albert.snow.select;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SelectTextManager {
    private static final String TAG = "ClickableText";

    private static final SelectTextManager instance = new SelectTextManager();

    private Application mApp;

    private SelectionInfoEvent mSelectEvent = null;
    private PopWindowManager popWindowManager = new PopWindowManager();

    private boolean isRegistAppListener = false;
    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {}

        @Override
        public void onActivityStarted(@NonNull Activity activity) {}

        @Override
        public void onActivityResumed(@NonNull Activity activity) {}

        @Override
        public void onActivityPaused(@NonNull Activity activity) {}

        @Override
        public void onActivityStopped(@NonNull Activity activity) {}

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            if (popWindowManager!= null && popWindowManager.isShow()) {
                destroy();
                mApp.unregisterActivityLifecycleCallbacks(this);
                isRegistAppListener = false;
            }
        }
    };

    public static SelectTextManager getInstance() {
        return instance;
    }

    private SelectTextManager() {}

    public void init(Application app) {
        mApp = app;
        popWindowManager.init(app);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
    public void show(SelectionInfoEvent event) {
        TextView textView = event.getTextView();
        CharSequence charSequence = textView.getText();
        if (TextUtils.isEmpty(charSequence) || event.getTextIndexBegin() >= charSequence.length()) {
            Log.e(TAG, "Failure:  click not fit condition");
            return;
        }

        if (!isRegistAppListener) {
            isRegistAppListener = true;
            mApp.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }

        if (mSelectEvent != null && popWindowManager.isShow()) {
            hide();
        }

        mSelectEvent = event;
        selectTextView(event.getTextIndexBegin(), event.getTextIndexEnd());
        popWindowManager.show(mApp, event);
    }

    public void hide() {
        unSelectTextView();
        popWindowManager.hide();
    }

    public void destroy() {
        Log.d(TAG, "destroy resource");
        unSelectTextView();
        popWindowManager.destroy();
        mSelectEvent = null;
    }

    public void unSelectTextView() {
        mSelectEvent.setText("");
        mSelectEvent.getTextView().removeSelectBackground();
    }

    public void selectTextView(int startPos, int endPos) {
        mSelectEvent.update(startPos, endPos);
    }

    public SelectionInfoEvent getSelectionInfo() {
        return mSelectEvent;
    }

    public TextView getTextView() {
        return mSelectEvent.getTextView();
    }

    public SelectPopWindow getOperateView() {
        return popWindowManager.getOperateView();
    }

    public CursorHandleWindow getCursorHandle(boolean isLeft) {
        return popWindowManager.getCursorHandle(isLeft);
    }

}
