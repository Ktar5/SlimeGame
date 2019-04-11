package com.ktar5.gameengine.postprocessing.effects;

import com.ktar5.gameengine.postprocessing.PostProcessorEffect;

public abstract class Antialiasing extends PostProcessorEffect {

    public abstract void setViewportSize(int width, int height);
}
