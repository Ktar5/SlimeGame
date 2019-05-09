package com.ktar5.gameengine.analytics;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.utils.SharedLibraryLoader;

public enum Platform {
    Windows, Linux, Android, iOS, WebGL, MacOS;

    public static Platform getDefaultPlatform(Application.ApplicationType type) {
        switch (type) {
            case Android:
                return Android;
            case WebGL:
                return WebGL;
            default:
                if (SharedLibraryLoader.isWindows)
                    return Windows;
                else if (SharedLibraryLoader.isLinux)
                    return Linux;
                else if (SharedLibraryLoader.isIos)
                    return iOS;
                else if (SharedLibraryLoader.isMac)
                    return MacOS;
                else
                    throw new IllegalStateException("You need to set a platform");
        }
    }


}
