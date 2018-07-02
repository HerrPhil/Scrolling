package com.experimental.scrolling;

import android.support.annotation.NonNull;

import com.experimental.scrolling.databinding.PlanetBinding;
import com.xwray.groupie.databinding.BindableItem;

public class PlanetItem extends BindableItem<PlanetBinding> {

    private Planet planet;

    public PlanetItem(Planet planet) {
        this.planet = planet;
    }

    @Override
    public int getLayout() {
        return R.layout.planet;
    }

    @Override
    public void bind(@NonNull PlanetBinding viewBinding, int position) {
        viewBinding.setPlanet(this.planet);
    }
}
