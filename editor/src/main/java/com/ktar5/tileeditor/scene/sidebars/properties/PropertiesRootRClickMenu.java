package com.ktar5.tileeditor.scene.sidebars.properties;

import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.properties.Property;
import com.ktar5.tileeditor.properties.RootProperty;
import com.ktar5.tileeditor.util.KChangeListener;

public class PropertiesRootRClickMenu extends PopupMenu {

    public PropertiesRootRClickMenu(RootProperty property, PropertiesSidebar sidebar) {
        MenuItem createMenuItem = new MenuItem("Create", new KChangeListener((changeEvent, actor) -> {
            Dialogs.showInputDialog(getStage(), "Create New Property", "Name: ", new InputDialogAdapter() {
                @Override
                public void finished(String input) {
                    if (input.isEmpty()) {
                        return;
                    }
                    Property newProperty = property.createProperty(input);
                    sidebar.addNode(sidebar.nodes.get(property.getPath()), newProperty);
                    Main.getInstance().getRoot().getTabHoldingPane().getCurrentTab().setDirty(true);
                }
            });
        }));
        add(createMenuItem);
    }

}
