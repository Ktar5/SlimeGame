package com.ktar5.tileeditor.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.internal.FilePopupMenu;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;
import com.ktar5.tileeditor.scene.menu.TopMenu;
import com.ktar5.tileeditor.scene.tabs.AbstractTab;
import com.ktar5.tileeditor.scene.tabs.TabHoldingPane;
import com.ktar5.tileeditor.scene.utils.KtarFilePopupMenu;
import lombok.Getter;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Getter
public class Root extends Stage {
    private VisTable mainTable = new VisTable();
    private VisTable editorTable = new VisTable();

    private TopMenu topMenu;
    private FileChooser fileChooser;
    private TabHoldingPane tabHoldingPane;

    public Root() {
        super(new ScreenViewport(), new SpriteBatch());
        Gdx.input.setInputProcessor(this);

        FileChooser.setDefaultPrefsName("com.ktar5.tileeditor.scene.filepicker");

        fileChooser = new FileChooser(FileChooser.Mode.OPEN);

        mainTable.setFillParent(true);
        mainTable.top();

        //Add menu bar
        topMenu = new TopMenu();
        mainTable.add(topMenu.getTable()).fillX().expandX();
        mainTable.row();

        //Add tab holding pane
        tabHoldingPane = new TabHoldingPane();
        mainTable.add(tabHoldingPane.getTable()).expandX().fillX();
        mainTable.row();

        tabHoldingPane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab(Tab tab) {
                editorTable.clearChildren();
                editorTable.add(tab.getContentTable()).expand().fill();
            }

            @Override
            public void removedTab(Tab tab) {
                ((AbstractTab) tab).onClosed();
            }

            @Override
            public void removedAllTabs() {
                editorTable.clearChildren();
            }
        });
//        editorTable.setBackground("button");

        mainTable.add(editorTable).grow();
        this.addActor(mainTable);
    }

    public void registerTool(MenuItem toolMenuItem) {
        this.getTopMenu().getToolMenu().addItem(toolMenuItem);
    }

    @Override
    public void act() {
        super.act();
//        Array.ArrayIterator<Tab> arrayIterator = new Array.ArrayIterator<>(tabHoldingPane.getTabs());
//        for (Tab tab : arrayIterator) {
//            if (tab instanceof TilemapTab) {
//                TilemapTab tab1 = (TilemapTab) tab;
//                tab1.update();
//            }
//        }
    }

    public void injectFileChooser() {
        KtarFilePopupMenu ktarFilePopupMenu = new KtarFilePopupMenu(fileChooser, new FilePopupMenu.FilePopupMenuCallback() {
            @Override
            public void showNewDirDialog() {
                Class<FileChooser> fileChooserClass = FileChooser.class;
                try {
                    Method m = fileChooserClass.getDeclaredMethod("showNewDirectoryDialog");
                    m.setAccessible(true);
                    m.invoke(fileChooser);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void showFileDelDialog(FileHandle file) {
                Class<FileChooser> fileChooserClass = FileChooser.class;
                try {
                    Method m = fileChooserClass.getDeclaredMethod("showFileDeleteDialog", FileHandle.class);
                    m.setAccessible(true);
                    m.invoke(fileChooser, file);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            FieldUtils.writeField(fileChooser, "fileMenu", ktarFilePopupMenu, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
