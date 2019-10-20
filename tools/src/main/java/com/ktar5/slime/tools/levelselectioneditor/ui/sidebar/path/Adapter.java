package com.ktar5.slime.tools.levelselectioneditor.ui.sidebar.path;

import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.ktar5.slime.tools.levelselectioneditor.Path;

import java.util.ArrayList;

public class Adapter extends ArrayListAdapter<Path, VisLabel> {

    public Adapter(ArrayList<Path> array) {
        super(array);
    }

    @Override
    protected VisLabel createView(Path item) {
        return new VisLabel(item.getName());
    }

    @Override
    protected void updateView(VisLabel view, Path item) {
        view.setText(item.getName());
    }

}