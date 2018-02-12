package repository;

import models.NSConfigDocument;

/**
 * Created by athakur on 2/11/2018.
 */
public interface NSConfigDocumentRepository {

    NSConfigDocument getNSConfigDocument(String profileName);

}
