/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.ui;

import android.content.Context;
import android.view.MotionEvent;

public abstract class KonamiCodeDetector extends OnSwipeGestureTouchListener {

    private enum Action {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        A,
        B
    }

    private enum State {

        INITIAL(Action.UP),
        UP(Action.UP),
        UP_UP(Action.DOWN),
        UP_UP_DOWN(Action.DOWN),
        UP_UP_DOWN_DOWN(Action.LEFT),
        UP_UP_DOWN_DOWN_LEFT(Action.RIGHT),
        UP_UP_DOWN_DOWN_LEFT_RIGHT(Action.LEFT),
        UP_UP_DOWN_DOWN_LEFT_RIGHT_LEFT(Action.RIGHT),
        UP_UP_DOWN_DOWN_LEFT_RIGHT_LEFT_RIGHT(Action.B),
        UP_UP_DOWN_DOWN_LEFT_RIGHT_LEFT_RIGHT_B(Action.A),
        FINAL(null);

        private Action mNextAction;

        State(Action nextAction) {
            mNextAction = nextAction;
        }

        public State next(Action action) {
            return mNextAction != null && action == mNextAction ? values()[ordinal() + 1]
                    : INITIAL;
        }
    }

    private final float mHalfScreenWidth;

    private State mState = State.INITIAL;

    public KonamiCodeDetector(Context context) {
        super(context, false);

        mHalfScreenWidth = (float) context.getResources().getDisplayMetrics().widthPixels / 2;
    }

    @Override
    public void onSwipeLeft() {
        onAction(Action.LEFT);
    }

    @Override
    public void onSwipeRight() {
        onAction(Action.RIGHT);
    }

    @Override
    public void onSwipeUp() {
        onAction(Action.UP);
    }

    @Override
    public void onSwipeDown() {
        onAction(Action.DOWN);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        if (event.getX() < mHalfScreenWidth) {
            onAction(Action.A);
        } else {
            onAction(Action.B);
        }
        return true;
    }

    private void onAction(Action action) {
        mState = mState.next(action);
        if (mState == State.FINAL) {
            onDetected();
            mState = mState.next(null);
        }
    }

    public abstract void onDetected();
}