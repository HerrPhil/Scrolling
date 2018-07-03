package com.experimental.scrolling.planet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import timber.log.Timber;

public class NestedRecyclerViewBehavior extends AppBarLayout.ScrollingViewBehavior {

    private View nestedScrollTarget;
    private boolean nestedScrollTargetIsBeingDragged = false;
    private boolean nestedScrollTargetWasUnableToScroll = false;
    private boolean skipsTouchInterception = false;
    private NestedScrollingChildHelper helper;

    public NestedRecyclerViewBehavior() {
        super();
    }

    public NestedRecyclerViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        Timber.d("NestedRecyclerViewBehavior: constructor");
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent motionEvent) {
        Timber.d("NestedRecyclerViewBehavior: onTouchEvent");

        boolean temporarilySkipsInterception = nestedScrollTarget != null;
        if (temporarilySkipsInterception) {
            // If a descendent view is scrolling we set a flag to temporarily skip our onInterceptTouchEvent implementation
            skipsTouchInterception = true;
        }
        // First dispatch, potentially skipping our onInterceptTouchEvent
        boolean handled = child.dispatchTouchEvent(motionEvent);
        if (temporarilySkipsInterception) {
            skipsTouchInterception = false;

            // If the first dispatch yielded no result or we noticed that the descendent view is unable to scroll in the
            // direction the user is scrolling, we dispatch once more but without skipping our onInterceptTouchEvent.
            // Note that RecyclerView automatically cancels active touches of all its descendents once it starts scrolling
            // so we don't have to do that.
            if (!handled || nestedScrollTargetWasUnableToScroll) {
                handled = child.dispatchTouchEvent(motionEvent);
            }
        }

        Timber.d("NestedRecyclerViewBehavior: onTouchEvent handled = %s", Boolean.toString(handled));
        return handled;
    }

    private float internalDy = 0;

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent motionEvent) {
        Timber.d("NestedRecyclerViewBehavior: onInterceptTouchEvent");
        boolean onInterceptTouchEventResult = false;
        if (child instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) child;
            onInterceptTouchEventResult = recyclerView.onInterceptTouchEvent(motionEvent);
        }
        boolean result = !skipsTouchInterception && onInterceptTouchEventResult;
        Timber.d("NestedRecyclerViewBehavior: onInterceptTouchEvent result = %s", Boolean.toString(result));


        // thinking about dispatching a nested scroll event here
        float dy = motionEvent.getY();
        internalDy += dy;
        if (internalDy > 0) {
            child.dispatchNestedScroll(0, 0, 0, 1, null);
        } else {
            child.dispatchNestedScroll(0, 1, 0, 0, null);
        }


        return result;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                  @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed,
                                  @ViewCompat.NestedScrollType int type) {
        Timber.d("NestedRecyclerViewBehavior: onNestedPreScroll");
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, @ViewCompat.NestedScrollType int type) {
        Timber.d("NestedRecyclerViewBehavior: onNestedScroll");
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (target == nestedScrollTarget && !nestedScrollTargetIsBeingDragged) {
            if (dyConsumed != 0) {
                Timber.d("NestedRecyclerViewBehavior: onNestedScroll, descendant was scrolled");
                // The descendent was actually scrolled, so we won't bother it any longer.
                // It will receive all future events until it finished scrolling.
                nestedScrollTargetIsBeingDragged = true;
                nestedScrollTargetWasUnableToScroll = false;
            } else if (dyUnconsumed != 0) { // dyConsumed is zero
                Timber.d("NestedRecyclerViewBehavior: onNestedScroll, descendant tried scrolling");
                // The descendent tried scrolling in response to touch movements but was not able to do so.
                // We remember that in order to allow RecyclerView to take over scrolling.
                nestedScrollTargetWasUnableToScroll = true;

                // This might be unnecessary if target's parent is coordinator layout.
                ViewParent viewParent = target.getParent();
                if (viewParent != null) {
                    Timber.d("NestedRecyclerViewBehavior: onNestedScroll, allow intercept touch event of target parent");
                    viewParent.requestDisallowInterceptTouchEvent(false);
                }

                // This might be enough.
                Timber.d("NestedRecyclerViewBehavior: onNestedScroll, allow intercept touch event of coordinator layout");
                coordinatorLayout.requestDisallowInterceptTouchEvent(false);
            }
        }
    }

    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child, @NonNull View directTargetChild, @NonNull View target,
                                       @ViewCompat.ScrollAxis int axes, @ViewCompat.NestedScrollType int type) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
        Timber.d("NestedRecyclerViewBehavior: onNestedScrollAccepted");
        if ((axes & View.SCROLL_AXIS_VERTICAL) != 0) {
            Timber.d("NestedRecyclerViewBehavior: onNestedScrollAccepted, descendant started scrolling; observe");
            // A descendent started scrolling, so we'll observe it.
            nestedScrollTarget = target;
            nestedScrollTargetIsBeingDragged = false;
            nestedScrollTargetWasUnableToScroll = false;
        }

    }

    /**
     * This method must return true to allow onNestedScroll to be called.
     *
     * @param coordinatorLayout
     * @param child
     * @param directTargetChild
     * @param target
     * @param axes
     * @param type
     * @return
     */
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child, @NonNull View directTargetChild, @NonNull View target,
                                       @ViewCompat.ScrollAxis int axes, @ViewCompat.NestedScrollType int type) {
        boolean result = (axes & View.SCROLL_AXIS_VERTICAL) != 0;
        Timber.d("NestedRecyclerViewBehavior: onStartNestedScroll = %s", Boolean.toString(result));
        return result;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                   @NonNull View child, @NonNull View target, @ViewCompat.NestedScrollType int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        Timber.d("NestedRecyclerViewBehavior: onStopNestedScroll, descendant finished scrolling; clean up");
        // The descendent finished scrolling. Clean up!
        nestedScrollTarget = null;
        nestedScrollTargetIsBeingDragged = false;
        nestedScrollTargetWasUnableToScroll = false;
    }

}
