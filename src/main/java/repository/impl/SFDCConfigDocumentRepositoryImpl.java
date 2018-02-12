package repository.impl;

import models.SFDCConfigDocument;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import repository.MongoBaseRepository;
import repository.SFDCConfigDocumentRepository;

/**
 * Created by athakur on 2/11/2018.
 */
@Component
@Repository
public class SFDCConfigDocumentRepositoryImpl extends MongoBaseRepository implements SFDCConfigDocumentRepository {

    @Override
    public SFDCConfigDocument getSFDCConfigDocument(String profileName) {
        SFDCConfigDocument sfdcConfigDocument = mongoTemplate.findOne(Query.query(Criteria.where("profileName").is(profileName)), SFDCConfigDocument.class);
        SFDCConfigDocument sfdcDefaultConfig = getDefaultSFDCConfigDocument();
        if(sfdcConfigDocument != null) {
            sfdcConfigDocument.getSfdcConfig().setSfdcAPIVersion(sfdcDefaultConfig.getSfdcConfig().getSfdcAPIVersion());
            sfdcConfigDocument.getSfdcConfig().setSfdcPartnerUrl(sfdcDefaultConfig.getSfdcConfig().getSfdcPartnerUrl());
            return sfdcConfigDocument;
        } else {
            throw new NullPointerException(String.format("Unknown %s profile", profileName));
        }
    }

    @Override
    public SFDCConfigDocument getDefaultSFDCConfigDocument() {
        SFDCConfigDocument sfdcConfigDocument = mongoTemplate.findOne(Query.query(Criteria.where("profileName").is("default")), SFDCConfigDocument.class);
        if(sfdcConfigDocument != null) {
            return sfdcConfigDocument;
        } else {
            throw new NullPointerException("Unknown default profile");
        }
    }


}
