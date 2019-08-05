package com.ktar5.slime.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ktar5.slime.tools.levelselectioneditor.Main;

/**
 * Launches the desktop (LWJGL) application.
 */
public class EditorLauncher {
    public static final Environment env = Environment.EDITOR;

    public enum Environment{
        GAME,
        EDITOR
    }

    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new Main(), getEditorConfiguration());
    }

    private static LwjglApplicationConfiguration getEditorConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "slimeGame";
        configuration.width = 960;
        configuration.height = 540;
        configuration.vSyncEnabled = true;
        configuration.foregroundFPS = 60;
        configuration.backgroundFPS = 0;
//        for (int size : new int[]{128, 64, 32, 16}) {
//            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
//        }
        return configuration;
    }

}