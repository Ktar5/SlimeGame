package com.ktar5.tileeditor.scene.topmenu;

import com.kotcrab.vis.ui.widget.MenuBar;
import lombok.Getter;

@Getter
public class TopMenu extends MenuBar {
    public TopMenu() {
        super();
        this.addMenu(new FileMenu());
        this.addMenu(new EditMenu());
        this.addMenu(new MapMenu());
    }
}
