package com.ktar5.tileeditor.scene.centerview.tabs;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.ktar5.tileeditor.util.Tabbable;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class AbstractTab extends Tab {
    private final UUID tabId;
    //    protected EditorPane pane;
    private boolean hasEdits = false;
    private final VisTable content;

    public AbstractTab(UUID uuid) {
        super(true);
        content = new VisTable();
        this.tabId = uuid;
        //TODO
//        this.setOnCloseRequest(e -> {
//            if (this.hasEdits) quitSaveConfirmation(e, getTabbable());
//        });
//        this.setOnClosed(e -> getTabbable().remove());

        //TODO  OH
//        pane.getViewport().addEventFilter(MouseEvent.MOUSE_CLICKED, (event) -> {
//            if (getTabbable().isDragging()) {
//                getTabbable().setDragging(false);
//                getTabbable().onDragEnd(event);
//            } else {
//                getTabbable().onClick(event);
//            }
//        });
//        pane.getViewport().addEventFilter(MouseEvent.MOUSE_MOVED, getTabbable()::onMove);
//        pane.getViewport().addEventFilter(MouseEvent.MOUSE_DRAGGED, (event) -> {
//            if (!getTabbable().isDragging()) {
//                getTabbable().onDragStart(event);
//                //VERY IMPORTANT TO SET DRAG AFTER DRAG START
//                getTabbable().setDragging(true);
//            } else {
//                getTabbable().onDrag(event);
//            }
//        });
    }

    @Override
    public String getTabTitle() {
        return hasEdits ? getTabbable().getName() + "*" : getTabbable().getName();
    }

    @Override
    public Table getContentTable() {
        return content;
    }

    public abstract Tabbable getTabbable();

    public abstract void onSelect();

    public void setEdit(boolean value) {
        if (value == hasEdits) {
            return;
        }
        hasEdits = value;
        //TODO update name
    }

}
