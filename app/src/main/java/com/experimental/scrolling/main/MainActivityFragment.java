package com.experimental.scrolling.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSunVisibility();
        setUpSolarSystem();
    }

//    private int topViewHeight;

    private void setUpSunVisibility() {
//        binding.sunSection.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                topViewHeight = binding.sunSection.getMeasuredHeight();
//            }
//        });
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
