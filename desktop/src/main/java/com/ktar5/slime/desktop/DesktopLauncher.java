package com.ktar5.slime.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.desktop.steamworks.SteamSDK;
import org.tinylog.Logger;

//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

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
        Logger.debug("A");
        createApplication();
        Logger.debug("B");
    }

    private static Lwjgl3Application createApplication() {
        Logger.debug("c");
        if(env == Environment.GAME){
            Logger.debug("d");
            SteamSDK steamSDK = new SteamSDK();
            Logger.debug("d.1");
            Sync sync = new Sync();
            Logger.debug("d.2");
            Lwjgl3ApplicationConfiguration gameConfiguration = getGameConfiguration();
            Logger.debug("d.3");
            SlimeGame slimeGame = new SlimeGame(steamSDK, sync);
            Logger.debug("d.3.1");
            Lwjgl3Application lwjgl3Application = null;
            try {
                lwjgl3Application = new Lwjgl3Application(slimeGame, gameConfiguration);
            } catch (Exception e){
                Logger.debug(e);
            }
            Logger.debug("d.4");
            return lwjgl3Application;
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

//    private static LwjglApplicationConfiguration getEditorConfiguration() {
//        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
//        configuration.title = "Slip 'n Slime";
//        configuration.width = 960;
//        configuration.height = 540;
//        configuration.vSyncEnabled = true;
//        configuration.foregroundFPS = 60;
//        configuration.backgroundFPS = 60;
//        for (int size : new int[]{128, 64, 32, 16}) {
//            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
//        }
//        return configuration;
//    }
    
}