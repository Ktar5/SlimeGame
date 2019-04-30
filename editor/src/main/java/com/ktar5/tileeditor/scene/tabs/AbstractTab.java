package com.ktar5.tileeditor.scene.tabs;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.ktar5.tileeditor.util.Tabbable;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class AbstractTab extends Tab {
    private final UUID tabId;
    private boolean hasEdits = false;
    private final VisTable content;

    public AbstractTab(UUID uuid) {
        super(true);
        content = new VisTable();
        this.tabId = uuid;
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
