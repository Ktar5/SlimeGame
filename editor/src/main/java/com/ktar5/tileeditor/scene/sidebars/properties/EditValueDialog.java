package com.ktar5.tileeditor.scene.sidebars.properties;

import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.properties.StringProperty;
import com.ktar5.tileeditor.util.KChangeListener;
import javafx.util.Pair;

import java.util.Optional;
import java.util.function.Consumer;

public class EditValueDialog {
    public static void create(StringProperty property, Consumer<Optional<Pair<String, String>>> consumer) {
        // Create the custom dialog.
        VisWindow dialog = new VisWindow("Edit Property");

        VisTable grid = new VisTable();
        grid.pad(20, 0, 10, 0);

        VisValidatableTextField propertyName = new VisValidatableTextField();
        propertyName.setText(property.getName());

        VisValidatableTextField propertyValue = new VisValidatableTextField();
        propertyValue.setText(property.getValue());

        grid.add(new VisLabel("Name:")).space(10);
        grid.add(propertyName);
        grid.row();
        grid.add(new VisLabel("Value:")).space(10);
        grid.add(propertyValue);

        VisTable buttonTable = new VisTable(true);
        VisTextButton okayButton = new VisTextButton("Confirm", new KChangeListener((changeEvent, actor) -> {
            consumer.accept(Optional.of(new Pair<>(propertyName.getText(), propertyValue.getText())));
            dialog.fadeOut();
        }));
        VisTextButton cancelButton = new VisTextButton("Cancel");
        cancelButton.addListener(new KChangeListener((changeEvent, actor) -> {
            dialog.fadeOut();
        }));
        buttonTable.add(okayButton, cancelButton);

        SimpleFormValidator simpleFormValidator = new SimpleFormValidator(okayButton);
        simpleFormValidator.notEmpty(propertyName, "cannot be empty");

        dialog.add(grid);
        dialog.row();
        dialog.add(buttonTable);
        dialog.centerWindow();
        dialog.setWidth(250);
        Main.getInstance().getRoot().addActor(dialog.fadeIn());
    }
}
