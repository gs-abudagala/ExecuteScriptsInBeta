package repository;

import models.SFDCConfigDocument;
import models.TenantPool;

import java.util.List;

/**
 * Created by athakur on 2/11/2018.
 */
public interface TenantPoolRepository {

    TenantPool getTenantInfo(String teamName);

    List<SFDCConfigDocument> getAllSFDCConfigDocumentsForATeam(String teamName);
}
