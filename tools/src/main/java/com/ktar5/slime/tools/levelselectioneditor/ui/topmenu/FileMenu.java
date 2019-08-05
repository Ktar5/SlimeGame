package com.ktar5.slime.tools.levelselectioneditor.ui.topmenu;

import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.ktar5.slime.tools.levelselectioneditor.Main;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.KChangeListener;

public class FileMenu extends Menu {


    public FileMenu() {
        super("File");

        final MenuItem open = new MenuItem("Open Scene", new KChangeListener((changeEvent, actor) -> {
//            Main.getManager().loadScene();
        }));

        final MenuItem newScene = new MenuItem("New Scene", new KChangeListener((changeEvent, actor) -> {
//            MapManager.get().createScene();
        }));

        final MenuItem save = new MenuItem("Save Current", new KChangeListener((changeEvent, actor) -> {
            Main.getInstance().mainStage.getSceneRenderer().getScene().save();
        }));

        final MenuItem saveAs = new MenuItem("Save As..", new KChangeListener((changeEvent, actor) -> {

        }));

        final MenuItem setTexture = new MenuItem("Set Texture", new KChangeListener((changeEvent, actor) -> {

        }));

        final MenuItem setStartingControl = new MenuItem("Set Starting Control", new KChangeListener((changeEvent, actor) -> {

        }));

        addItem(newScene);
        addItem(open);

        addSeparator();
        addItem(save);
        addItem(saveAs);

        addSeparator();
        addItem(setTexture);
        addItem(setStartingControl);

    }
}