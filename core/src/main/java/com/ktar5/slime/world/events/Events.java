package com.ktar5.slime.world.events;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.Gate;

public final class Events {

    public static void setGate(String xString, String yString, String openClose) {
        System.out.println("CALLED SETGATE");
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();

        int x = Integer.valueOf(xString);
        int y = currentLevel.getGrid().height - 1 - Integer.valueOf(yString);
        boolean open = openClose.equalsIgnoreCase("open");

        Gate gate = ((Gate) currentLevel.getGrid().grid[x][y]);
        gate.open = open;
        System.out.println(gate.opening);
    }

    public static void setGraphic(String xString, String yString, String layer, String tilesetID) {
        System.out.println("CALLED SETGRAPHIC");
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        int x = Integer.valueOf(xString);
        int y = currentLevel.getGrid().height - 1 - Integer.valueOf(yString);
        int tileID = Integer.valueOf(tilesetID);
        TiledMapTileLayer mapLayer = ((TiledMapTileLayer) currentLevel.getTileMap().getLayers().get(layer));
        mapLayer.setCell(x, y, null);
        //mapLayer.getCell(x,y).setTile(currentLevel.getTileMap().getTileSets().getTile(tileID));
        //mapLayer.getCell(x, y).getTile().setTextureRegion(currentLevel.getTileMap().getTileSets().getTile(tileID).getTextureRegion());
    }

}
