package com.ktar5.slime.tools.levelselectioneditor.ui.sidebar.path;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.ktar5.slime.tools.levelselectioneditor.Main;
import com.ktar5.slime.tools.levelselectioneditor.Path;
import com.ktar5.slime.tools.levelselectioneditor.scene.Scene;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.KChangeListener;
import lombok.Getter;

@Getter
public class PathSidebar extends Table {

    private PathSelection pathSelection;
    private PathData pathData;

    private Table internalTable;

    private VisTextButton createPathButton;

    public PathSidebar() {
        internalTable = new Table();
        pathData = new PathData();
        pathSelection = new PathSelection();
        this.add(internalTable).grow();
        createPathButton = new VisTextButton("Create New Path");
        createPathButton.addListener(new KChangeListener((changeEvent, actor) -> {
            Scene scene = Main.getInstance().mainStage.getSceneRenderer().getScene();
            int size = scene.getPaths().values().size();
            Path path = new Path(scene, "path" + (size + 1));
            scene.getPaths().put(path.getPathID(), path);
            pathSelection.getAdapter().add(path);
            pathSelection.itemsChanged();
        }));
    }

    public void setToPathData(Path path) {
        internalTable.clear();
        pathData.setPath(path);
        internalTable.add(pathData).grow();
    }

    public void setToSelection() {
        internalTable.clear();
        internalTable.add(createPathButton).row();
        internalTable.add(pathSelection.getMainTable());
    }

    public void resetPathListView(){
        internalTable.clear();
        pathSelection = new PathSelection();
        setToSelection();
    }

}
