# Scrolling and Pinning

After reviewing several blog posts, I needed a few more repititions to get my head around the finer points of the CoordinatorLayout and its scrolling capabilities.

https://guides.codepath.com/android/handling-scrolls-with-coordinatorlayout
http://saulmm.github.io/mastering-coordinator

CoordinatorLayout is a ViewGroup that implements NestedScrollingParent2.
https://developer.android.com/reference/android/support/design/widget/CoordinatorLayout
https://developer.android.com/reference/android/view/ViewGroup
https://developer.android.com/reference/android/support/v4/view/NestedScrollingParent2

That means that it can have children that are views or layouts and that scroll.

The first child of CoordinatorLayout is AppBarLayout.
CoordinatorLayout uses an AppBarLayout to contain the unpinned and pinned view children.
AppBarLayout is a LinearLayout that defaults to vertical orientation.
It handles scrolling gestures.
https://developer.android.com/reference/android/support/design/widget/AppBarLayout

The first child of AppBarLayout will be the layout that contains business views that are unpinned.
That is, views that can be scrolled up and under the app toolbar with a gesture.
For example, this container can be a LinearLayout with vertical orientation.
The important attribute to include in the layout scroll flag to scroll.
```xml
<LinearLayout
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:background="@color/colorSun"
  android:orientation="vertical"
  app:layout_scrollFlags="scroll">
...
  </LinearLayout>
```

The second child of AppBarLayout is the CollapsingToolbarLayout.
CollapsingToolbarLayout is a FrameLayout that implements a collapsing app bar.
https://developer.android.com/reference/android/support/design/widget/CollapsingToolbarLayout

It also manages children that are marked to be pinned.
It is important to allow it to be resized at runtime.  Layout width and height should match parent.  The minimum height should be 0dp.  The scroll flags need to allow scrolling and the content to disappear (exitUntilCollapsed).
```xml
<android.support.design.widget.CollapsingToolbarLayout
  android:id="@+id/toolbar_layout"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  android:minHeight="0dp"
  app:contentScrim="?android:attr/colorPrimary"
  app:layout_scrollFlags="scroll|exitUntilCollapsed"
  app:toolbarId="@+id/toolbar">
```

The first child of CollapsingToobarLayout is the Toolbar.
https://developer.android.com/reference/android/support/v7/widget/Toolbar
The important configuration settings of the Toolbar are the height of the pinned area and the collapse mode (pin).
```xml
<android.support.v7.widget.Toolbar
android:id="@+id/toolbar"
android:layout_width="match_parent"
android:layout_height="24dp"
app:layout_collapseMode="pin"
app:popupTheme="@style/AppTheme.PopupOverlay" />
```
I discovered by tracing through the CollapsingToolbarLayout that it inherits the height of its Toolbar child as its minimum height.  This is the height at which collapsing stops.
```java
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    ...
        // Finally, set our minimum height to enable proper AppBarLayout collapsing
        if (mToolbar != null) {
            if (mCollapsingTitleEnabled && TextUtils.isEmpty(mCollapsingTextHelper.getText())) {
                // If we do not currently have a title, try and grab it from the Toolbar
                mCollapsingTextHelper.setText(mToolbar.getTitle());
            }
            if (mToolbarDirectChild == null || mToolbarDirectChild == this) {
                setMinimumHeight(getHeightWithMargins(mToolbar));
            } else {
               setMinimumHeight(getHeightWithMargins(mToolbarDirectChild));
            }
        }
    ...
    }
```

The second child of CollapsingToolbarLayout is the layout of content that is to be pinned.
This layout should be equal in height to the Toolbar.  This allows it to be fully flush with the top of the screen when it is scrolled to the top.
```xml
<TextView
  android:layout_width="wrap_content"
  android:layout_height="24dp"
  android:layout_gravity="start|bottom"
  android:text="@string/custom_stuff_to_see_after_scrolling"
  android:textAppearance="@android:style/TextAppearance.Medium" />
```
If whitespace is needed, then added a top margin to the Toolbar.  The CollapsingToolbarLayout includes this value in the calculated height at which collapsing stops.
```xml
<android.support.v7.widget.Toolbar
  android:id="@+id/toolbar"
  android:layout_width="match_parent"
  android:layout_height="24dp"
  android:layout_marginTop="64dp"
  app:layout_collapseMode="pin"
  app:popupTheme="@style/AppTheme.PopupOverlay" />
```

The second child of CoordinatorLayout is the layout that is the container of content (ie. other views) that are visible under the pinned view container.  For example, this might be a list of information.
