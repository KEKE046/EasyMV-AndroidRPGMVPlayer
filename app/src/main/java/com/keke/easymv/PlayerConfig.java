package com.keke.easymv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

public class PlayerConfig {
    boolean SHOW_FPS = false;
    boolean BACK_BUTTON_QUITS = true;
    boolean BOOTSTRAP_INTERFACE = true;
    boolean FORCE_CANVAS = false;
    boolean FORCE_NO_AUDIO = false;
    boolean FIX_LOCALSTORAGE = true;
    boolean ADD_GAMEPAD = false;
    boolean MANUALLY_START = false;
    String FORCE_AUDIO_EXT = "";
    String indexPage = "";
    public String title = "";

    private Properties toProperties() {
        Properties result = new Properties();
        result.setProperty("SHOW_FPS", this.SHOW_FPS ? "yes" : "no");
        result.setProperty("BACK_BUTTON_QUITS", this.BACK_BUTTON_QUITS ? "yes" : "no");
        result.setProperty("BOOTSTRAP_INTERFACE", this.BOOTSTRAP_INTERFACE ? "yes" : "no");
        result.setProperty("FORCE_CANVAS", this.FORCE_CANVAS ? "yes" : "no");
        result.setProperty("FORCE_NO_AUDIO", this.FORCE_NO_AUDIO ? "yes" : "no");
        result.setProperty("FIX_LOCALSTORAGE", this.FIX_LOCALSTORAGE ? "yes" : "no");
        result.setProperty("ADD_GAMEPAD", this.ADD_GAMEPAD ? "yes" : "no");
        result.setProperty("MANUALLY_START", this.MANUALLY_START ? "yes" : "no");
        result.setProperty("FORCE_AUDIO_EXT", this.FORCE_AUDIO_EXT);
        result.setProperty("title", this.title);
        return result;
    }

    private static PlayerConfig fromPropertirs(Properties properties) {
        PlayerConfig config = new PlayerConfig();
        String valYes = "yes", valNo = "no";
        config.SHOW_FPS = properties.getProperty("SHOW_FPS", valNo).equals("yes");
        config.BACK_BUTTON_QUITS = properties.getProperty("BACK_BUTTON_QUITS", valYes).equals("yes");
        config.BOOTSTRAP_INTERFACE = properties.getProperty("BOOTSTRAP_INTERFACE", valYes).equals("yes");
        config.FORCE_CANVAS = properties.getProperty("FORCE_CANVAS", valNo).equals("yes");
        config.FORCE_NO_AUDIO = properties.getProperty("FORCE_NO_AUDIO", valNo).equals("yes");
        config.FIX_LOCALSTORAGE = properties.getProperty("FIX_LOCALSTORAGE", valYes).equals("yes");
        config.ADD_GAMEPAD = properties.getProperty("ADD_GAMEPAD", valNo).equals("yes");
        config.MANUALLY_START = properties.getProperty("MANUALLY_START", valNo).equals("yes");
        config.FORCE_AUDIO_EXT = properties.getProperty("FORCE_AUDIO_EXT", "");
        config.title = properties.getProperty("title", "");
        return config;
    }

    static PlayerConfig fromFile(File file) {
        try{
            Properties pps = new Properties();
            pps.load(new FileInputStream(file));
            return fromPropertirs(pps);
        } catch (Exception e) {
            PlayerConfig result = new PlayerConfig();
            File parent = file.getParentFile();
            if(parent != null) {
                result.title = parent.getName();
            }
            else {
                result.title = "<???>";
            }
            return result;
        }
    }

    boolean store(File file) {
        try {
            Properties pps = toProperties();
            pps.store(new FileOutputStream(file), "");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String serialize() {
        Properties properties = toProperties();
        properties.setProperty("indexPage", this.indexPage);
        StringWriter writer = new StringWriter();
        try {
            properties.store(writer, "");
        } catch (Exception e) {
            return null;
        }
        return writer.toString();
    }

    public static PlayerConfig deserialize(String data) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(data));
        }catch (Exception e) {
            return null;
        }
        PlayerConfig config = PlayerConfig.fromPropertirs(properties);
        config.indexPage = properties.getProperty("indexPage","");
        return config;
    }
}
