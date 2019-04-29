package com.ktar5.tileeditor.scene.dialogs;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.ktar5.tileeditor.Main;

import java.util.function.Consumer;

public class LoadDialog {

    public static void create(String title, String extensionDescription, String extension, Consumer<FileHandle> consumer) {
        FileChooser fileChooser = Main.getInstance().getRoot().getFileChooser();
        fileChooser.setMode(FileChooser.Mode.OPEN);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setName(title);
        FileTypeFilter fileTypeFilter = new FileTypeFilter(false);
        fileTypeFilter.addRule(extensionDescription, extension);
        fileChooser.setFileTypeFilter(fileTypeFilter);
        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                consumer.accept(files.get(0));
                fileChooser.fadeOut();
            }
        });
        Main.getInstance().getRoot().addActor(fileChooser.fadeIn());
    }

}
