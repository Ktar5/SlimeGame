package com.ktar5.slime.tools.levelselectioneditor.ui.topmenu;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.ktar5.slime.tools.levelselectioneditor.Main;
import com.ktar5.slime.tools.levelselectioneditor.points.ControlPoint;
import com.ktar5.slime.tools.levelselectioneditor.scene.Scene;
import com.ktar5.slime.tools.levelselectioneditor.ui.MainStage;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.KChangeListener;
import org.json.JSONObject;
import org.tinylog.Logger;

import static com.ktar5.slime.screens.levelselection.NodeLevelSelectionScreen.readFileAsString;

public class FileMenu extends Menu {


    public FileMenu() {
        super("File");

        final MenuItem open = new MenuItem("Open Scene", new KChangeListener((changeEvent, actor) -> {
            FileChooser fileChooser = Main.getInstance().mainStage.getFileChooser();
            fileChooser.setMode(FileChooser.Mode.OPEN);
            fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
            fileChooser.setName("Load Scene / World");
            FileTypeFilter fileTypeFilter = new FileTypeFilter(false);
            fileTypeFilter.addRule("Scene / World File", "json");
            fileChooser.setFileTypeFilter(fileTypeFilter);
            fileChooser.setMultiSelectionEnabled(false);

            fileChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(Array<FileHandle> files) {
                    FileHandle file = files.get(0);
                    if (file == null) {
                        Logger.info("Tried to load world / scene, cancelled or failed");
                    }
                    Logger.info("Beginning to load from file: " + file.path());

                    String data = readFileAsString(file.file());
                    if (data == null || data.isEmpty()) {
                        return;
                    }

                    Scene scene = new Scene(file.file(), new JSONObject(data));
                    Main.getInstance().mainStage.setScene(scene);

                    Logger.info("Finished loading: " + file.path());
                    fileChooser.fadeOut();
                }
            });

            Main.getInstance().mainStage.addActor(fileChooser.fadeIn());
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
            MainStage mainStage = Main.getInstance().mainStage;
            if(mainStage.getSidebar().getSelectedPoint() == null)
                return;
            if (!(mainStage.getSidebar().getSelectedPoint() instanceof ControlPoint))
                return;

            Main.getInstance().mainStage.getSceneRenderer().getScene().setStartingControlPoint(((ControlPoint) mainStage.getSidebar().getSelectedPoint()).getControlID());
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