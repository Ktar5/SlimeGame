package com.ktar5.slime.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.desktop.steamworks.SteamSDK;
import com.ktar5.tileeditor.Main;

/**
 * Launches the desktop (LWJGL) application.
 */
public class DesktopLauncher {
    public static final Environment env = Environment.GAME;

    public enum Environment{
        GAME,
        EDITOR
    }

    public static void main(String[] args) {
        createApplication();
    }
    
    private static LwjglApplication createApplication() {
        if(env == Environment.GAME){
            return new LwjglApplication(new SlimeGame(new SteamSDK()), getGameConfiguration());
        }else if(env == Environment.EDITOR){
            return new LwjglApplication(new Main(), getEditorConfiguration());
//            return new LwjglApplication(new SlimeGame(), getEditorConfiguration());
        }
        return null;
    }
    
    private static LwjglApplicationConfiguration getGameConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "Slip 'n Slime";
        configuration.width = 960;
        configuration.height = 540;
        configuration.vSyncEnabled = false;
        configuration.foregroundFPS = 60;
        configuration.backgroundFPS = 60;
        for (int size : new int[]{128, 64, 32, 16}) {
            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
        }
        return configuration;
    }

    private static LwjglApplicationConfiguration getEditorConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "Slip 'n Slime";
        configuration.width = 960;
        configuration.height = 540;
        configuration.vSyncEnabled = true;
        configuration.foregroundFPS = 60;
        configuration.backgroundFPS = 60;
        for (int size : new int[]{128, 64, 32, 16}) {
            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
        }
        return configuration;
    }
    
}