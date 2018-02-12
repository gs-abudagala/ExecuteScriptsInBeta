package repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import providers.MongoTemplateProvider;

/**
 * Created by athakur on 2/11/2018.
 */
public class MongoBaseRepository {

    protected MongoTemplate mongoTemplate;

    public MongoBaseRepository() {
        mongoTemplate = MongoTemplateProvider.get();
    }
}
