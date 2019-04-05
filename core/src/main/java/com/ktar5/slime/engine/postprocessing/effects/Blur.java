package com.ktar5.slime.engine.postprocessing.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.ktar5.slime.engine.postprocessing.PostProcessor;
import com.ktar5.slime.engine.postprocessing.PostProcessorEffect;
import com.ktar5.slime.engine.postprocessing.filters.Combine;
import com.ktar5.slime.engine.postprocessing.filters.Threshold;
import com.ktar5.slime.engine.postprocessing.utils.PingPongBuffer;

public class Blur extends PostProcessorEffect {
    public static class Settings {
        public final String name;

        public final com.ktar5.slime.engine.postprocessing.filters.Blur.BlurType blurType;
        public final int blurPasses; // simple blur
        public final float blurAmount; // normal blur (1 pass)
        public final float bloomThreshold;

        public final float bloomIntensity;
        public final float bloomSaturation;
        public final float baseIntensity;
        public final float baseSaturation;

        public Settings(String name, com.ktar5.slime.engine.postprocessing.filters.Blur.BlurType blurType, int blurPasses, float blurAmount, float bloomThreshold,
                        float baseIntensity, float baseSaturation, float bloomIntensity, float bloomSaturation) {
            this.name = name;
            this.blurType = blurType;
            this.blurPasses = blurPasses;
            this.blurAmount = blurAmount;

            this.bloomThreshold = bloomThreshold;
            this.baseIntensity = baseIntensity;
            this.baseSaturation = baseSaturation;
            this.bloomIntensity = bloomIntensity;
            this.bloomSaturation = bloomSaturation;
        }

        // simple blur
        public Settings(String name, int blurPasses, float bloomThreshold, float baseIntensity, float baseSaturation,
                        float bloomIntensity, float bloomSaturation) {
            this(name, com.ktar5.slime.engine.postprocessing.filters.Blur.BlurType.Gaussian5x5b, blurPasses, 0, bloomThreshold, baseIntensity, baseSaturation, bloomIntensity,
                    bloomSaturation);
        }

        public Settings(Bloom.Settings other) {
            this.name = other.name;
            this.blurType = other.blurType;
            this.blurPasses = other.blurPasses;
            this.blurAmount = other.blurAmount;

            this.bloomThreshold = other.bloomThreshold;
            this.baseIntensity = other.baseIntensity;
            this.baseSaturation = other.baseSaturation;
            this.bloomIntensity = other.bloomIntensity;
            this.bloomSaturation = other.bloomSaturation;
        }
    }

    private PingPongBuffer pingPongBuffer;

    private com.ktar5.slime.engine.postprocessing.filters.Blur blur;
    private Threshold threshold;
    private Combine combine;

    private Bloom.Settings settings;

    private boolean blending = false;
    private int sfactor, dfactor;

    public Blur(int fboWidth, int fboHeight) {
        pingPongBuffer = PostProcessor.newPingPongBuffer(fboWidth, fboHeight, PostProcessor.getFramebufferFormat(), false);

        blur = new com.ktar5.slime.engine.postprocessing.filters.Blur(fboWidth, fboHeight);
        threshold = new Threshold();
        combine = new Combine();

        setSettings(new Bloom.Settings("default", 2, 0.277f, 0, 1f, 1f, 1f));
    }

    @Override
    public void dispose() {
        combine.dispose();
        threshold.dispose();
        blur.dispose();
        pingPongBuffer.dispose();
    }

    public void setBaseIntesity(float intensity) {
        combine.setSource1Intensity(intensity);
    }

    public void setBaseSaturation(float saturation) {
        combine.setSource1Saturation(saturation);
    }

    public void setBloomIntesity(float intensity) {
        combine.setSource2Intensity(intensity);
    }

    public void setBloomSaturation(float saturation) {
        combine.setSource2Saturation(saturation);
    }

    public void setThreshold(float gamma) {
        threshold.setTreshold(gamma);
    }

    public void setBlurType(com.ktar5.slime.engine.postprocessing.filters.Blur.BlurType type) {
        blur.setType(type);
    }

    public void setSettings(Bloom.Settings settings) {
        this.settings = settings;

        // setup threshold filter
        setThreshold(settings.bloomThreshold);

        // setup combine filter
        setBaseIntesity(settings.baseIntensity);
        setBaseSaturation(settings.baseSaturation);
        setBloomIntesity(settings.bloomIntensity);
        setBloomSaturation(settings.bloomSaturation);

        // setup blur filter
        setBlurPasses(settings.blurPasses);
        setBlurAmount(settings.blurAmount);
        setBlurType(settings.blurType);
    }

    public void setBlurPasses(int passes) {
        blur.setPasses(passes);
    }

    public void setBlurAmount(float amount) {
        blur.setAmount(amount);
    }

    public com.ktar5.slime.engine.postprocessing.filters.Blur.BlurType getBlurType() {
        return blur.getType();
    }

    public Bloom.Settings getSettings() {
        return settings;
    }

    public int getBlurPasses() {
        return blur.getPasses();
    }

    public float getBlurAmount() {
        return blur.getAmount();
    }

    @Override
    public void render(final FrameBuffer src, final FrameBuffer dest) {
        Texture texsrc = src.getColorBufferTexture();

        boolean blendingWasEnabled = PostProcessor.isStateEnabled(GL20.GL_BLEND);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        pingPongBuffer.begin();
        {
//            // threshold / high-pass filter
//            // only areas with pixels >= threshold are blit to smaller fbo
//            threshold.setInput(texsrc).setOutput(pingPongBuffer.getSourceBuffer()).render();

            // blur pass
            blur.render(pingPongBuffer);
        }
        pingPongBuffer.end();

//        if (blending || blendingWasEnabled) {
//            Gdx.gl.glEnable(GL20.GL_BLEND);
//        }
//
//        if (blending) {
//            // TODO support for Gdx.gl.glBlendFuncSeparate(sfactor, dfactor, GL20.GL_ONE, GL20.GL_ONE );
//            Gdx.gl.glBlendFunc(sfactor, dfactor);
//        }

        restoreViewport(dest);

        // mix original scene and blurred threshold, modulate via
        // set(Base|Bloom)(Saturation|Intensity)
        combine.setOutput(dest).setInput(texsrc, pingPongBuffer.getResultTexture()).render();
    }

    @Override
    public void rebind() {
        blur.rebind();
        threshold.rebind();
        combine.rebind();
        pingPongBuffer.rebind();
    }
}
