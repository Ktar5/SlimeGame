package com.ktar5.tileeditor.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.ktar5.tileeditor.Main;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import com.ktar5.utilities.annotation.dontoverride.DontOverride;
import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

public interface Tabbable extends Interactable {

    /**
     * Saves the tabbable to a file.
     */
    public void save();

    @DontOverride
    /**
     * Saves the tabbable to a chosen file.
     */
    public default void saveAs() {
        FileChooser fileChooser = Main.getInstance().getRoot().getFileChooser();
        fileChooser.setMode(FileChooser.Mode.SAVE);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setName("Save As..");
        FileTypeFilter fileTypeFilter = new FileTypeFilter(false);
        fileTypeFilter.addRule("Json File", "json");
        fileChooser.setFileTypeFilter(fileTypeFilter);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                FileHandle file = files.get(0);
                if (file != null) {
                    updateSaveFile(file.file());
                    save();
                }
            }
        });
    }

    /**
     * Sets the tabbable to be marked as having an edit/change.
     */
    @DontOverride
    public default void setChanged(boolean value) {
        Main.getInstance().getRoot().getTabHoldingPane().getTab(getId()).setDirty(value);
    }

    /**
     * Returns a UUID that should be randomly generated in the constructor of any subclass
     */
    public UUID getId();

    /**
     * Gets the name of the tabbable.
     */
    public String getName();

    /**
     * Returns the save file
     */
    public File getSaveFile();

    /**
     * Removes the map from the application.
     */
    public void remove();

    /**
     * Change the save file to a different file.
     */
    public void updateSaveFile(File file);

    /**
     * Turn a Tabbable into a JSONObject manually to assure the
     * proper serialization of the data into json.
     *
     * Note: all implementations must call super
     */
    @CallSuper
    public JSONObject serialize();

}
