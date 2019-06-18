package com.ktar5.gameengine.util;

import com.badlogic.gdx.utils.Base64Coder;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.io.*;


public abstract class GameData {
    protected final File file;

    public GameData(File saveDirectory) {
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }

        file = new File(saveDirectory, "save.data");

        if (!this.file.exists()) {
            try {
                file.createNewFile();

//                InputStream read = Gdx.files.internal("data/save.data").read();
//
//                byte[] buffer = new byte[read.available()];
//                read.read(buffer);
//
//                OutputStream outStream = new FileOutputStream(file);
//                outStream.write(buffer);
                saveGame();
            } catch (IOException e) {
                Logger.error(e);
            }
        } else {
            String s = readFileAsString(file);
            if (s == null) {
                Logger.error(new RuntimeException("Failed to load file, contents are null."));
                return;
            }
            s = Base64Coder.decodeString(s);
            deserialize(new JSONObject(s));
        }

    }

    protected abstract JSONObject serialize();

    protected abstract void deserialize(JSONObject json);

    //String[] lines = StringUtils.split(FileUtils.readFileToString(new File("...")), '\n');
    private String readFileAsString(File file) {
        StringBuilder fileData = new StringBuilder();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                fileData.append(String.valueOf(buf, 0, numRead));
            }
            reader.close();
            fr.close();
        } catch (IOException e) {
            Logger.info(e);
            return null;
        }
        return fileData.toString();
    }

    public void saveGame() {
        try {
            if (this.file.exists()) {
                this.file.delete();
                this.file.createNewFile();
            }

            FileWriter writer = new FileWriter(this.file);
            String s = serialize().toString(4);
            s = Base64Coder.encodeString(s);
            writer.write(s);
            writer.close();
        } catch (IOException e) {
            Logger.error("Unable to save file");
            Logger.error(e);
        }
    }


}
