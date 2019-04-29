package com.ktar5.tileeditor.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.function.BiConsumer;

public class KChangeListener extends ChangeListener {
    private BiConsumer<ChangeEvent, Actor> consumer;

    public KChangeListener(BiConsumer<ChangeEvent, Actor> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        consumer.accept(event, actor);
    }
}
