package com.ktar5.tileeditor.scene.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.internal.FilePopupMenu;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.util.KChangeListener;
import com.ktar5.utilities.common.data.Wrap;

import java.io.File;
import java.io.IOException;

public class KtarFilePopupMenu extends FilePopupMenu {
    private MenuItem createFileMenuItem;

    public KtarFilePopupMenu(FileChooser chooser, FilePopupMenuCallback callback) {
        super(chooser, callback);
        Wrap<Dialogs.InputDialog> wrapper = new Wrap<>(null);
        Dialogs.InputDialog inputDialog = new Dialogs.InputDialog("Create a JSON file", "File Name", true, input -> {
            return !input.isEmpty() && !new File(chooser.getCurrentDirectory().file(), input + ".json").exists();
        }, new InputDialogListener() {
            @Override
            public void finished(String input) {
                File directory = chooser.getCurrentDirectory().file();
                File newFile = new File(directory, input + ".json");
                try {
                    //noinspection ResultOfMethodCallIgnored
                    newFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                wrapper.get().fadeOut();
            }

            @Override
            public void canceled() {

            }
        });
        wrapper.set(inputDialog);

        createFileMenuItem = new MenuItem("New File");
        createFileMenuItem.addListener(new KChangeListener((changeEvent, actor) -> {
            Main.getInstance().getRoot().addActor(inputDialog);
            inputDialog.fadeIn();
        }));

    }


    @Override
    public void build() {
        super.build();
        addItem(createFileMenuItem);
    }

    @Override
    public void build(Array<FileHandle> favorites, FileHandle file) {
        super.build(favorites, file);
        addItem(createFileMenuItem);
    }

}
