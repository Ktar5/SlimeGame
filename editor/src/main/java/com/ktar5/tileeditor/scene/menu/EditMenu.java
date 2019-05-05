package com.ktar5.tileeditor.scene.menu;


import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;

public class EditMenu extends Menu {
    
    public EditMenu() {
        super("Edit");
        this.addItem(new MenuItem("Undo"));
        this.addItem(new MenuItem("Redo"));
    }
    
}
