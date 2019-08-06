package com.ktar5.slime.tools.levelselectioneditor.ui.sidebar;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ktar5.slime.tools.levelselectioneditor.points.Point;
import com.ktar5.slime.tools.levelselectioneditor.ui.sidebar.path.PathSidebar;
import lombok.Getter;

public class Sidebar extends Table {
    private EditMode editMode = EditMode.POINT;

    @Getter
    private PathSidebar pathSidebar;
    private PointSidebar pointSidebar;

    private Container<Table> dataContainer;

    public Sidebar() {
        pathSidebar = new PathSidebar();
        pointSidebar = new PointSidebar();
        dataContainer = new Container<>();

        this.debugAll();
        //Add the edit mode selector
        this.add(new EditModeSelector()).top().growX().padTop(10).padBottom(10);

        this.row();

        //Add dataContainer
        this.add(dataContainer).growY().growX();
    }

    public void setEditMode(EditMode editMode) {
        this.editMode = editMode;
    }

    public void setPointSidebar(Point point) {
        dataContainer.clear();
        dataContainer.setActor(pointSidebar);
        pointSidebar.setPoint(point);
    }

    public void setPathData() {
        dataContainer.clear();
        dataContainer.setActor(pathSidebar);
        pathSidebar.setToSelection();
    }

}
