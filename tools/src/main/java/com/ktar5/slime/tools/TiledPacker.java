package com.ktar5.slime.tools;

import com.badlogic.gdx.tiledmappacker.TiledMapPacker;

import java.io.File;

public class TiledPacker {
    public static void main(String args[]) {
        File file = new File("assets\\maps\\generated");
        //deleteDir(file);
        TiledMapPacker.main(new String[]{"assets\\maps", "assets\\maps\\generated", "--strip-unused", "--combine-tilesets"});
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
