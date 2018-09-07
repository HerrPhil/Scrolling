package com.experimental.scrolling.sun;

import android.support.annotation.NonNull;

import com.experimental.scrolling.R;
import com.experimental.scrolling.databinding.SunDetailBinding;
import com.xwray.groupie.databinding.BindableItem;

public class SunItem extends BindableItem<SunDetailBinding> {

    private String description;

    private int position;

    public SunItem(String description) {
        this.description = description;
    }

    @Override
    public int getLayout() {
        return R.layout.sun_detail;
    }

    @Override
    public void bind(@NonNull SunDetailBinding viewBinding, int position) {
        viewBinding.setDescription(this.description);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
