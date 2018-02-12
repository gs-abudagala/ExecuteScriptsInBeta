package providers;

import com.mongodb.*;
import config.MongoConfig;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

/**
 * Created by athakur on 2/11/2018.
 */
public class MongoTemplateProvider {

    private static MongoTemplate mongoTemplate;

    public static void init() {
        MongoConfig mongoConfig = MongoConfigProvider.getMongoConfig();
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.sslEnabled(true).sslInvalidHostNameAllowed(true).socketTimeout(90000).
                minHeartbeatFrequency(25).heartbeatSocketTimeout(4000);
        Mongo mongo = new MongoClient(new ServerAddress(mongoConfig.getMongoHost(), Integer.parseInt(mongoConfig.getMongoPort())),
                Collections.singletonList(MongoCredential.createCredential(mongoConfig.getMongoUsername(), mongoConfig.getMongoDB(), mongoConfig.getMongoPassword().toCharArray()))
                , builder.build());
        mongoTemplate = new MongoTemplate(mongo, mongoConfig.getMongoDB());
    }

    public static MongoTemplate get() {
        if (mongoTemplate == null) {
            init();
        }
        return mongoTemplate;
    }

}
