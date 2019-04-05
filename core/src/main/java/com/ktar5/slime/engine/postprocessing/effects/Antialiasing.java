package com.ktar5.slime.engine.postprocessing.effects;

import com.ktar5.slime.engine.postprocessing.PostProcessorEffect;

public abstract class Antialiasing extends PostProcessorEffect {

    public abstract void setViewportSize(int width, int height);
}
