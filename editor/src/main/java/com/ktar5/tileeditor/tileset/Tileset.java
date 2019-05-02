package com.ktar5.tileeditor.tileset;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ktar5.tileeditor.properties.RootProperty;
import com.ktar5.tileeditor.util.Tabbable;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.mini2Dx.gdx.utils.IntMap;
import org.pmw.tinylog.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Getter
public class Tileset implements Tabbable {
    public static final int SCALE = 1;
    private RootProperty rootProperty;

    private UUID id;
    private IntMap<TextureRegion> tileImages;
    private Texture originalImage;
    private File sourceFile, saveFile;
    private int tileWidth, tileHeight;
    private int paddingVertical, paddingHorizontal;
    private int offsetLeft, offsetUp;
    private int dimensionX, dimensionY, columns, rows;
    @Setter
    private boolean dragging;

    /**
     * This constructor is used to deserialize tilesets.
     * It **MUST** be overriden by all subclasses.
     *
     * @param saveFile the file used for saving
     * @param json     the json serialization of the tileset
     */
    public Tileset(File saveFile, JSONObject json) {
        this(Paths.get(saveFile.getPath()).resolve(json.getString("sourceFile")).toFile(),
                saveFile,
                json.getJSONObject("padding").getInt("vertical"),
                json.getJSONObject("padding").getInt("horizontal"),
                json.getJSONObject("offset").getInt("left"),
                json.getJSONObject("offset").getInt("up"),
                json.getInt("tileWidth"),
                json.getInt("tileHeight"));
        rootProperty.deserialize(json.getJSONObject("properties"));
    }

    /**
     * This constructor is used to initialize tilesets on creation.
     * It **MUST** be overrided by all subclasses.
     *
     * @param sourceFile        the image file representing this tileset
     * @param saveFile          the file that the tileset should be saved to
     * @param paddingVertical   the vertical padding between each tile in pixels (excluding the very top)
     * @param paddingHorizontal the horizontal padding between each tile in pixels (excluding the very left)
     * @param offsetLeft        the offset, in pixels, from the left edge of the image where the tiles start
     * @param offsetUp          the offset, in pixels, from the top edge of the image where the tiles start
     * @param tileWidth         the width of each tile in pixels
     * @param tileHeight        the height of each tile in pixels
     */
    public Tileset(File sourceFile, File saveFile, int paddingVertical, int paddingHorizontal,
                   int offsetLeft, int offsetUp, int tileWidth, int tileHeight) {
        this.sourceFile = sourceFile;
        this.saveFile = saveFile;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.offsetLeft = offsetLeft;
        this.offsetUp = offsetUp;
        rootProperty = new RootProperty();

        this.paddingHorizontal = paddingHorizontal;
        this.paddingVertical = paddingVertical;
        tileImages = new IntMap<>();
        this.id = UUID.randomUUID();

        originalImage = new Texture(new FileHandle(sourceFile));
        columns = (originalImage.getWidth() - getOffsetLeft()) / (getTileWidth() + getPaddingHorizontal());
        rows = (originalImage.getHeight() - getOffsetUp()) / (getTileHeight() + getPaddingVertical());
        this.dimensionX = columns * getTileWidth();
        this.dimensionY = rows * getTileHeight();
        getTilesetImages(originalImage);

        this.tileHeight *= SCALE;
        this.tileWidth *= SCALE;
        this.offsetLeft = offsetLeft * SCALE;
        this.offsetUp = offsetUp * SCALE;
        this.paddingHorizontal = paddingHorizontal * SCALE;
        this.paddingVertical = paddingVertical * SCALE;
    }

    /**
     * Scales an image using a proper image scaling library.
     *
     * @param sbi    the BufferedImage to be scaled
     * @param factor the scalar to scale by
     * @return the BufferedImage
     */
    public static BufferedImage scale(BufferedImage sbi, int factor) {
//        return Scalr.resize(sbi, Scalr.Method.SPEED, sbi.getWidth() * factor);
        throw new NotImplementedException("Not yet implemented scale");
    }


    /**
     * Initializes and sets {@link Tileset#tileImages}
     */
    public void getTilesetImages(Texture texture) {
        int index = 0;

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getColumns(); col++) {
                TextureRegion region = new TextureRegion(texture,
                        getOffsetLeft() + getPaddingHorizontal() + (((2 * getPaddingHorizontal()) + getTileWidth()) * col),
                        getOffsetUp() + getPaddingVertical() + (((2 * getPaddingVertical()) + getTileHeight()) * row),
                        getTileWidth(), getTileHeight()
                );
                this.getTileImages().put(index++, region);
            }
        }
    }

    /**
     * Serializes the information stored in the tileset to a json file.
     * Subclasses should override this and provide specific implementations.
     * NOTE: all subclasses must call super or else code won't compile.
     */
    @Override
    @CallSuper
    public JSONObject serialize() {
        JSONObject json = new JSONObject();

        rootProperty.serialize(json);

        JSONObject padding = new JSONObject();
        padding.put("horizontal", this.getPaddingHorizontal());
        padding.put("vertical", this.getPaddingVertical());
        json.put("padding", padding);

        JSONObject offset = new JSONObject();
        offset.put("left", this.getOffsetLeft());
        offset.put("up", this.getOffsetUp());
        json.put("offset", offset);

        json.put("tileWidth", tileWidth);
        json.put("tileHeight", tileHeight);

        Path path = Paths.get(this.getSaveFile().getPath())
                .relativize(Paths.get(this.getSourceFile().getPath()));
        json.put("sourceFile", path.toString());
        return json;
    }

    /**
     * Removes the map from the application.
     */
    @Override
    public void remove() {
        TilesetManager.get().remove(getId());
    }

    /**
     * Returns a UUID that should be randomly generated in the constructor of any subclass
     */
    @Override
    public UUID getId() {
        return this.id;
    }

    /**
     * Saves the tileset to a file.
     */
    @Override
    public void save() {
        TilesetManager.get().saveTileset(getId());
    }

    /**
     * Gets the name of the tileset.
     */
    @Override
    public String getName() {
        return getSaveFile().getName();
    }

    /**
     * Returns the save file
     */
    @Override
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * Change the save file to a different file.
     */
    @Override
    public void updateSaveFile(File file) {
        this.saveFile = file;
    }

    public int getIdFromXY(int x, int y) {
        if (y >= getRows() || x >= getColumns() || x < 0 || y < 0) {
            Logger.debug("ERROR >> Finding tileset tile x: " + x + " y:" + y + " OUT OF BOUNDS");
            return -1;
        }
        return (y * (getColumns())) + x;
    }

    public TextureRegion getTextureRegionFromXY(int x, int y) {
        int id = getIdFromXY(x, y);
        if (id == -1) {
            return null;
        }
        return getTileImages().get(id);
    }

    public Tile getTileFromXY(int x, int y) {
        int id = getIdFromXY(x, y);
        if (id == -1) {
            return null;
        }
        return new Tile(id, 0, this);
    }


}
