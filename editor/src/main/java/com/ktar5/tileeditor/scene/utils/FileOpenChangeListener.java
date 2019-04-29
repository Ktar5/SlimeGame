package com.ktar5.tileeditor.scene.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.ktar5.tileeditor.Main;

public class FileOpenChangeListener extends ChangeListener {
    private FileChooser fileChooser;

    public FileOpenChangeListener(FileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        Main.getInstance().getRoot().addActor(fileChooser.fadeIn());
    }
}
