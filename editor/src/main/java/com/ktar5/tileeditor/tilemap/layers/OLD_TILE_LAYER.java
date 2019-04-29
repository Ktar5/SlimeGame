//package com.ktar5.tileeditor.tilemap.layers;
//
//import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.ktar5.tileeditor.tilemap.Tilemap;
//import com.ktar5.tileeditor.tilemap.whole.WholeTile;
//import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//import org.json.JSONObject;
//
//public class OLD_TILE_LAYER extends BaseLayer {
//
//    public TileLayer(Tilemap parent, JSONObject json) {
//        super(parent, json);
//    }
//
//    @Override
//    public void deserializeBlock(String block, int x, int y) {
//        if (block.equals("0")) {
//            return;
//        } else {
//            String[] split = block.split("_");
//            if (split.length == 2) {
//                this.grid[x][y] = new WholeTile(Integer.valueOf(split[1]), 0,
//                        getParent().getTilesets().getTileset(split[0]));
//            } else if (split.length == 3) {
//                this.grid[x][y] = new WholeTile(Integer.valueOf(split[1]), Integer.valueOf(split[2]),
//                        getParent().getTilesets().getTileset(split[0]));
//            } else {
//                System.out.println("Length > 3 error");
//            }
//        }
//    }
//
//    @Override
//    public JSONObject serialize() {
//        return null;
//    }
//
//    @Override
//    public void onClick(MouseEvent event) {
//        int x = (int) (event.getX() / this.getTileWidth());
//        int y = (int) (event.getY() / this.getTileHeight());
//
//        if (event.getButton().equals(MouseButton.PRIMARY)) {
//            setCurrent(x, y);
//        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
//            remove(x, y);
//        }
//    }
//
//    @Override
//    public void onDrag(MouseEvent event) {
//        int x = (int) (event.getX() / this.getTileWidth());
//        int y = (int) (event.getY() / this.getTileHeight());
//
//        if (x >= getWidth() || y >= getHeight() || x < 0 || y < 0) return;
//        if (isDragging() && x == previousX && y == previousY) return;
//
//        previousX = x;
//        previousY = y;
//
//        if (event.getButton().equals(MouseButton.PRIMARY)) {
//            setCurrent(x, y);
//        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
//            remove(x, y);
//        }
//        refreshHighlight(x, y);
//    }
//
//    @Override
//    public void onDragStart(MouseEvent event) {
//        onDrag(event);
//    }
//
//    @Override
//    public void onDragEnd(MouseEvent event) {
//
//    }
//
//    private void refreshHighlight(int x, int y) {
//        if (rect == null) {
//            rect = new Rectangle(0, 0, 16, 16);
//            rect.setFill(Color.AQUA.deriveColor(0, 1, 1, .5f));
//            EditorCoordinator.get().getEditor().getTabDrawingPane(getParent().getId()).getChildren().add(rect);
//        }
//        rect.toFront();
//        rect.setTranslateX(x * this.getTileWidth());
//        rect.setTranslateY(y * this.getTileWidth());
//    }
//
//    @Override
//    public void onMove(MouseEvent event) {
//        int x = (int) (event.getX() / this.getTileWidth());
//        int y = (int) (event.getY() / this.getTileHeight());
//
//        if (x >= getWidth() || y >= getHeight() || x < 0 || y < 0) return;
//        if (x == previousX && y == previousY) return;
//
//        previousX = x;
//        previousY = y;
//
//        refreshHighlight(x, y);
//    }
//
//    @Override
//    public void draw(Pane pane) {
//        for (int y = getHeight() - 1; y >= 0; y--) {
//            for (int x = 0; x <= getWidth() - 1; x++) {
//                if (grid[x][y] == null) {
//                    continue;
//                }
//                int blockId = ((WholeTile) grid[x][y]).getBlockId();
//                if (blockId == 0) {
//                    continue;
//                }
//                grid[x][y].draw(pane, x * getTileWidth(), y * getTileHeight());
//            }
//        }
//    }
//
//    public void set(int x, int y, WholeTile tile) {
//        if (tile == null || getTileset() == null) {
//            return;
//        }
//        remove(x, y);
//        this.grid[x][y] = tile;
//        this.grid[x][y].updateAllImageViews();
//        Pane pane = EditorCoordinator.get().getEditor().getTabDrawingPane(getId());
//        this.grid[x][y].draw(pane, x * getTileWidth(), y * getTileHeight());
//        setChanged(true);
//    }
//
//    public void setCurrent(int x, int y) {
//        if (getTileset() == null) return;
//
//        if (grid[x][y] != null && grid[x][y] instanceof WholeTile) {
//            WholeTile wholeTile = (WholeTile) this.grid[x][y];
//            wholeTile.setBlockId(currentId);
//            wholeTile.setDirection(currentData);
//            wholeTile.updateAllImageViews();
//        } else {
//            remove(x, y);
//            this.grid[x][y] = new WholeTile(currentId, currentData, getTileset());
//            this.grid[x][y].updateAllImageViews();
//            Pane pane = EditorCoordinator.get().getEditor().getTabDrawingPane(getId());
//            this.grid[x][y].draw(pane, x * getTileWidth(), y * getTileWidth());
//        }
//        setChanged(true);
//    }
//
//    public void remove(int x, int y) {
//        if (grid[x][y] == null) {
//            return;
//        }
//        this.grid[x][y].remove(EditorCoordinator.get().getEditor().getTabDrawingPane(getId()));
//        this.grid[x][y] = null;
//        setChanged(true);
//    }
//
//    @Override
//    public void render(SpriteBatch batch, float dTime) {
//
//    }
//
//    @Override
//    public void debug(float dTime) {
//
//    }
//
//}
