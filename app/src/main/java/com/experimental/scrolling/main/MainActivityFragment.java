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
import com.experimental.scrolling.planet.Planet;
import com.experimental.scrolling.planet.PlanetItem;
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
        setUpSun();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSolarSystem();
    }

    private void setUpSun() {
        // The collapsing toolbar layout (FrameLayout) stretches over the whole collapsing area.
        // The app needs to delegate click events on that area to buttons below it.
        // get click parent
        View clickParent = binding.solarSystem.toolbarLayout;
        clickParent.post(new Runnable() {
            // Post in the parent's message queue to make sure the parent lays out its children
            // before you call getHitRect().
            @Override
            public void run() {
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
                binding.solarSystem.sunButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), R.string.button_description_1, Toast.LENGTH_SHORT).show();
                    }
                });
                binding.solarSystem.sunButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), R.string.button_description_2, Toast.LENGTH_SHORT).show();
                    }
                });
                binding.solarSystem.sunButton3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), R.string.button_description_3, Toast.LENGTH_SHORT).show();
                    }
                });

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
            }
        });


    }

    private void setUpSolarSystem() {
        if (!(binding.solarSystem.planetContent instanceof RecyclerView)) {
            return;
        }
        RecyclerView recyclerView = (RecyclerView) binding.solarSystem.planetContent;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.divider, this.getContext().getTheme()));
        recyclerView.addItemDecoration(dividerItemDecoration);
        GroupAdapter adapter = new GroupAdapter();
        String[] planets = this.getResources().getStringArray(R.array.planets);
        Section updatingGroup = new Section();
        List<PlanetItem> updatableItems = new ArrayList<>();
        for (String planet : planets) {
            updatableItems.add(new PlanetItem(new Planet(planet)));
        }
        updatingGroup.update(updatableItems);
        adapter.add(updatingGroup);
        recyclerView.setAdapter(adapter);
    }
}
