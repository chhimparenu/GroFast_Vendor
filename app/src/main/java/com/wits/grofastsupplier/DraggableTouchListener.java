package com.wits.grofastsupplier;

import android.view.MotionEvent;
import android.view.View;

public class DraggableTouchListener implements View.OnTouchListener {
    private float downRawX, downRawY;
    private float dX, dY;
    private static final int CLICK_THRESHOLD = 200; // Threshold to differentiate between click and drag
    private long startClickTime;

    private View.OnClickListener clickListener;

    public DraggableTouchListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            downRawX = motionEvent.getRawX();
            downRawY = motionEvent.getRawY();
            dX = view.getX() - downRawX;
            dY = view.getY() - downRawY;
            startClickTime = System.currentTimeMillis();
            return true; // Consumed
        } else if (action == MotionEvent.ACTION_MOVE) {
            int viewWidth = view.getWidth();
            int viewHeight = view.getHeight();
            View viewParent = (View) view.getParent();
            int parentWidth = viewParent.getWidth();
            int parentHeight = viewParent.getHeight();

            float newX = motionEvent.getRawX() + dX;
            newX = Math.max(0, newX); // Don't allow the FAB past the left hand side of the parent
            newX = Math.min(parentWidth - viewWidth, newX); // Don't allow the FAB past the right hand side of the parent

            float newY = motionEvent.getRawY() + dY;
            newY = Math.max(0, newY); // Don't allow the FAB past the top of the parent
            newY = Math.min(parentHeight - viewHeight, newY); // Don't allow the FAB past the bottom of the parent

            view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start();

            return true; // Consumed
        } else if (action == MotionEvent.ACTION_UP) {
            long clickDuration = System.currentTimeMillis() - startClickTime;
            if (clickDuration < CLICK_THRESHOLD) {
                if (clickListener != null) {
                    clickListener.onClick(view);
                }
            }
            return true; // Consumed
        } else {
            return false; // Not consumed
        }
    }
}