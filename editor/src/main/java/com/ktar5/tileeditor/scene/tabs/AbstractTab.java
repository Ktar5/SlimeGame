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
    private final VisTable content;

    public AbstractTab(UUID uuid) {
        super(true);
        content = new VisTable();
        this.tabId = uuid;
    }

    @Override
    public String getTabTitle() {
        return getTabbable().getName();
    }

    @Override
    public Table getContentTable() {
        return content;
    }

    public abstract void onClosed();

    public abstract Tabbable getTabbable();

    public abstract void onSelect();

}
