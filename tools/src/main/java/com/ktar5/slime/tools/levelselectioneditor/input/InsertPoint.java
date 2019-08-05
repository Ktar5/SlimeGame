package com.ktar5.slime.tools.levelselectioneditor.input;

import com.ktar5.slime.tools.levelselectioneditor.Path;
import com.ktar5.slime.tools.levelselectioneditor.scene.Scene;
import com.ktar5.slime.tools.levelselectioneditor.scene.SceneRenderer;

public class InsertPoint {

    public static void act(Scene scene, Path path, int x, int y, SceneRenderer renderer) {
        if (path != null) {
            int xFixed = (int) ((x - renderer.getRenderX()) / renderer.getScale());
            int yFixed = (int) ((y - renderer.getRenderY()) / renderer.getScale());

            System.out.println("Fixed: " + xFixed + " , " + yFixed);

            //TODO
//                    PathPoint point = new PathPoint(, xFixed, yFixed);
//                    path.addPoint(point);
        }
    }

}
