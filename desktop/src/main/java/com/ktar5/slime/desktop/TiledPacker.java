package com.ktar5.slime.desktop;

import com.badlogic.gdx.tiledmappacker.TiledMapPacker;

import java.io.File;

public class TiledPacker {
    public static void main(String args[]) {
        File file = new File("assets\\maps\\generated");
        //deleteDir(file);
        TiledMapPacker.main(new String[]{"assets\\maps", "assets\\maps\\generated", "--strip-unused"});

        //TexturePacker.Settings s = new TexturePacker.Settings();
        //TexturePacker.process(s, "Main/workfiles/finished", "Android/assets/data", "loading.pack");
    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
}
