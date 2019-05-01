package com.ktar5.tileeditor.scene.tabs;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

import java.util.UUID;

public class TabHoldingPane extends TabbedPane {
    private Table pane = new Table();

    public TabHoldingPane() {
        super();
        addListener(new TabbedPaneAdapter(){
            @Override
            public void switchedTab (Tab tab) {
                Table content = tab.getContentTable();

                pane.clearChildren();
                pane.add(content).expand().fill();
            }
        });
    }

    public void addTab(AbstractTab tab) {
        this.add(tab);
    }

//    public void setSelectedTab(UUID uuid) {
//        AbstractTab tab = getTab(uuid);
//
//    }

    public void setChanges(UUID uuid, boolean value) {
        getTab(uuid).setEdit(value);
    }

    public AbstractTab getCurrentTab() {
        return ((AbstractTab) this.getActiveTab());
    }

    public Table getTabTable(){
        return pane;
    }

    public AbstractTab getTab(UUID uuid) {
        AbstractTab tab = getCurrentTab();
        if (tab != null && tab.getTabId().equals(uuid)) {
            return tab;
        }
        for (int i = 0; i < getTabs().size ; i++) {
            Tab forTab = getTabs().get(i);
            if (forTab instanceof AbstractTab && ((AbstractTab) forTab).getTabId().equals(uuid)) {
                return ((AbstractTab) forTab);
            }
        }
        return null;
    }


}
