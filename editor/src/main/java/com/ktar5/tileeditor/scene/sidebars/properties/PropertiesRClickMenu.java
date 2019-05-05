package com.ktar5.tileeditor.scene.sidebars.properties;

import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.properties.ParentProperty;
import com.ktar5.tileeditor.properties.Property;
import com.ktar5.tileeditor.properties.StringProperty;
import com.ktar5.tileeditor.util.KChangeListener;
import org.pmw.tinylog.Logger;

public class PropertiesRClickMenu extends PopupMenu {

    public PropertiesRClickMenu(Property property, PropertiesSidebar sidebar) {
        super();
        MenuItem editMenuItem = new MenuItem("Dummy", new KChangeListener((changeEvent, actor) -> {
            if (property instanceof ParentProperty) {
                EditParentDialog.create(((ParentProperty) property), s -> s.ifPresent(property::changeName));
            } else {
                EditValueDialog.create(((StringProperty) property), stringStringPair -> stringStringPair.ifPresent(nameAndValue -> {
                    PropertiesSidebar.PropertyNode propertyNode = sidebar.nodes.get(property.getPath());
                    if (nameAndValue.getKey() == null || nameAndValue.getKey().replace(" ", "").equals("")) {
                        return;
                    }
                    if (nameAndValue.getValue() == null) {
                        ((StringProperty) property).setNameAndValue(nameAndValue.getKey(), "");
                    } else {
                        ((StringProperty) property).setNameAndValue(nameAndValue.getKey(), nameAndValue.getValue());
                    }

                    propertyNode.update();
                }));
            }
        }));

        if (property instanceof ParentProperty) {
            editMenuItem.setText("Edit Name");
        } else {
            editMenuItem.setText("Edit..");
        }

        addItem(editMenuItem);
        addSeparator();

        //Create menu
        MenuItem createMenuItem = new MenuItem("Create", new KChangeListener((changeEvent, actor) -> {
            if (!(property instanceof ParentProperty)) return;
            Dialogs.showInputDialog(getStage(), "Create New Property", "Name: ", new InputDialogAdapter() {
                @Override
                public void finished(String input) {
                    if (input.isEmpty()) {
                        return;
                    }
                    Property newProperty = ((ParentProperty) property).createProperty(input, "None");
                    Logger.debug(property.getPath());
                    sidebar.addNode(sidebar.nodes.get(property.getPath()), newProperty);
                    Main.getInstance().getRoot().getTabHoldingPane().getCurrentTab().setDirty(true);
                }
            });
        }));
        addItem(createMenuItem);

        //Delete menu
        MenuItem removeMenuItem = new MenuItem("Delete", new KChangeListener((changeEvent, actor) -> {
            property.getParent().getChildren().remove(property.getName());
            sidebar.removeNode(property.getPath());
            Main.getInstance().getRoot().getTabHoldingPane().getCurrentTab().setDirty(true);
        }));
        addItem(removeMenuItem);
    }

}
