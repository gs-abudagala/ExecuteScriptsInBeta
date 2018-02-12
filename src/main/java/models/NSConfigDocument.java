package models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by athakur on 2/11/2018.
 */
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "NsConfig")
public class NSConfigDocument {

    private NSConfig nsConfig;
    private String profileName;

    @Tolerate
    public NSConfigDocument() {

    }

}
