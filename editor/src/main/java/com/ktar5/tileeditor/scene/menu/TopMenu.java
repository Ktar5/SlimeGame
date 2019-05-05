package com.ktar5.tileeditor.scene.menu;

import com.kotcrab.vis.ui.widget.MenuBar;
import lombok.Getter;

@Getter
public class TopMenu extends MenuBar {
    private ToolMenu toolMenu;

    public TopMenu() {
        super();
        this.addMenu(new FileMenu());
        this.addMenu(new EditMenu());
        this.addMenu(new MapMenu());
        toolMenu = new ToolMenu();
        this.addMenu(toolMenu);
    }
}
