package com.ktar5.tileeditor.scene.dialogs;

import com.ktar5.tileeditor.Main;

import static com.kotcrab.vis.ui.util.dialog.Dialogs.showErrorDialog;

public class GenericAlert {

    public GenericAlert(String message) {
        showErrorDialog(Main.getInstance().getRoot(), message);
    }
    
}
