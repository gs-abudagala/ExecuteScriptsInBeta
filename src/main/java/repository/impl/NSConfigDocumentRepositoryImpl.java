package repository.impl;

import models.NSConfigDocument;
import models.TenantPool;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import repository.MongoBaseRepository;
import repository.NSConfigDocumentRepository;

/**
 * Created by athakur on 2/11/2018.
 */
@Repository
public class NSConfigDocumentRepositoryImpl extends MongoBaseRepository implements NSConfigDocumentRepository {

    public NSConfigDocument getNSConfigDocument(String profileName) {
        NSConfigDocument nsConfigDocument = mongoTemplate.findOne(Query.query(Criteria.where("profileName").is(profileName)), NSConfigDocument.class);
        if(nsConfigDocument != null) {
            return nsConfigDocument;
        } else {
            throw new NullPointerException(String.format("Unknown %s profileName", profileName));
        }
    }

}
