package com.ktar5.tileeditor.scene.dialogs;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.scene.utils.NumberTextField;
import com.ktar5.tileeditor.util.KChangeListener;
import lombok.Builder;
import lombok.Getter;

import java.util.function.Consumer;

@Builder
@Getter
public class CreateTileset {
    private FileHandle sourceFile, tilesetFile;
    private int tileWidth, tileHeight, paddingVertical, paddingHorizontal, offsetLeft, offsetUp;

    public static void create(Consumer<CreateTileset> consumer) {
        CreateTilesetBuilder builder = new CreateTilesetBuilder();

        // Create the custom dialog.
        VisWindow dialog = new VisWindow("Create New Tilemap");


        VisValidatableTextField tilesetPath = new VisValidatableTextField();
        tilesetPath.setReadOnly(true);
        VisValidatableTextField imagePath = new VisValidatableTextField();
        imagePath.setReadOnly(true);

        NumberTextField tileHeightField = new NumberTextField("Tile Height", 16);
        tileHeightField.setSize(20, tileHeightField.getHeight());
        NumberTextField tileWidthField = new NumberTextField("Tile Width", 16);
        tileWidthField.setSize(20, tileHeightField.getHeight());
        NumberTextField paddingVertField = new NumberTextField("Vertical Padding", 0);
        paddingVertField.setSize(20, tileHeightField.getHeight());
        NumberTextField paddingHorzField = new NumberTextField("Horizontal Padding", 0);
        paddingHorzField.setSize(20, tileHeightField.getHeight());
        NumberTextField offsetLeftField = new NumberTextField("Left Offset", 0);
        offsetLeftField.setSize(20, tileHeightField.getHeight());
        NumberTextField offsetUpField = new NumberTextField("Up Offset", 0);
        offsetUpField.setSize(20, tileHeightField.getHeight());

        VisTextButton selectImageFileButton = new VisTextButton("Select Image File", new KChangeListener((changeEvent, actor) -> {
            FileChooser fileChooser = Main.getInstance().getRoot().getFileChooser();
            fileChooser.setMode(FileChooser.Mode.OPEN);
            fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
            fileChooser.setName("Image File");
            FileTypeFilter fileTypeFilter = new FileTypeFilter(false);
            fileTypeFilter.addRule("Image File", "png");
            fileChooser.setFileTypeFilter(fileTypeFilter);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(Array<FileHandle> files) {
                    FileHandle file = files.get(0);
                    if (file != null) {
                        imagePath.setText(file.path());
                        builder.sourceFile(file);
                    }
                }
            });
            Main.getInstance().getRoot().addActor(fileChooser.fadeIn());
        }));

        VisTextButton selectTilesetFileButton = new VisTextButton("Create Tileset File", new KChangeListener((changeEvent, actor) -> {
            FileChooser fileChooser = Main.getInstance().getRoot().getFileChooser();
            fileChooser.setMode(FileChooser.Mode.OPEN);
            fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
            fileChooser.setName("Tileset Chooser");
            FileTypeFilter fileTypeFilter = new FileTypeFilter(false);
            fileTypeFilter.addRule("Json File", "json");
            fileChooser.setFileTypeFilter(fileTypeFilter);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(Array<FileHandle> files) {
                    FileHandle file = files.get(0);
                    if (file != null) {
                        tilesetPath.setText(file.path());
                        builder.tilesetFile(file);
                    }
                }
            });
            Main.getInstance().getRoot().addActor(fileChooser.fadeIn());
        }));

        VisTable largeValues = new VisTable();
        largeValues.pad(20, 0, 10, 10);

//        largeValues.debugAll();
        largeValues.add(new VisLabel("Image Path:")).space(5).left().center();
        largeValues.add(imagePath).space(5).left().center();
        largeValues.add(selectImageFileButton).right().center();

        largeValues.row().spaceBottom(15);
        largeValues.add(new VisLabel("File Path:")).space(5).left();
        largeValues.add(tilesetPath).space(5).left();
        largeValues.add(selectTilesetFileButton).right();


        VisTable smallValues = new VisTable();
        smallValues.pad(20, 0, 10, 10);

        smallValues.add(new VisLabel("Horiz Pad:")).space(5).left();
        smallValues.add(paddingHorzField).left().width(40);

        smallValues.add().space(20);
        smallValues.add(new VisLabel("Tile Width:")).space(5).left();
        smallValues.add(tileWidthField).left().width(40);

        smallValues.add().space(20);
        smallValues.add(new VisLabel("Offset Left:")).space(5).left();
        smallValues.add(offsetLeftField).left().width(40);
        //column, row

        smallValues.row();

        smallValues.add(new VisLabel("Offset Up:")).space(5).left();
        smallValues.add(offsetUpField).left().width(40);

        smallValues.add().space(20);
        smallValues.add(new VisLabel("Tile Height:")).space(5).left();
        smallValues.add(tileHeightField).left().width(40);

        smallValues.add().space(20);
        smallValues.add(new VisLabel("Vert Pad:")).space(5).left();
        smallValues.add(paddingVertField).left().width(40);

        smallValues.row();


        VisTable buttonTable = new VisTable(true);
        // Set the button types.
        VisTextButton doneButton = new VisTextButton("Done", new KChangeListener((changeEvent, actor) -> {
            builder.tileWidth(tileWidthField.getNumber());
            builder.tileHeight(tileHeightField.getNumber());
            builder.paddingVertical(paddingVertField.getNumber());
            builder.paddingHorizontal(paddingHorzField.getNumber());
            builder.offsetLeft(offsetLeftField.getNumber());
            builder.offsetUp(offsetUpField.getNumber());
            consumer.accept(builder.build());
            dialog.fadeOut();
        }));
        buttonTable.add(doneButton);
        // Enable/Disable login button depending on whether an input was entered.
        doneButton.setDisabled(true);


        SimpleFormValidator simpleFormValidator = new SimpleFormValidator(doneButton);
        simpleFormValidator.notEmpty(offsetLeftField, "left offset cannot be empty");
        simpleFormValidator.notEmpty(offsetUpField, "up offset cannot be empty");
        simpleFormValidator.notEmpty(paddingHorzField, "horizontal padding cannot be empty");
        simpleFormValidator.notEmpty(paddingVertField, "vertical padding cannot be empty");
        simpleFormValidator.notEmpty(tileWidthField, "tile width cannot be empty");
        simpleFormValidator.valueGreaterThan(tileWidthField, "tile width must be > 0", 0);
        simpleFormValidator.notEmpty(tileHeightField, "tile height cannot be empty");
        simpleFormValidator.valueGreaterThan(tileHeightField, "tile height must be > 0", 0);

        simpleFormValidator.notEmpty(imagePath, "must have a path selected");
        simpleFormValidator.notEmpty(tilesetPath, "must have a path selected");

        VisTable visTable = new VisTable();
        visTable.add(largeValues);
        visTable.row();
        visTable.add(smallValues);

        dialog.add(visTable);
        dialog.add(buttonTable).bottom().right();

        dialog.addCloseButton();

        dialog.setWidth(500);
        dialog.setHeight(265);
        dialog.centerWindow();

        Main.getInstance().getRoot().addActor(dialog.fadeIn());
    }


}
