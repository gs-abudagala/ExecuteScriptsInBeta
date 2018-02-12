package config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by athakur on 2/11/2018.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoConfig implements IConfig {

    @Field("mongo_host")
    private String mongoHost;
    @Field("mongo_port")
    private String mongoPort;
    @Field("mongo_db")
    private String mongoDB;
    @Field("mongo_username")
    private String mongoUsername;
    @Field("mongo_password")
    private String mongoPassword;
    @Field("mongo_ssl_enable")
    private boolean mongoSSLEnabled;

    @Tolerate
    public MongoConfig() {

    }

}
