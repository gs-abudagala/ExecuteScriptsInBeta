package models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by athakur on 2/11/2018.
 */
@Getter
@Setter
@Document(collection = "SfdcConfig")
public class SFDCConfigDocument {

    String profileName;
    SFDCConfig sfdcConfig;

    @Tolerate
    public SFDCConfigDocument() {

    }
}
