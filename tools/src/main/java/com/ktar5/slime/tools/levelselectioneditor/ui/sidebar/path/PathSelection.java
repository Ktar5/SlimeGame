package com.ktar5.slime.tools.levelselectioneditor.ui.sidebar.path;

import com.kotcrab.vis.ui.widget.ListView;
import com.ktar5.slime.tools.levelselectioneditor.Main;
import com.ktar5.slime.tools.levelselectioneditor.Path;

import java.util.ArrayList;

public class PathSelection extends ListView<Path> {


    public PathSelection() {
        super(new Adapter(new ArrayList<>(Main.getInstance().mainStage.getSceneRenderer().getScene().getPaths().values())));
        this.setItemClickListener(item -> {
            Main.getInstance().mainStage.getSidebar().getPathSidebar().setToPathData(item);
        });
        this.setUpdatePolicy(UpdatePolicy.ON_DRAW);
    }

    public void itemsChanged(){
        ((Adapter) getAdapter()).itemsChanged();
    }


}
