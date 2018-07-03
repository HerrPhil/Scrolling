package com.experimental.scrolling.planet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import timber.log.Timber;

public class NestedRecyclerView extends RecyclerView implements NestedScrollingParent {

    private View nestedScrollTarget;
    private boolean nestedScrollTargetIsBeingDragged = false;
    private boolean nestedScrollTargetWasUnableToScroll = false;
    private boolean skipsTouchInterception = false;

    public NestedRecyclerView(Context context) {
        super(context);
    }

    public NestedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        Timber.d("NestedRecyclerView: dispatchTouchEvent");
        boolean temporarilySkipsInterception = nestedScrollTarget != null;
        if (temporarilySkipsInterception) {
            // If a descendent view is scrolling we set a flag to temporarily skip our onInterceptTouchEvent implementation
            skipsTouchInterception = true;
        }

        // First dispatch, potentially skipping our onInterceptTouchEvent
        boolean handled = super.dispatchTouchEvent(motionEvent);

        if (temporarilySkipsInterception) {
            skipsTouchInterception = false;

            // If the first dispatch yielded no result or we noticed that the descendent view is unable to scroll in the
            // direction the user is scrolling, we dispatch once more but without skipping our onInterceptTouchEvent.
            // Note that RecyclerView automatically cancels active touches of all its descendents once it starts scrolling
            // so we don't have to do that.
            if (!handled || nestedScrollTargetWasUnableToScroll) {
                handled = super.dispatchTouchEvent(motionEvent);
            }
        }

        Timber.d("NestedRecyclerView: dispatchTouchEvent handled = %s", Boolean.toString(handled));
        return handled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Timber.d("NestedRecyclerView: onInterceptTouchEvent");
        boolean onInterceptTouchEventResult = false;
        onInterceptTouchEventResult = super.onInterceptTouchEvent(motionEvent);
        boolean result = !skipsTouchInterception && onInterceptTouchEventResult;
        Timber.d("NestedRecyclerView: onInterceptTouchEvent result = %s", Boolean.toString(result));
        return result;
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Timber.d("NestedRecyclerView: onNestedScroll");
        if (target == nestedScrollTarget && !nestedScrollTargetIsBeingDragged) {
            if (dyConsumed != 0) {
                Timber.d("NestedRecyclerView: onNestedScroll, descendant was scrolled");
                // The descendent was actually scrolled, so we won't bother it any longer.
                // It will receive all future events until it finished scrolling.
                nestedScrollTargetIsBeingDragged = true;
                nestedScrollTargetWasUnableToScroll = false;
            } else if (dyUnconsumed != 0) { // dyConsumed is zero
                Timber.d("NestedRecyclerView: onNestedScroll, descendant tried scrolling");
                // The descendent tried scrolling in response to touch movements but was not able to do so.
                // We remember that in order to allow RecyclerView to take over scrolling.
                nestedScrollTargetWasUnableToScroll = true;

                // This might be unnecessary if target's parent is coordinator layout.
                ViewParent viewParent = target.getParent();
                if (viewParent != null) {
                    Timber.d("NestedRecyclerView: onNestedScroll, allow intercept touch event of target parent");
                    viewParent.requestDisallowInterceptTouchEvent(false);
                }
            }
        }
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int axes) {
        Timber.d("NestedRecyclerView: onNestedScrollAccepted");
        if ((axes & View.SCROLL_AXIS_VERTICAL) != 0) {
            Timber.d("NestedRecyclerView: onNestedScrollAccepted, descendant started scrolling; observe");
            // A descendent started scrolling, so we'll observe it.
            nestedScrollTarget = target;
            nestedScrollTargetIsBeingDragged = false;
            nestedScrollTargetWasUnableToScroll = false;
        }

    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target,
                                       @ViewCompat.ScrollAxis int axes) {
        boolean result = (axes & View.SCROLL_AXIS_VERTICAL) != 0;
        Timber.d("NestedRecyclerView: onStartNestedScroll = %s", Boolean.toString(result));
        return result;
    }

    @Override
    public void onStopNestedScroll(@NonNull View child) {
        Timber.d("NestedRecyclerView: onStopNestedScroll, descendant finished scrolling; clean up");
        // The descendent finished scrolling. Clean up!
        nestedScrollTarget = null;
        nestedScrollTargetIsBeingDragged = false;
        nestedScrollTargetWasUnableToScroll = false;
    }

}
