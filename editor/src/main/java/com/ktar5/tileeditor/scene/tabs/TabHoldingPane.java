package com.ktar5.tileeditor.scene.tabs;

import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;

import java.util.UUID;

public class TabHoldingPane extends TabbedPane {
    public TabHoldingPane() {
        super();
    }

    public void addTab(AbstractTab tab) {
        this.add(tab);
    }

    public AbstractTab getCurrentTab() {
        return ((AbstractTab) this.getActiveTab());
    }

    public AbstractTab getTab(UUID uuid) {
        AbstractTab tab = getCurrentTab();
        if (tab != null && tab.getTabId().equals(uuid)) {
            return tab;
        }
        for (int i = 0; i < getTabs().size; i++) {
            Tab forTab = getTabs().get(i);
            if (forTab instanceof AbstractTab && ((AbstractTab) forTab).getTabId().equals(uuid)) {
                return ((AbstractTab) forTab);
            }
        }
        return null;
    }


}
