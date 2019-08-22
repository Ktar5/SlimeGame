package com.ktar5.slime.tools.levelselectioneditor.ui.sidebar.path;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ktar5.slime.tools.levelselectioneditor.Path;
import lombok.Getter;

@Getter
public class PathSidebar extends Table {

    private PathSelection pathSelection;
    private PathData pathData;

    private Table internalTable;

    public PathSidebar() {
        internalTable = new Table();
        pathData = new PathData();
        pathSelection = new PathSelection();
        this.add(internalTable).grow();
    }

    public void setToPathData(Path path) {
        internalTable.clear();
        pathData.setPath(path);
        internalTable.add(pathData).grow();
    }

    public void setToSelection() {
        internalTable.clear();
        internalTable.add(pathSelection.getMainTable());
    }

}
