package repository;

import models.SFDCConfigDocument;

/**
 * Created by athakur on 2/11/2018.
 */
public interface SFDCConfigDocumentRepository {

    SFDCConfigDocument getSFDCConfigDocument(String profileName);

    SFDCConfigDocument getDefaultSFDCConfigDocument();
}
