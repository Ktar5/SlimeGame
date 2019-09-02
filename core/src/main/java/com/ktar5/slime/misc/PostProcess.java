package com.ktar5.slime.misc;

import com.badlogic.gdx.graphics.Pixmap;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.GaussianBlurEffect;
import com.crashinvaders.vfx.effects.VignetteEffect;
import lombok.Getter;

@Getter
public class PostProcess {

    private VfxManager vfxManager;
    private GaussianBlurEffect vfxEffect;
    private VignetteEffect vignetteEffect;

    public PostProcess(){
        // VfxManager is a manager for the effects.
        // It captures rendering into internal off-screen buffer and applies a chain of defined effects.
        // Off-screen buffers may have any pixel format, but for the better effect mixing
        // it's recommended to use values with an alpha component (e.g. RGBA8888 or RGBA4444).
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);

        // Create and add an effect.
        // VfxEffect derivative classes serve as controllers for the effects.
        // They usually provide some public properties to configure and control the effects.
        vfxEffect = new GaussianBlurEffect();
        vignetteEffect = new VignetteEffect(false);
        vfxManager.addEffect(vfxEffect);
        vfxManager.addEffect(vignetteEffect);
    }

}
