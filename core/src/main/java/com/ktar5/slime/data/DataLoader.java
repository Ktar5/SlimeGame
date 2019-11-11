package com.ktar5.slime.data;

import com.badlogic.gdx.utils.Base64Coder;
import com.ktar5.slime.SlimeGame;
import lombok.Getter;
import org.tinylog.Logger;

import java.io.*;

@Getter
public class DataLoader {
    private GameData gameData;
    private File saveFile;

    public DataLoader(){
        File saveDirectory = new File(System.getProperty("user.home")
                + File.separator + "documents"
                + File.separator + "Wildmagic Studios"
                + File.separator + "Slip n Slime");

        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }

        saveFile = new File(saveDirectory, "save.data");

        if (!this.saveFile.exists()) {
            try {
                gameData = new GameData();
                saveFile.createNewFile();
                saveGame();
            } catch (IOException e) {
                Logger.error(e);
            }
        } else {
            String s = readFileAsString(saveFile);
            if (s == null) {
                Logger.error(new RuntimeException("Failed to load file, contents are null."));
                return;
            }
            s = Base64Coder.decodeString(s);
            gameData = SlimeGame.getGame().getGson().fromJson(s, GameData.class);
        }
    }



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
            if (this.saveFile.exists()) {
                this.saveFile.delete();
            }
            this.saveFile.createNewFile();


            FileWriter writer = new FileWriter(this.saveFile);
            String data = SlimeGame.getGame().getGson().toJson(gameData);
            data = Base64Coder.encodeString(data);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            Logger.error("Unable to save file");
            Logger.error(e);
        }
    }
}
