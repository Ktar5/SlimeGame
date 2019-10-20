package com.ktar5.slime.tools.levelselectioneditor.ui.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.*;

public class InputDialog extends VisWindow {
    private InputDialogListener listener;
    private VisTextArea field;
    private VisTextButton okButton;
    private VisTextButton cancelButton;

    public InputDialog(String title, String fieldTitle, InputDialogListener listener) {
        super(title);
        this.setResizable(true);
        this.setHeight(250f);
        this.setWidth(400f);

        this.listener = listener;

        TableUtils.setSpacingDefaults(this);
        setModal(true);

        addCloseButton();
        closeOnEscape();

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setIgnoreSpacing(true);
        buttonBar.setButton(ButtonBar.ButtonType.CANCEL, cancelButton = new VisTextButton(ButtonBar.ButtonType.CANCEL.getText()));
        buttonBar.setButton(ButtonBar.ButtonType.OK, okButton = new VisTextButton(ButtonBar.ButtonType.OK.getText()));

        VisTable fieldTable = new VisTable(true);

        field = new VisTextArea();

        if (fieldTitle != null) fieldTable.add(new VisLabel(fieldTitle));

        fieldTable.add(field).expand().fill().width(250).height(125);

        add(fieldTable).padTop(3).spaceBottom(4);
        row();
        add(buttonBar.createTable()).padBottom(3);

        addListeners();

//        pack();
        centerWindow();
    }

    @Override
    protected void close() {
        super.close();
        listener.canceled();
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage != null)
            field.focusField();
    }

    public InputDialog setText(String text) {
        return setText(text, false);
    }

    /**
     * @param selectText if true text will be selected (this can be useful if you want to allow user quickly erase all text).
     */
    public InputDialog setText(String text, boolean selectText) {
        field.setText(text);
        field.setCursorPosition(text.length());
        if (selectText) {
            field.selectAll();
        }

        return this;
    }

    private void addListeners() {
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listener.finished(field.getText());
                fadeOut();
            }
        });

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });

        field.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                //keycode == Input.Keys.ENTER &&
//                if (okButton.isDisabled() == false) {
//                    listener.finished(field.getText());
//                    fadeOut();
//                }

                return super.keyDown(event, keycode);
            }
        });
    }
}