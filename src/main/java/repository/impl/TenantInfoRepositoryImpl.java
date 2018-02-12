package repository.impl;

import models.SFDCConfigDocument;
import models.TenantPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import repository.MongoBaseRepository;
import repository.SFDCConfigDocumentRepository;
import repository.TenantPoolRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by athakur on 2/11/2018.
 */
@Repository
public class TenantInfoRepositoryImpl extends MongoBaseRepository implements TenantPoolRepository {

    public TenantPool getTenantInfo(String teamName) {
        TenantPool tenantPoolDocument = mongoTemplate.findOne(Query.query(Criteria.where("teamName").is(teamName)), TenantPool.class);
        if(tenantPoolDocument != null) {
            return tenantPoolDocument;
        } else {
            throw new NullPointerException(String.format("Unknown %s teamName", teamName));
        }
    }

    @Override
    public List<SFDCConfigDocument> getAllSFDCConfigDocumentsForATeam(String teamName) {
        TenantPool tenantPoolDocument = getTenantInfo(teamName);
        SFDCConfigDocumentRepository sfdcConfigDocumentRepository = new SFDCConfigDocumentRepositoryImpl();
        return tenantPoolDocument.getProfiles().stream()
                .map(profile -> sfdcConfigDocumentRepository.getSFDCConfigDocument(profile.getProfileName()))
                .collect(Collectors.toList());
    }
}
