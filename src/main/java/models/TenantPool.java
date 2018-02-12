package models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Created by athakur on 2/11/2018.
 */
@Getter
@Setter
@Document(collection = "TenantPool")
public class TenantPool {

    @Field("teamName")
    private String teamName;
    @Field("profiles")
    private List<Profile> profiles;

    @Tolerate
    public TenantPool() {
    }
}
