package com.ktar5.gameengine.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.*;

public class AnimationLoader {
    private HashMap<String, Animation<TextureRegion>> animations;
    private HashMap<String, Texture> textures;
    
    private List<String> blacklist = Arrays.asList(
            "..\\assets\\default_skin\\uiskin.atlas");
    
    
    private final boolean checkAtlasIndexes = false;
    
    public AnimationLoader() {
        animations = new HashMap<>();
        textures = new HashMap<>();
//        File internal = new File("../assets");
//        searchAtlases(internal);
    }

//    public void searchAtlases(File root) {
//        new FileScanner(root, (file) -> {
//            if (file.getName().endsWith(".atlas") && !file.getPath().contains("maps")) {
//                System.out.println(file.getPath());
//                loadAtlas(file.getPath());
//                return true;
//            }
//            return false;
//        });
//    }
    
    public Animation<TextureRegion> getAnimation(String animation) {
        if (animation.contains(animation)) {
            return animations.get(animation);
        } else {
            throw new RuntimeException("No animation found for value: " + animation + ". Check your capitalization?");
        }
    }
    
    public void loadAtlas(String atlasPath) {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        HashMap<String, List<TextureAtlas.AtlasRegion>> atlasAnimations = new HashMap<>();
        for (TextureAtlas.AtlasRegion region : regions) {
            if (atlasAnimations.containsKey(region.name)) {
                atlasAnimations.get(region.name).add(region);
            } else {
                //noinspection ArraysAsListWithZeroOrOneArgument
                List<TextureAtlas.AtlasRegion> newList = new ArrayList<>();
                newList.add(region);
                atlasAnimations.put(region.name, newList);
            }
        }
        for (Map.Entry<String, List<TextureAtlas.AtlasRegion>> stringListEntry : atlasAnimations.entrySet()) {
            List<TextureAtlas.AtlasRegion> value = stringListEntry.getValue();
            int largestIndex = value.size() - 1;
            Collections.sort(value, (o1, o2) -> o1.index - o2.index);
            if(checkAtlasIndexes){
                for (TextureAtlas.AtlasRegion region : value) {
                    if (region.index > largestIndex || region.index < 0 && !blacklist.contains(atlasPath)) {
                        throw new IllegalArgumentException("Check indexes for animation " + stringListEntry.getKey() + " on atlas " + atlasPath);
                    }
                }
                
            }
            animations.put(stringListEntry.getKey(), new Animation<>(1 / 15f, value.toArray(new TextureAtlas.AtlasRegion[value.size()])));
        }
    }
    
    public Animation<TextureRegion> getTextureAsAnimation(String filename) {
        if (animations.containsKey(filename)) {
            return animations.get(filename);
        } else {
            Texture texture;
            if (textures.containsKey(filename)) {
                texture = textures.get(filename);
            } else {
                texture = new Texture(Gdx.files.internal(filename));
                textures.put(filename, texture);
            }
            animations.put(filename, new Animation<>(1, new TextureRegion(texture)));
            return animations.get(filename);
        }
    }
    
    public Texture getTexture(String filename) {
        if (textures.containsKey(filename)) {
            return textures.get(filename);
        } else {
            Texture texture = new Texture(Gdx.files.internal(filename));
            textures.put(filename, texture);
            return texture;
        }
    }
    
}

