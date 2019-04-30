package com.ktar5.tileeditor.scene.sidebars.properties;

import com.kotcrab.vis.ui.util.form.FormInputValidator;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.properties.ParentProperty;
import com.ktar5.tileeditor.util.KChangeListener;

import java.util.Optional;
import java.util.function.Consumer;

public class EditParentDialog {
    //TODO show path
    public static void create(ParentProperty property, Consumer<Optional<String>> consumer) {
        // Create the custom dialog.
        VisWindow dialog = new VisWindow("Edit Property");

        String previousValue = property.getName();

        VisTable grid = new VisTable();
        grid.pad(20, 0, 10, 0);

        VisValidatableTextField propertyName = new VisValidatableTextField();
        propertyName.setText(property.getName());

        grid.add(new VisLabel("Name:")).space(10);
        grid.add(propertyName);

        dialog.add(grid);

        VisTable buttonTable = new VisTable(true);
        VisTextButton cancelButton = new VisTextButton("Cancel");
        cancelButton.addListener(new KChangeListener((changeEvent, actor) -> {
            dialog.fadeOut();
        }));
        VisTextButton okayButton = new VisTextButton("Confirm", new KChangeListener((changeEvent, actor) -> {
            consumer.accept(Optional.of((propertyName.getText())));
            dialog.fadeOut();
        }));
        buttonTable.add(okayButton, cancelButton);

        SimpleFormValidator simpleFormValidator = new SimpleFormValidator(okayButton);
        simpleFormValidator.notEmpty(propertyName, "cannot be empty");
        simpleFormValidator.custom(propertyName, new FormInputValidator("still the same") {
            @Override
            protected boolean validate(String input) {
                return !previousValue.equals(input);
            }
        });
        simpleFormValidator.custom(propertyName, new FormInputValidator("already exists in parent") {
            @Override
            protected boolean validate(String input) {
                return !property.getParent().getChildren().containsKey(input);
            }
        });

        dialog.row();
        dialog.add(buttonTable);
        dialog.centerWindow();
        dialog.setWidth(250);
        Main.getInstance().getRoot().addActor(dialog.fadeIn());
    }


}
