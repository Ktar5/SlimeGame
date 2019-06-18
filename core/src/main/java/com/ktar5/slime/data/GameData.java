package com.ktar5.slime.data;

import com.badlogic.gdx.Gdx;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.io.*;


public class GameData {
    private final File file;

    public GameData() {
        File savedirectory = new File(System.getProperty("user.home")
                + File.separator + "documents"
                + File.separator + "Wildmagic Studios"
                + File.separator + "A Story of Slime");
        if (!savedirectory.exists()) {
            savedirectory.mkdirs();
        }
        file = new File(savedirectory, "save.data");


        if (!this.file.exists()) {
            try {
                file.createNewFile();

                InputStream read = Gdx.files.internal("data/save.data").read();

                byte[] buffer = new byte[read.available()];
                read.read(buffer);

                OutputStream outStream = new FileOutputStream(file);
                outStream.write(buffer);
            } catch (IOException e) {
                Logger.error(e);
            }


        }

//        new JSONObject(readFileAsString(file));
    }

    private JSONObject serialize() {
        return null;
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
            if (this.file.exists()) {
                this.file.delete();
                this.file.createNewFile();
            }

            FileWriter writer = new FileWriter(this.file);
            String s = serialize().toString(4);
//            Base64Coder.encodeString(Base64Coder.encodeString(s=));
//            writer.write();
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to save file :(");
            Logger.error(e);
        }
    }


}
