package providers;

import config.MongoConfig;
import gherkin.deps.com.google.gson.JsonObject;
import gherkin.deps.com.google.gson.JsonParser;
import utils.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by athakur on 2/11/2018.
 */
public class MongoConfigProvider {

    private static File mongoConfigJson = new File("C:\\Users\\athakur\\Desktop\\Automation\\executeScriptsInBeta\\resources\\mongo-config.json");

    private MongoConfigProvider() {

    }

    public static MongoConfig getMongoConfig() {
        JsonObject loadedConfig = null;
        try {
            JsonParser parser = new JsonParser();
            loadedConfig = parser.parse(new FileReader("C:\\Users\\athakur\\Desktop\\Automation\\executeScriptsInBeta\\resources\\mongo-config.json")).getAsJsonObject();
            MongoConfig mongoConfig = new MongoConfig();
            mongoConfig.setMongoDB(loadedConfig.get("mongo_db").getAsString());
            mongoConfig.setMongoHost(loadedConfig.get("mongo_host").getAsString());
            mongoConfig.setMongoPort(loadedConfig.get("mongo_port").getAsString());
            mongoConfig.setMongoUsername(loadedConfig.get("mongo_username").getAsString());
            mongoConfig.setMongoPassword(loadedConfig.get("mongo_password").getAsString());
            mongoConfig.setMongoSSLEnabled(loadedConfig.get("mongo_ssl_enable").getAsString().equalsIgnoreCase("true") ? true : false);
            return mongoConfig;
        } catch (IOException e) {
            Log.info("IOException occured - " + e.getMessage());
            return null;
        } catch (NullPointerException nullPointerException) {
            return null;
        }
    }

}
