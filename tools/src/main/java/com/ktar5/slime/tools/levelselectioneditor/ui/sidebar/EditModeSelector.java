package com.ktar5.slime.tools.levelselectioneditor.ui.sidebar;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.ktar5.slime.tools.levelselectioneditor.Main;
import com.ktar5.slime.tools.levelselectioneditor.input.Input;
import com.ktar5.slime.tools.levelselectioneditor.input.InputMode;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.KChangeListener;
import lombok.Getter;

@Getter
public class EditModeSelector extends Table {

    public EditModeSelector() {
        VisTextButton pointButton = new VisTextButton("Point");
        pointButton.setProgrammaticChangeEvents(false);
        VisTextButton pathButton = new VisTextButton("Path");
        pathButton.setProgrammaticChangeEvents(false);

        pointButton.addListener(new KChangeListener((changeEvent, actor) -> {
            pointButton.setChecked(true);
            pathButton.setChecked(false);
            Main.getInstance().mainStage.getSidebar().setEditMode(EditMode.POINT);
            Main.getInstance().mainStage.getSidebar().setPointSidebar(null);
            Input.inputMode = InputMode.SELECT_POINT;
        }));

        pathButton.addListener(new KChangeListener((changeEvent, actor) -> {
            pointButton.setChecked(false);
            pathButton.setChecked(true);
            Main.getInstance().mainStage.getSidebar().setEditMode(EditMode.PATH);
            Main.getInstance().mainStage.getSidebar().setPathSidebar();
        }));

        this.add(pointButton).center().padLeft(25).padRight(25);
        this.add(pathButton).center().padLeft(25).padRight(25);
        this.top();
    }

}
