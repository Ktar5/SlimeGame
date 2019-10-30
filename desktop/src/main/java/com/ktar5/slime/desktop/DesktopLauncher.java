package com.ktar5.slime.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.desktop.steamworks.SteamSDK;

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

    private static Lwjgl3Application createApplication() {
        if(env == Environment.GAME){
            return new Lwjgl3Application(new SlimeGame(new SteamSDK(), new Sync()), getGameConfiguration());
        }else if(env == Environment.EDITOR){
//            return new Lwjgl3Application(new Main(), getEditorConfiguration());
//            return new LwjglApplication(new SlimeGame(), getEditorConfiguration());
        }
        return null;
    }
    
    private static Lwjgl3ApplicationConfiguration getGameConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Slip 'n Slime");
//        configuration.width = 960;
//        configuration.height = 540;
        configuration.useVsync(true);
        configuration.setIdleFPS(60);
        configuration.setWindowedMode(960, 540);
//        for (int size : new int[]{128, 64, 32, 16}) {
//            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
//        }
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