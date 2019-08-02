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
            Main.getInstance().mainScene.getSceneRenderer().getScene().save();
        }));

        final MenuItem saveAs = new MenuItem("Save As..", new KChangeListener((changeEvent, actor) -> {

        }));


        addItem(open);
        addItem(newScene);
        addItem(save);
        addItem(saveAs);
    }
}