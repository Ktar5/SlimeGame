package com.ktar5.tileeditor.scene.dialogs;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.scene.utils.FileOpenChangeListener;
import com.ktar5.tileeditor.util.KChangeListener;
import lombok.Builder;
import lombok.Getter;
import org.pmw.tinylog.Logger;

import java.util.function.Consumer;

@Builder
@Getter
public class CreateTilemap {
    private int width, height, tileHeight, tileWidth;
    private FileHandle file;

    public static void create(Consumer<CreateTilemap> consumer) {
        CreateTilemapBuilder builder = new CreateTilemapBuilder();

        // Create the custom dialog.
        VisWindow dialog = new VisWindow("Create New Tilemap");

        FileChooser fileChooser = Main.getInstance().getRoot().getFileChooser();
        fileChooser.setMode(FileChooser.Mode.OPEN);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setName("Create Resource File");
        FileTypeFilter fileTypeFilter = new FileTypeFilter(false);
        fileTypeFilter.addRule("Json File", "json");
        fileChooser.setFileTypeFilter(fileTypeFilter);
        fileChooser.setMultiSelectionEnabled(false);

        VisTextButton openFileButton = new VisTextButton("Select File", new FileOpenChangeListener(fileChooser));

        VisValidatableTextField filePath = new VisValidatableTextField();
        filePath.setWidth(240);
        filePath.setReadOnly(true);

        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                openFileButton.setDisabled(false);
                FileHandle file = files.get(0);
                if (file != null) {
                    filePath.setText(file.path());
                    builder.file(file);
                }
            }
        });

        Logger.debug("Hello this is happening.");

        VisValidatableTextField width = new VisValidatableTextField();
        width.setSize(20, width.getHeight());
        VisValidatableTextField height = new VisValidatableTextField();
        height.setSize(20, width.getHeight());
        VisValidatableTextField tileHeight = new VisValidatableTextField("16");
        tileHeight.setSize(20, width.getHeight());
        VisValidatableTextField tileWidth = new VisValidatableTextField("16");
        tileWidth.setSize(20, width.getHeight());

        VisTable largeValues = new VisTable();
        largeValues.pad(20, 0, 10, 10);

        largeValues.add(new VisLabel("File Path:")).space(5).left();
        largeValues.add(filePath).space(15).left();
        largeValues.add(openFileButton).right();


        VisTable smallValues = new VisTable();
        smallValues.pad(20, 0, 10, 10);

        smallValues.add(new VisLabel("Width:")).space(5).left();
        smallValues.add(width).left().width(40);

        smallValues.add().space(20);
        smallValues.add(new VisLabel("Tile Width:")).space(5).left();
        smallValues.add(tileWidth).width(40);

        smallValues.row();

        smallValues.add(new VisLabel("Height:")).space(5).left();
        smallValues.add(height).left().width(40);
        smallValues.add().space(20);

        smallValues.add(new VisLabel("Tile Height:")).space(5).left();
        smallValues.add(tileHeight).width(40);


        VisTable buttonTable = new VisTable(true);
        // Set the button types.
        VisTextButton doneButton = new VisTextButton("Done", new KChangeListener((changeEvent, actor) -> {
            builder.height(Integer.valueOf(height.getText()));
            builder.width(Integer.valueOf(width.getText()));
            builder.tileHeight(Integer.valueOf(tileHeight.getText()));
            builder.tileWidth(Integer.valueOf(tileWidth.getText()));
            consumer.accept(builder.build());
            dialog.fadeOut();
        }));
        buttonTable.add(doneButton);

        // Enable/Disable login button depending on whether an input was entered.
        doneButton.setDisabled(true);

        SimpleFormValidator simpleFormValidator = new SimpleFormValidator(doneButton);
        simpleFormValidator.notEmpty(width, "width cannot be empty");
        simpleFormValidator.valueGreaterThan(width, "width must be > 0", 0);
        simpleFormValidator.integerNumber(width, "width must be a whole number");

        simpleFormValidator.notEmpty(height, "height cannot be empty");
        simpleFormValidator.valueGreaterThan(height, "height must be > 0", 0);
        simpleFormValidator.integerNumber(height, "height must be a whole number");

        simpleFormValidator.notEmpty(tileWidth, "tile width cannot be empty");
        simpleFormValidator.valueGreaterThan(tileWidth, "tile width must be > 0", 0);
        simpleFormValidator.integerNumber(tileWidth, "tile width must be a whole number");

        simpleFormValidator.notEmpty(tileHeight, "tile height cannot be empty");
        simpleFormValidator.valueGreaterThan(tileHeight, "tile height must be > 0", 0);
        simpleFormValidator.integerNumber(tileHeight, "tile height must be a whole number");

        simpleFormValidator.notEmpty(filePath, "must have a path selected");

        VisTable visTable = new VisTable();
        visTable.add(largeValues);
        visTable.row();
        visTable.add(smallValues);
        visTable.row();
        visTable.add(buttonTable.right()).right();

        dialog.add(visTable);
        dialog.addCloseButton();

        dialog.setWidth(500);
        dialog.setHeight(220);
        dialog.centerWindow();


        Main.getInstance().getRoot().addActor(dialog.fadeIn());
    }

}