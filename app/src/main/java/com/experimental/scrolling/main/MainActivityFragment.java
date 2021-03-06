package com.experimental.scrolling.main;

import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.experimental.scrolling.R;
import com.experimental.scrolling.databinding.FragmentMainBinding;
import com.experimental.scrolling.manager.CustomLinearLayoutManager;
import com.experimental.scrolling.planet.Planet;
import com.experimental.scrolling.planet.PlanetItem;
import com.experimental.scrolling.sun.SunItem;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private FragmentMainBinding binding;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        setUpButtons();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSunDetails();
        setUpSolarSystem();
    }

    private void setUpButtons() {
        // The collapsing toolbar layout (FrameLayout) stretches over the whole collapsing area.
        // The app needs to delegate click events on that area to buttons below it.
        // get click parent
        View clickParent = binding.solarSystem.toolbarLayout;
        // Post in the parent's message queue to make sure the parent lays out its children
        // before you call getHitRect().
        clickParent.post(() -> {
            // The bounds for the delegate views (Buttons)
            Rect delegateArea1 = new Rect();
            Rect delegateArea2 = new Rect();
            Rect delegateArea3 = new Rect();

            // The buttons.
            Button button1 = binding.solarSystem.sunButton1;
            Button button2 = binding.solarSystem.sunButton2;
            Button button3 = binding.solarSystem.sunButton3;

            // Enable buttons.
            button1.setEnabled(true);
            button2.setEnabled(true);
            button3.setEnabled(true);

            // Listener to button clicks.
            binding.solarSystem.sunButton1.setOnClickListener(v -> Toast.makeText(getContext(), R.string.button_description_1, Toast.LENGTH_SHORT).show());
            binding.solarSystem.sunButton2.setOnClickListener(v -> Toast.makeText(getContext(), R.string.button_description_2, Toast.LENGTH_SHORT).show());
            binding.solarSystem.sunButton3.setOnClickListener(v -> Toast.makeText(getContext(), R.string.button_description_3, Toast.LENGTH_SHORT).show());

            // Get hit rectangles.
            button1.getHitRect(delegateArea1);
            button2.getHitRect(delegateArea2);
            button3.getHitRect(delegateArea3);

            // Extend the buttons' touch area
            delegateArea1.left += 4;
            delegateArea1.right += 4;
            delegateArea1.top += 4;
            delegateArea1.bottom += 4;

            delegateArea2.left += 4;
            delegateArea2.right += 4;
            delegateArea2.top += 4;
            delegateArea2.bottom += 4;

            delegateArea3.left += 4;
            delegateArea3.right += 4;
            delegateArea3.top += 4;
            delegateArea3.bottom += 4;

            // Instantiate touch delegates.
            // "delegateAreax" is the bounds in local coordinates of the containing view
            // to be mapped to the delegate view.
            // "buttonx" is the child view that should receive motion events.
            TouchDelegate touchDelegate1 = new TouchDelegate(delegateArea1, button1);
            TouchDelegate touchDelegate2 = new TouchDelegate(delegateArea2, button2);
            TouchDelegate touchDelegate3 = new TouchDelegate(delegateArea3, button3);

            // Set the touch delegate on the parent view.
            // Touches within the touch delegate bounds are routed to the child.
            if (View.class.isInstance(button1.getParent())) {
                ((View) button1.getParent()).setTouchDelegate(touchDelegate1);
            }
            if (View.class.isInstance(button2.getParent())) {
                ((View) button2.getParent()).setTouchDelegate(touchDelegate2);
            }
            if (View.class.isInstance(button3.getParent())) {
                ((View) button3.getParent()).setTouchDelegate(touchDelegate3);
            }
        });
    }

    private void setUpSunDetails() {
        if (!(binding.solarSystem.sunDetailContent instanceof RecyclerView)) {
            return;
        }
        RecyclerView recyclerView = (RecyclerView) binding.solarSystem.sunDetailContent;
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider, getContext().getTheme()));
        recyclerView.addItemDecoration(dividerItemDecoration);
        GroupAdapter adapter = new GroupAdapter();
        adapter.setOnItemClickListener((item, view) -> {
            SunItem sunItem = (SunItem) item;
            Toast.makeText(getContext(), String.format(getResources().getConfiguration().getLocales().get(0), "%d: %s", sunItem.getPosition(), getResources().getString(R.string.sun_more_info)), Toast.LENGTH_SHORT).show();
        });
        String[] descriptions = getResources().getStringArray(R.array.sun_descriptions);
        Section updatingGroup = new Section();
        List<SunItem> items = new ArrayList<>();
        for (String description : descriptions) {
            items.add(new SunItem(description));
        }
        updatingGroup.update(items);
        adapter.add(updatingGroup);
        recyclerView.setAdapter(adapter);
    }

    private void setUpSolarSystem() {
        if (!(binding.solarSystem.planetContent instanceof RecyclerView)) {
            return;
        }
        RecyclerView recyclerView = (RecyclerView) binding.solarSystem.planetContent;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider, getContext().getTheme()));
        recyclerView.addItemDecoration(dividerItemDecoration);
        GroupAdapter adapter = new GroupAdapter();
        String[] planets = getResources().getStringArray(R.array.planets);
        Section updatingGroup = new Section();
        List<PlanetItem> items = new ArrayList<>();
        for (String planet : planets) {
            items.add(new PlanetItem(new Planet(planet)));
        }
        updatingGroup.update(items);
        adapter.add(updatingGroup);
        recyclerView.setAdapter(adapter);
    }
}
