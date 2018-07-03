package com.experimental.scrolling.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.experimental.scrolling.R;
import com.experimental.scrolling.databinding.FragmentMainBinding;
import com.experimental.scrolling.planet.AppBarStateChangeListener;
import com.experimental.scrolling.planet.OffsetChangeState;
import com.experimental.scrolling.planet.Planet;
import com.experimental.scrolling.planet.PlanetItem;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.view.View.*;
import static android.view.View.GONE;

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

    private void setUpSunVisibility() {
        binding.solarSystem.appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChange(AppBarLayout appBarLayout, OffsetChangeState state) {
                Timber.d("AppBarStateChangeListener.onStateChange: state = %s", state.name());
                switch (state) {
                    case EXPANDED:
                        Timber.d("AppBarStateChangeListener.onStateChange: set sun section visible");
                        binding.sunSection.setVisibility(VISIBLE);
                        break;
                    case COLLAPSED:
                        Timber.d("AppBarStateChangeListener.onStateChange: set sun section gone");
                        binding.sunSection.setVisibility(GONE);
                        break;
                    case IDLE:
                    default:
                        Timber.d("AppBarStateChangeListener.onStateChange: do nothing");
                        break;
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
