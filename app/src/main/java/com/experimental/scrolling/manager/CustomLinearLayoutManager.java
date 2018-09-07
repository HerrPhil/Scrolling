package com.experimental.scrolling.manager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class CustomLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled;

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean isScrollEnabled) {
        this.isScrollEnabled = isScrollEnabled;
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && this.canScrollHorizontally();
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && this.canScrollVertically();
    }
}
