package com.experimental.scrolling.planet;

import android.support.design.widget.AppBarLayout;

import timber.log.Timber;

import static com.experimental.scrolling.planet.OffsetChangeState.COLLAPSED;
import static com.experimental.scrolling.planet.OffsetChangeState.EXPANDED;
import static com.experimental.scrolling.planet.OffsetChangeState.IDLE;

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    private OffsetChangeState mCurrentState = IDLE;

    /**
     * Called when the {@link AppBarLayout}'s layout offset has been changed. This allows
     * child views to implement custom behavior based on the offset (for instance pinning a
     * view at a certain y value).
     *
     * @param appBarLayout   the {@link AppBarLayout} which offset has changed
     * @param verticalOffset the vertical offset for the parent {@link AppBarLayout}, in px
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Timber.d("AppBarStateChangeListener.onOffsetChanged: vertical offset = %d, total scroll range = %d", verticalOffset, appBarLayout.getTotalScrollRange());
        if (verticalOffset == 0) {
            if (mCurrentState != EXPANDED) {
                onStateChange(appBarLayout, EXPANDED);
            }
            mCurrentState = EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != COLLAPSED) {
                onStateChange(appBarLayout, COLLAPSED);
            }
            mCurrentState = COLLAPSED;
        } else {
            if (mCurrentState != IDLE) {
                onStateChange(appBarLayout, IDLE);
            }
            mCurrentState = IDLE;
        }
    }

    public abstract void onStateChange(AppBarLayout appBarLayout, OffsetChangeState state);
}
