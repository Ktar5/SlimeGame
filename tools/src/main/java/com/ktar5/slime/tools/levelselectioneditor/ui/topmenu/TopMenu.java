package com.ktar5.slime.tools.levelselectioneditor.ui.topmenu;

import com.kotcrab.vis.ui.widget.MenuBar;

public class TopMenu extends MenuBar {

    public TopMenu() {
        super();
        this.addMenu(new FileMenu());
    }

}
