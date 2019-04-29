package com.ktar5.tileeditor.scene.utils;

import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class NumberTextField extends VisValidatableTextField {

    public NumberTextField(String promptText) {
        super(promptText);
        this.addValidator(new Validators.IntegerValidator());
    }

    public NumberTextField(String promptText, int defaultNumber) {
        this(promptText);
        this.setText(String.valueOf(defaultNumber));
    }

    public int getNumber() {
        return Integer.valueOf(this.getText());
    }

}
